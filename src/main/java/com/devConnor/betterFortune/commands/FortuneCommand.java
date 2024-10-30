package com.devConnor.betterFortune.commands;

import com.devConnor.betterFortune.managers.FortuneManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.devConnor.betterFortune.utils.PermissionUtils.doesPlayerHavePermissions;

public class FortuneCommand implements CommandExecutor {

    private final FortuneManager fortuneManager;

    public FortuneCommand(FortuneManager fortuneManager) {
        this.fortuneManager = fortuneManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!doesPlayerHavePermissions(player)) {
                return true;
            }
        }

        String message;
        if (args.length == 2 && args[0].equalsIgnoreCase("ignore")) {
            message = fortuneManager.blacklistBlock(args[1]);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("unignore")) {
            message = fortuneManager.removeBlockFromBlacklist(args[1]);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            message = fortuneManager.setProbabilityThresholdForBlock(args[1], args[2]);
        } else {
            return false;
        }
        sender.sendMessage(message);

        return true;
    }
}
