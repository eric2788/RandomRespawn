package com.ericlam.executors;

import com.ericlam.configmanager.Config;
import com.ericlam.main.RandomRespawn;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Addspawn implements CommandExecutor {
    private final RandomRespawn plugin;

    public Addspawn(RandomRespawn plugin){
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

        if(!player.hasPermission("rr.admin")) {
            player.sendMessage(Config.getInstance().getPrefix()+"§c你沒有權限!");
            return false;
        }

        if (strings.length != 1){
            player.sendMessage(cf.getPrefix()+"§a/addspawn <名稱>");
            return false;
        }

        String name = strings[0];

        if(config.contains("spawn."+name)){
            player.sendMessage(cf.getPrefix()+"§c這個重生點的名字已經存在！");
            return false;
        }

        Location stand = player.getLocation();
        String world = stand.getWorld().getName();
        double x = stand.getX();
        double y = stand.getY();
        double z = stand.getZ();
        double pitch = stand.getPitch();
        double yaw = stand.getYaw();

        config.set("spawn."+name,name);
        config.set("spawn."+name+".world",world);
        config.set("spawn."+name+".x",x);
        config.set("spawn."+name+".y",y);
        config.set("spawn."+name+".z",z);
        config.set("spawn."+name+".pitch",pitch);
        config.set("spawn."+name+".yaw",yaw);

        try {
            config.save(configfile);
            YamlConfiguration.loadConfiguration(configfile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(cf.getPrefix() + "出現錯誤，無法添加。");
        }

        player.sendMessage(cf.getPrefix() + "§a已成功添加此重生點!");
        return true;
    }
}
