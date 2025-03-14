package dev.smartshub.hhittRemover.util;

import dev.smartshub.hhittRemover.HhittRemover;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Msg {

    /**
     * Send a message to a CommandSender
     * @param plugin The plugin instance
     * @param sender The CommandSender to send the message to
     * @param path The path of the message in the config
     */
    public static void msg(HhittRemover plugin, CommandSender sender, String path) {
        String message = plugin.getConfig().getString("messages." + path);
        sender.sendMessage(parse(message));
    }


    /**
     * Parse a string with color codes
     * @param string The string to parse
     * @return The parsed string
     */
    private static String parse(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
