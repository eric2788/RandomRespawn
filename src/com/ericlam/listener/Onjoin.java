package com.ericlam.listener;

import com.ericlam.addon.StatusManager;
import com.ericlam.configmanager.Config;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class Onjoin implements Listener {
    private StatusManager stats = StatusManager.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        FileConfiguration config = Config.getInstance().getConfig();
        Player player = e.getPlayer();
        UUID puuid = player.getUniqueId();
        int healthscale = config.getInt("healthscale");
        player.setHealthScale(healthscale);
        int maxhealth = config.getInt("maxhealth");
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxhealth);
        if (stats.getWhoAlreadyHaveFirstHeal().contains(puuid)) return;
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        stats.getWhoAlreadyHaveFirstHeal().add(puuid);
    }
}
