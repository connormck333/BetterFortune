package com.devConnor.betterFortune.commands;

import com.devConnor.betterFortune.managers.BlockDupeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.devConnor.betterFortune.utils.PermissionUtils.doesPlayerHavePermissions;

public class BlockDupeCommand implements CommandExecutor {

    private final BlockDupeManager blockDupeManager;

    public BlockDupeCommand(BlockDupeManager blockDupeManager) {
        this.blockDupeManager = blockDupeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!doesPlayerHavePermissions(player)) {
                return true;
            }
        }

        String message;
        if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            message = blockDupeManager.addPlayer(args[1], args[2]);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            message = blockDupeManager.removePlayer(args[1]);
        } else {
            return false;
        }
        sender.sendMessage(message);

        return true;
    }
}
