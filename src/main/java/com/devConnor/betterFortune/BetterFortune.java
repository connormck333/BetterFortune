package com.devConnor.betterFortune;

import com.devConnor.betterFortune.commands.BlockDupeCommand;
import com.devConnor.betterFortune.commands.FortuneCommand;
import com.devConnor.betterFortune.listeners.ConnectListener;
import com.devConnor.betterFortune.listeners.EventListener;
import com.devConnor.betterFortune.managers.BlockDupeManager;
import com.devConnor.betterFortune.managers.ConfigManager;
import com.devConnor.betterFortune.managers.FortuneManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterFortune extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.setup(this);

        FortuneManager fortuneManager = new FortuneManager();
        BlockDupeManager blockDupeManager = new BlockDupeManager();
        Bukkit.getPluginManager().registerEvents(
                EventListener.builder()
                        .betterFortune(this)
                        .fortuneManager(fortuneManager)
                        .blockDupeManager(blockDupeManager)
                        .build(),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                ConnectListener.builder()
                        .blockDupeManager(blockDupeManager)
                        .build(),
                this
        );

        getCommand("betterfortune").setExecutor(new FortuneCommand(fortuneManager));
        getCommand("blockdupe").setExecutor(new BlockDupeCommand(blockDupeManager));
    }
}
