package com.devConnor.betterFortune.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PermissionUtils {

    public static boolean doesPlayerHavePermissions(Player player) {
        if (player.isOp() || player.hasPermission("betterfortune.admin")) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
        return false;
    }

}
