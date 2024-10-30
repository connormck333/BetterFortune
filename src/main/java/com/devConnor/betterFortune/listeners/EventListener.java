package com.devConnor.betterFortune.listeners;

import com.devConnor.betterFortune.BetterFortune;
import com.devConnor.betterFortune.managers.BlockDupeManager;
import com.devConnor.betterFortune.managers.FortuneManager;
import lombok.Builder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@Builder
public class EventListener implements Listener {

    private final BetterFortune betterFortune;
    private final FortuneManager fortuneManager;
    private final BlockDupeManager blockDupeManager;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if (block.getDrops().isEmpty()) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Material material = block.getType();
        Double playerBlockDupe = blockDupeManager.getPlayerBlockDupe(player.getUniqueId());
        if ((!doesItemHaveFortune(itemInHand) && playerBlockDupe == null) || fortuneManager.isMaterialBlacklisted(material)) {
            return;
        }

        double probabilityThreshold = fortuneManager.getProbabilityThreshold(material);

        e.setDropItems(false);
        for (ItemStack drop : block.getDrops()) {
            int newAmountToDrop = calculateAmountToDrop(itemInHand.getEnchantmentLevel(Enchantment.FORTUNE), probabilityThreshold, playerBlockDupe);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop.getType(), newAmountToDrop));
        }
    }

    private int calculateAmountToDrop(int fortuneLevel, double probabilityThreshold, Double playerBlockDupe) {
        Random random = new Random();
        playerBlockDupe = playerBlockDupe != null ? playerBlockDupe : 1;
        int amount = 1;

        for (int i = 0; i < fortuneLevel; i++) {
            if (random.nextDouble(1) <= probabilityThreshold) {
                amount++;
            }
        }

        return (int) (amount * playerBlockDupe);
    }

    private boolean doesItemHaveFortune(ItemStack item) {
        return item.containsEnchantment(Enchantment.FORTUNE);
    }
}
