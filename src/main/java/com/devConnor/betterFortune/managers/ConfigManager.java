package com.devConnor.betterFortune.managers;

import com.devConnor.betterFortune.BetterFortune;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.devConnor.betterFortune.utils.MessageUtils.sendWarningToConsole;

public class ConfigManager {

    private static BetterFortune betterFortune;
    private static FileConfiguration config;

    private static final String PLAYER_BLOCK_DUPE_PATH = "block-dupe-enabled-players";
    private static final String BLACKLISTED_BLOCKS_PATH = "ignored-blocks";
    private static final String CUSTOM_PROBABILITIES_PATH = "custom-probability-thresholds";

    public static void setup(BetterFortune betterFortune) {
        ConfigManager.betterFortune = betterFortune;
        ConfigManager.config = betterFortune.getConfig();
        betterFortune.saveDefaultConfig();
    }

    public static double getDefaultProbabilityThreshold() {
        return config.getDouble("default-probability-threshold");
    }

    public static HashSet<Material> getBlacklistedItems() {
        List<String> configMaterials = config.getStringList(BLACKLISTED_BLOCKS_PATH);
        HashSet<Material> ignoredMaterials = new HashSet<>();
        if (configMaterials.isEmpty()) {
            return ignoredMaterials;
        }

        for (String key : configMaterials) {
            Material material = loadMaterialFromString(key.toUpperCase());
            if (material != null) {
                ignoredMaterials.add(material);
            }
        }

        return ignoredMaterials;
    }

    public static HashMap<Material, Double> getProbabilityThresholds() {
        ConfigurationSection section = config.getConfigurationSection(CUSTOM_PROBABILITIES_PATH);
        HashMap<Material, Double> probabilities = new HashMap<>();
        if (section == null) {
            return probabilities;
        }

        for (String key : section.getKeys(false)) {
            Material material = loadMaterialFromString(key.toUpperCase());
            if (material != null) {
                double probability = section.getDouble(key);
                System.out.println(probability);
                if (probability > 0 && probability <= 1) {
                    probabilities.put(material, probability);
                } else {
                    sendWarningToConsole("Probability threshold for [" + material + "] must be between 0 & 1 - skipping.");
                }
            }
        }

        return probabilities;
    }

    public static Double loadPlayerDupe(UUID playerUUID) {
        ConfigurationSection section = config.getConfigurationSection(PLAYER_BLOCK_DUPE_PATH);
        if (section == null) {
            return null;
        }

        for (String playerName : section.getKeys(false)) {
            try {
                UUID uuid = Bukkit.getPlayer(playerName).getUniqueId();
                if (uuid == playerUUID) {
                    return section.getDouble(playerName);
                }
            } catch (Exception ignored) {}
        }

        return null;
    }

    public static HashMap<UUID, Double> getBlockDupePlayers() {
        ConfigurationSection section = config.getConfigurationSection(PLAYER_BLOCK_DUPE_PATH);
        HashMap<UUID, Double> enabledPlayers = new HashMap<>();
        if (section == null) {
            return enabledPlayers;
        }

        for (String playerName : section.getKeys(false)) {
            try {
                UUID uuid = Bukkit.getPlayer(playerName).getUniqueId();
                double blockDupeMultiplier = section.getDouble(playerName);
                enabledPlayers.put(uuid, blockDupeMultiplier);
            } catch (Exception e) {
                sendWarningToConsole("Could not find player: [" + playerName + "] - skipping.");
            }
        }

        return enabledPlayers;
    }

    public static boolean savePlayerDupe(String playerName, double dupeAmount) {
        ConfigurationSection section = getConfigSection(PLAYER_BLOCK_DUPE_PATH);

        try {
            section.set(playerName, dupeAmount);
            betterFortune.saveConfig();
            return true;
        } catch (Exception e) {
            sendWarningToConsole("Could not set player [" + playerName + "] dupe amount.");
            return false;
        }
    }

    public static boolean removePlayerDupe(String playerName) {
        ConfigurationSection section = config.getConfigurationSection(PLAYER_BLOCK_DUPE_PATH);
        if (section == null) {
            return false;
        }

        try {
            section.set(playerName, null);
            betterFortune.saveConfig();
            return true;
        } catch (Exception e) {
            sendWarningToConsole("Could not remove player [" + playerName + "] block dupe.");
            return false;
        }
    }

    public static boolean setBlockIgnored(Material material) {
        ConfigurationSection section = getConfigSection(BLACKLISTED_BLOCKS_PATH);

        try {
            List<String> ignoredBlocks = section.getStringList("");
            ignoredBlocks.add(material.name());
            config.set(BLACKLISTED_BLOCKS_PATH, ignoredBlocks);

            betterFortune.saveConfig();
            return true;
        } catch (Exception e) {
            sendWarningToConsole("Could not blacklist [" + material.name() + "].");
            return false;
        }
    }

    public static boolean removeBlockFromBlacklist(Material material) {
        ConfigurationSection section = getConfigSection(BLACKLISTED_BLOCKS_PATH);

        try {
            List<String> ignoredBlocks = section.getStringList("");
            String blockToRemove = null;
            for (String ignoredBlock : ignoredBlocks) {
                if (ignoredBlock.equalsIgnoreCase(material.name())) {
                    blockToRemove = ignoredBlock;
                }
            }

            if (blockToRemove == null) {
                return false;
            }

            ignoredBlocks.remove(blockToRemove);
            config.set(BLACKLISTED_BLOCKS_PATH, ignoredBlocks);

            betterFortune.saveConfig();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            sendWarningToConsole("Could not unignore [" + material.name() + "]");
            return false;
        }
    }

    public static boolean setBlockProbabilityThreshold(Material material, double threshold) {
        ConfigurationSection section = getConfigSection(CUSTOM_PROBABILITIES_PATH);

        try {
            section.set(material.name(), threshold);
            betterFortune.saveConfig();
            return true;
        } catch (Exception e) {
            sendWarningToConsole("Could not set probability threshold for [" + material.name() + "].");
            return false;
        }
    }

    private static Material loadMaterialFromString(String key) {
        try {
            return Material.valueOf(key);
        } catch (Exception e) {
            sendWarningToConsole("Could not find block: " + key + " - skipping.");
        }

        return null;
    }

    private static ConfigurationSection getConfigSection(String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            section = config.createSection(path);
        }

        return section;
    }

}
