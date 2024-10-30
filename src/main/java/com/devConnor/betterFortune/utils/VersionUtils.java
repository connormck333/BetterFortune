package com.devConnor.betterFortune.utils;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.devConnor.betterFortune.utils.MessageUtils.sendWarningToConsole;

public class VersionUtils {

    public static boolean isVersionAtLeast(String versionToCheck) {
        String currentVersion = Bukkit.getVersion();

        // Extract major and minor versions from the current version
        String regex = "MC: 1\\.(\\d+)(?:\\.(\\d+))?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(currentVersion);

        if (matcher.find()) {
            int currentMajor = Integer.parseInt(matcher.group(1));
            int currentMinor = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;

            // Extract major and minor from the versionToCheck
            String[] versionParts = versionToCheck.split("\\.");
            if (versionParts.length < 2) {
                return false; // Invalid version to check
            }

            int checkMajor = Integer.parseInt(versionParts[0]);
            int checkMinor = Integer.parseInt(versionParts[1]);

            // Compare versions: check if the current version is at least the version to check
            return (currentMajor > checkMajor) || (currentMajor == checkMajor && currentMinor >= checkMinor);
        }

        return false;
    }

    public static Enchantment getFortuneEnchant() {
        try {
            if (isVersionAtLeast("20.6")) {
                return (Enchantment) Enchantment.class.getDeclaredField("FORTUNE").get(null);
            } else {
                return (Enchantment) Enchantment.class.getDeclaredField("LOOT_BONUS_BLOCKS").get(null);
            }
        } catch (Exception e) {
            sendWarningToConsole("Error: could not find fortune enchantment enum");
            throw new RuntimeException(e);
        }
    }

}
