package com.ericlam.addon;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class StatusManager {
    private HashMap<Player, GameMode> gameModeHashMap;
    private HashMap<Player, Location> locationHashMap;
    private HashSet<UUID> whoAlreadyHaveFirstHeal;

    public StatusManager(){
        gameModeHashMap = new HashMap<>();
        locationHashMap = new HashMap<>();
        whoAlreadyHaveFirstHeal = new HashSet<>();
    }

    private static StatusManager stats;

    public static StatusManager getInstance() {
        if (stats == null) stats = new StatusManager();
        return stats;
    }

    public HashMap<Player, GameMode> getGameModeHashMap() {
        return gameModeHashMap;
    }

    public HashMap<Player, Location> getLocationHashMap() {
        return locationHashMap;
    }

    public void handleQuit(Player player){
        if (!(locationHashMap.containsKey(player) || gameModeHashMap.containsKey(player))) return;
        player.setGameMode(gameModeHashMap.get(player));
        player.teleport(locationHashMap.get(player));
        gameModeHashMap.remove(player);
        locationHashMap.remove(player);
    }

    public HashSet<UUID> getWhoAlreadyHaveFirstHeal() {
        return whoAlreadyHaveFirstHeal;
    }
}
