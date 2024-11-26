package dev.smartshub.hhittRemover.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class MainCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        // No rocket science, if sender is admin and args are 1, then return the reload command
        if(sender.hasPermission("hhittremover.admin")){
            if(args.length == 1){
                return List.of("reload");
            }
        }
        return List.of();
    }
}
