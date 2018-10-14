package com.ericlam.executors;

import com.ericlam.addon.StatusManager;
import com.ericlam.configmanager.Config;
import com.ericlam.main.RandomRespawn;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Setmaxhealth implements CommandExecutor {
    private final RandomRespawn plugin;
    public Setmaxhealth(RandomRespawn plugin){
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
            player.sendMessage(cf.getPrefix()+"§a/setmaxhealth <血量>");
            return false;
        }

        try {Integer.parseInt(strings[0]);}
        catch (NumberFormatException e){
            player.sendMessage(cf.getPrefix()+"§c無效的數值!");
            return false;
        }

        int maxhealth = Integer.parseInt(strings[0]);
        config.set("maxhealth",maxhealth);

        try {
            config.save(configfile);
            YamlConfiguration.loadConfiguration(configfile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(cf.getPrefix()+"§c保存失敗");
        }

        player.sendMessage(cf.getPrefix()+"§a保存成功, 將在下次進入伺服器時生效。");

        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            StatusManager.getInstance().getWhoAlreadyHaveFirstHeal().remove(p.getUniqueId());
        }

        return true;
    }
}
