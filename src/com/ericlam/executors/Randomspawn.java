package com.ericlam.executors;

import com.ericlam.configmanager.Config;
import com.ericlam.main.RandomRespawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Randomspawn implements CommandExecutor {
    private final RandomRespawn plugin;
    public Randomspawn(RandomRespawn plugin){
        this.plugin = plugin;
    }
    private Location spawn;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("you are not player!");
            return false;
        }
        Config cf = Config.getInstance();
        FileConfiguration config = cf.getConfig();

        Player player = (Player) commandSender;

        if (config.getConfigurationSection("spawn") == null){
            player.sendMessage(cf.getPrefix()+"你沒有設置任何重生點!");
            return false;
        }

        Set<String> keys = config.getConfigurationSection("spawn").getKeys(false);

        if(keys.size() <= 1){
            player.sendMessage(cf.getPrefix()+"你最少需要設置兩個重生點才可運行。");
            player.sendMessage(cf.getPrefix()+"§7你目前共有"+keys.size()+"§7個重生點。");
            return false;
        }

        int Max = keys.size() - 1;


        int result = randomWithRange(0,Max);

        /*player.sendMessage("=====DEBUG=====");
        player.sendMessage("Min: 0");
        player.sendMessage("Max: "+Max);
        player.sendMessage("You get: "+result);
        player.sendMessage("===============");*/

        Iterator<String> key = keys.iterator();

        int i = 0;
        while(key.hasNext()){
            String name = key.next();
            if (i == result){
                //player.sendMessage("DEBUG: send you to teleport name: "+name);
                World world = Bukkit.getWorld(config.getString("spawn."+name+".world"));
                double x = config.getDouble("spawn."+name+".x");
                double y = config.getDouble("spawn."+name+".y");
                double z = config.getDouble("spawn."+name+".z");
                double pitch = config.getDouble("spawn."+name+".pitch");
                double yaw = config.getDouble("spawn."+name+".yaw");
                spawn = new Location(world,x,y,z,(float)yaw,(float)pitch);
                break;
            }
            i += 1;
        }

        player.teleport(spawn);
        player.sendMessage(cf.getPrefix()+"§e傳送你至一個隨機的重生點..");
        return true;
    }

    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}
