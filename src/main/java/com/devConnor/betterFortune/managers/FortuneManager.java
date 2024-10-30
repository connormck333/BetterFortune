package com.devConnor.betterFortune.managers;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;

import static com.devConnor.betterFortune.utils.ManagerUtils.deriveDoubleFromString;
import static com.devConnor.betterFortune.utils.MessageUtils.invalidDouble;

public class FortuneManager {

    @Getter
    private final double defaultProbabilityThreshold;
    private final HashMap<Material, Double> probabilityThresholds;
    private final HashSet<Material> blacklistedBlocks;

    public FortuneManager() {
        this.defaultProbabilityThreshold = ConfigManager.getDefaultProbabilityThreshold();
        this.probabilityThresholds = ConfigManager.getProbabilityThresholds();
        this.blacklistedBlocks = ConfigManager.getBlacklistedItems();
    }

    public double getProbabilityThreshold(Material material) {
        Double probabilityThreshold = probabilityThresholds.get(material);
        return probabilityThreshold != null ? probabilityThreshold : defaultProbabilityThreshold;
    }

    public boolean isMaterialBlacklisted(Material material) {
        return blacklistedBlocks.contains(material);
    }

    public String blacklistBlock(String blockName) {
        Material material = deriveMaterialFromString(blockName);
        if (material == null) {
            return invalidBlockId(blockName);
        }

        boolean success = ConfigManager.setBlockIgnored(material);
        if (!success) {
            return ChatColor.RED + "Could not ignore " + blockName + ".";
        }
        blacklistedBlocks.add(material);

        return ChatColor.GREEN + blockName + " has been ignored.";
    }

    public String removeBlockFromBlacklist(String blockName) {
        Material material = deriveMaterialFromString(blockName);
        if (material == null) {
            return invalidBlockId(blockName);
        }

        boolean success = ConfigManager.removeBlockFromBlacklist(material);
        if (!success) {
            return ChatColor.RED + "Could not unignore " + blockName + ".";
        }
        blacklistedBlocks.remove(material);

        return ChatColor.GREEN + blockName + " unignored.";
    }

    public String setProbabilityThresholdForBlock(String blockName, String thresholdStr) {
        Material material = deriveMaterialFromString(blockName);
        if (material == null) {
            return invalidBlockId(blockName);
        }

        Double threshold = deriveDoubleFromString(thresholdStr);
        if (threshold == null) {
            return invalidDouble();
        }

        boolean success = ConfigManager.setBlockProbabilityThreshold(material, threshold);
        if (!success) {
            return ChatColor.RED + "Could not set probability threshold for " + blockName;
        }

        return ChatColor.GREEN + blockName + " probability threshold set to " + threshold + ".";
    }

    private String invalidBlockId(String blockName) {
        return ChatColor.RED + blockName + " is not a valid block id";
    }

    private Material deriveMaterialFromString(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
