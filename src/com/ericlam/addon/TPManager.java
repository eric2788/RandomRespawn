package com.ericlam.addon;

import com.ericlam.configmanager.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class TPManager {
    private Location spawn;
    private Config cf;
    private FileConfiguration config;

    public TPManager(){
        cf = Config.getInstance();
        config = cf.getConfig();
    }

    public Location getNearestLocation(Player player,Location location){
        Location deathlocation;

        if (location == null) deathlocation = player.getLocation();
        else deathlocation = location;

        World deathworld = deathlocation.getWorld();
        double deathx = deathlocation.getX();
        double deathy = deathlocation.getY();
        double deathz = deathlocation.getZ();

        if (config.getConfigurationSection("spawn") == null){
            player.sendMessage(cf.getPrefix()+"你沒有設置任何重生點!");
            return deathlocation;
        }

        Set<String> keys = config.getConfigurationSection("spawn").getKeys(false);

        if(keys.size() <= 1){
            player.sendMessage(cf.getPrefix()+"你最少需要設置兩個重生點才可運行。");
            player.sendMessage(cf.getPrefix()+"§7你目前共有"+keys.size()+"§7個重生點。");
            return deathlocation;
        }


        for (String name : keys) {
            World world = Bukkit.getWorld(config.getString("spawn." + name + ".world"));
            //player.sendMessage("DEBUG: checking teleport name: " + name + ", world: " + world.getName());
            if (world != deathworld) {
                //player.sendMessage("DEBUG: " + world.getName() + " not match with " + world.getName() + ", skipping...");
                continue;
            }
            double x = config.getDouble("spawn." + name + ".x");
            double y = config.getDouble("spawn." + name + ".y");
            double z = config.getDouble("spawn." + name + ".z");
            double pitch = config.getDouble("spawn." + name + ".pitch");
            double yaw = config.getDouble("spawn." + name + ".yaw");
            Location current = new Location(world, x, y, z, (float) yaw, (float) pitch);

            if (spawn == null) {
                spawn = current;
                //player.sendMessage("DEBUG: Spawn is now current.");
                continue;
            }

            int gapCurrent = (int) Math.sqrt(Math.pow((current.getX() - deathx), 2) + Math.pow((current.getY() - deathy), 2) + Math.pow((current.getZ() - deathz), 2));
            //player.sendMessage("gapCurrent: "+gapCurrent);
            int gapSpawn = (int) Math.sqrt(Math.pow((spawn.getX() - deathx), 2) + Math.pow((spawn.getY() - deathy), 2) + Math.pow((spawn.getZ() - deathz), 2));
            //player.sendMessage("gapSpawn: "+gapSpawn);

            if (gapCurrent < gapSpawn) {
                //player.sendMessage("DEBUG: Current is smaller than Latest.");
                spawn = current;
                //player.sendMessage("DEBUG: Spawn is now Current.");
            }
        }

        if (spawn == null){
            player.sendMessage(cf.getPrefix()+"此世界并沒有可用重生點。");
            return deathlocation;
        }

        player.sendMessage(Config.getInstance().getPrefix()+"§e傳送你至一個最近的重生點..");
        return spawn;
    }

    public Location getRandomLocation(Player player,Location location){
        Location orig;

        if (location == null) orig = player.getLocation();
        else orig = location;

        if (config.getConfigurationSection("spawn") == null){
            player.sendMessage(cf.getPrefix()+"你沒有設置任何重生點!");
            return orig;
        }

        Set<String> keys = config.getConfigurationSection("spawn").getKeys(false);

        if(keys.size() <= 1){
            player.sendMessage(cf.getPrefix()+"你最少需要設置兩個重生點才可運行。");
            player.sendMessage(cf.getPrefix()+"§7你目前共有"+keys.size()+"§7個重生點。");
            return orig;
        }

        //player.sendMessage("DEBUG: before:");
        //player.sendMessage(keys.toArray(new String[0]));
        Set<String> filteredKey = keys.stream().filter(k -> Bukkit.getWorld(config.getString("spawn." + k + ".world")) == orig.getWorld() ).collect(Collectors.toSet());
        //player.sendMessage("DEBUG: after:");
        //player.sendMessage(filteredKey.toArray(new String[0]));

        int Max = filteredKey.size() - 1;


        int result = randomWithRange(0,Max);

        /*player.sendMessage("=====DEBUG=====");
        player.sendMessage("Min: 0");
        player.sendMessage("Max: "+Max);
        player.sendMessage("You get: "+result);
        player.sendMessage("===============");*/

        Iterator<String> key = filteredKey.iterator();

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

        if (spawn == null){
            player.sendMessage(cf.getPrefix()+"此世界并沒有可用重生點。");
            return orig;
        }

        player.sendMessage(Config.getInstance().getPrefix()+"§e傳送你至一個隨機的重生點..");
        return spawn;
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}
