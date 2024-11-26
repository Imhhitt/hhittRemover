package dev.smartshub.hhittRemover.utils;

import org.bukkit.ChatColor;

public class MessageUtils {
    public static String parse(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
