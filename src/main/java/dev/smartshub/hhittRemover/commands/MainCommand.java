package dev.smartshub.hhittRemover.commands;

import dev.smartshub.hhittRemover.HhittRemover;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import dev.smartshub.hhittRemover.config.MainConfigManager;
import dev.smartshub.hhittRemover.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final HhittRemover plugin;
    private final CleanManager cleanManager;
    private final MainConfigManager mainConfigManager;

    public MainCommand(HhittRemover plugin, CleanManager cleanManager, MainConfigManager mainConfigManager){
        this.plugin = plugin;
        this.cleanManager = cleanManager;
        this.mainConfigManager = mainConfigManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){

        // Check for admin permissions
        if(!sender.hasPermission("hhittremover.admin")){
            MessageUtils.msg(plugin, sender, "no-permission");
            return true;
        }

        // Reload config's file and CleanManager maps
        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
            mainConfigManager.reloadConfig();
            cleanManager.loadConfig();
            MessageUtils.msg(plugin, sender, "config-reloaded");
            return true;
        }

        // The sole command is the reload one, then no more commands are available
        MessageUtils.msg(plugin, sender, "no-such-command");
        return true;
    }
}
