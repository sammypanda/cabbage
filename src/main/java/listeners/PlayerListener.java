package main.java.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) { // when player drops an item
        Bukkit.broadcastMessage("Cabbage saw you drop that!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Bukkit.broadcastMessage("Cabbage saw that!");
    }
}