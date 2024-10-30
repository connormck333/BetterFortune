package com.devConnor.betterFortune.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static com.devConnor.betterFortune.utils.ManagerUtils.deriveDoubleFromString;
import static com.devConnor.betterFortune.utils.MessageUtils.invalidDouble;

public class BlockDupeManager {

    private final HashMap<UUID, Double> enabledPlayers;

    public BlockDupeManager() {
        this.enabledPlayers = new HashMap<>();
    }

    public Double getPlayerBlockDupe(UUID uuid) {
        return enabledPlayers.get(uuid);
    }

    public void loadPlayer(UUID uuid) {
        Double dupeAmount = ConfigManager.loadPlayerDupe(uuid);
        if (dupeAmount != null) {
            enabledPlayers.put(uuid, dupeAmount);
        }
    }

    public String addPlayer(String playerName, String dupeAmountStr) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return ChatColor.RED + "Could not find player.";
        }

        Double dupeAmount = deriveDoubleFromString(dupeAmountStr);
        if (dupeAmount == null) {
            return invalidDouble();
        }

        boolean success = ConfigManager.savePlayerDupe(playerName, dupeAmount);
        if (!success) {
            return ChatColor.RED + "Could not set player's block dupe.";
        }
        this.enabledPlayers.put(player.getUniqueId(), dupeAmount);

        return ChatColor.GREEN + "Block dupe [x" + dupeAmountStr + "] enabled for " + playerName;
    }

    public String removePlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return ChatColor.RED + "Could not find player.";
        }

        UUID uuid = player.getUniqueId();
        if (!enabledPlayers.containsKey(uuid)) {
            return ChatColor.RED + "This player does not have block dupe enabled.";
        }

        boolean success = ConfigManager.removePlayerDupe(playerName);
        if (!success) {
            return ChatColor.RED + "Could not remove player's block dupe.";
        }
        enabledPlayers.remove(uuid);

        return ChatColor.GREEN + "Block dupe removed for " + playerName + ".";
    }
}
