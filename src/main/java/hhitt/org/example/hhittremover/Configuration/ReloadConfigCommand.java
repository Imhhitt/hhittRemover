package hhitt.org.example.hhittremover.Configuration;

import hhitt.org.example.hhittremover.HhittRemover;
import hhitt.org.example.hhittremover.Utils.Utils;
import jdk.jshell.execution.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfigCommand implements CommandExecutor {

    private final HhittRemover plugin;

    public ReloadConfigCommand(HhittRemover plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.reloadConfig();
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Messages.Reload")));
            return true;
        }

        if (!sender.hasPermission("hhittremover.admin")) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Messages.No-Permission")));
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Messages.Reload")));
        return true;
    }
}