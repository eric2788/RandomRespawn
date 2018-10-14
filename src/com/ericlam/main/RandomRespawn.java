package com.ericlam.main;

import com.ericlam.addon.StatusManager;
import com.ericlam.executors.*;
import com.ericlam.listener.Onjoin;
import com.ericlam.listener.Onrespawn;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class RandomRespawn extends JavaPlugin {
    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        File configyaml = new File(this.getDataFolder(),"config.yml");
        if (!configyaml.exists()) this.saveResource("config.yml",true);
        YamlConfiguration.loadConfiguration(configyaml);

        /*
        CommandExecutors
         */

        getCommand("randomspawn").setExecutor(new Randomspawn(this));
        getCommand("addspawn").setExecutor(new Addspawn(this));
        getCommand("removespawn").setExecutor(new Removespawn(this));
        getCommand("setmaxhealth").setExecutor(new Setmaxhealth(this));
        getCommand("nearestspawn").setExecutor(new Nearestspawn(this));
        getCommand("spawnlist").setExecutor(new Spawnlist(this));

        /*
        EventListeners
         */

        getServer().getPluginManager().registerEvents(new Onjoin(),this);
        getServer().getPluginManager().registerEvents(new Onrespawn(),this);

        getLogger().info("RandomRespawn Enabled.");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()){
            StatusManager.getInstance().handleQuit(player);
        }
        getLogger().info("RandomRespawn Disabled.");
    }
}
