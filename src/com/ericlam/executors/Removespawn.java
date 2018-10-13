package com.ericlam.executors;

import com.ericlam.configmanager.Config;
import com.ericlam.main.RandomRespawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Removespawn implements CommandExecutor {
    private final RandomRespawn plugin;
    public Removespawn(RandomRespawn plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("you are not player!");
            return false;
        }
        Config cf = Config.getInstance();
        FileConfiguration config = cf.getConfig();
        File configfile = cf.getConfigfile();

        Player player = (Player) commandSender;

        if (strings.length != 1){
            player.sendMessage(cf.getPrefix()+"§a/removespawn <名稱>");
            return false;
        }

        String name = strings[0];
        if(!config.contains("spawn."+name)){
            player.sendMessage(cf.getPrefix()+"§c沒有此重生點!");
            return false;
        }

        config.getConfigurationSection("spawn").set(name,null);

        try {
            config.save(configfile);
            YamlConfiguration.loadConfiguration(configfile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(cf.getPrefix()+"§a刪除失敗。");
        }

        player.sendMessage(cf.getPrefix()+"§a刪除成功。");

        return true;
    }
}
