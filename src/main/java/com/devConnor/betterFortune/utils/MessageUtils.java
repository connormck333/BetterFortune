package com.devConnor.betterFortune.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    private static final String MESSAGE_PREFIX = "[BetterFortune] ";

    public static void sendWarningToConsole(String message) {
        System.out.println(MESSAGE_PREFIX + message);
    }

    public static String invalidDouble() {
        return ChatColor.RED + "Please enter a valid number";
    }
}
