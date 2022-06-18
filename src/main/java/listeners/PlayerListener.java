package main.java.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) { // when player drops an item
        Bukkit.broadcastMessage("Cabbage saw you drop that!");
    }
}