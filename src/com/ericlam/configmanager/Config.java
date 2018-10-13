package com.ericlam.configmanager;

import com.ericlam.main.RandomRespawn;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Config {
    private Plugin plugin = RandomRespawn.plugin;
    private File configfile;
    private FileConfiguration config;
    private static Config conf;
    private String prefix;

    public static Config getInstance() {
        if (conf == null) conf = new Config();
        return conf;
    }

    public Config(){
        configfile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configfile);
        prefix = config.getString("prefix");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigfile() {
        return configfile;
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&',prefix);
    }
}
