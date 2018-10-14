package com.ericlam.executors;

import com.ericlam.addon.TPManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Spawnlist implements CommandExecutor {
    private final RandomRespawn plugin;
    public Spawnlist(RandomRespawn plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("you are not player!");
            return false;
        }

        FileConfiguration config = Config.getInstance().getConfig();
        String prefix = Config.getInstance().getPrefix();

        Player player = (Player) commandSender;

        if(!player.hasPermission("rr.admin")) {
            player.sendMessage(Config.getInstance().getPrefix()+"§c你沒有權限!");
            return false;
        }

        if (config.getConfigurationSection("spawn") == null){
            player.sendMessage(prefix+"你沒有設置任何重生點!");
            return false;
        }

        Set<String> keys = config.getConfigurationSection("spawn").getKeys(false);
        List<String> list = new ArrayList<>();
        list.add(prefix+"§b重生點列表:");

        for(String name : keys){
            String world = config.getString("spawn." + name + ".world");
            int x = (int) config.getDouble("spawn." + name + ".x");
            int y = (int) config.getDouble("spawn." + name + ".y");
            int z = (int) config.getDouble("spawn." + name + ".z");
            int pitch = (int) config.getDouble("spawn." + name + ".pitch");
            int yaw = (int) config.getDouble("spawn." + name + ".yaw");
            list.add(prefix+"- "+name+": "+"世界: "+world+"; XYZ: "+x+", "+y+", "+z+"; Yaw/Pitch: "+yaw+"/"+pitch);
        }

        String[] msglist = list.toArray(new String[0]);

        player.sendMessage(msglist);

        return true;

    }
}
