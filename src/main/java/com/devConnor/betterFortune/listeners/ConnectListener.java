package com.devConnor.betterFortune.listeners;

import com.devConnor.betterFortune.managers.BlockDupeManager;
import lombok.Builder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Builder
public class ConnectListener implements Listener {

    private final BlockDupeManager blockDupeManager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        blockDupeManager.loadPlayer(e.getPlayer().getUniqueId());
    }
}
