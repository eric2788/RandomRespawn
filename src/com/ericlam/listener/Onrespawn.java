package com.ericlam.listener;

import com.ericlam.addon.TPManager;
import com.ericlam.addon.StatusManager;
import com.ericlam.configmanager.Config;
import com.ericlam.main.RandomRespawn;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Onrespawn implements Listener {
    private Config cf = Config.getInstance();
    private StatusManager stats = StatusManager.getInstance();

    @EventHandler
    public void teleporToNearest(PlayerRespawnEvent e){
        Location respawn;
        Player player = e.getPlayer();
        if (!cf.getConfig().getBoolean("enable-nearest-respawn")){

            respawn = new TPManager().getRandomLocation(player,null);

        } else {

            respawn = new TPManager().getNearestLocation(player,null);

        }
        e.setRespawnLocation(respawn);
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e){
        FileConfiguration config = cf.getConfig();
        if (!(e.getEntity() instanceof Player) || !config.getBoolean("use-custom-respawn")) return;
        int respawnDelay= config.getInt("respawn-delay");
        double lastdamage = e.getFinalDamage();
        Player player = (Player) e.getEntity();

        if (lastdamage >= player.getHealth()){
          e.setCancelled(true);
          Location deathloc = player.getLocation();
          TPManager tpManager = new TPManager();
          GameMode gm = player.getGameMode();
          stats.getGameModeHashMap().put(player,gm);
          stats.getLocationHashMap().put(player,player.getLocation());
          player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
          player.setGameMode(GameMode.SPECTATOR);
          player.sendTitle("§e你已死亡!","§b你將在"+respawnDelay+"秒後重生...",10,respawnDelay*20,10);
            Bukkit.getScheduler().runTaskLater(RandomRespawn.plugin,()->{
                player.setGameMode(gm);

                if (cf.getConfig().getBoolean("enable-nearest-respawn")) {

                    player.teleport(tpManager.getNearestLocation(player,deathloc));

                } else {

                    player.teleport(tpManager.getRandomLocation(player,deathloc));

                }

                stats.getGameModeHashMap().remove(player);
                stats.getLocationHashMap().remove(player);
              },respawnDelay*20L);

        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        stats.handleQuit(player);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
        Player player = e.getPlayer();
        if(stats.getGameModeHashMap().containsKey(player)){
            e.setCancelled(true);
            player.sendMessage(cf.getPrefix()+"死亡時無法輸入指令!");
        }
    }
}
