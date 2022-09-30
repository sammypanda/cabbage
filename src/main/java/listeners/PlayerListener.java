package main.java.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) { // when player drops an item
        Bukkit.broadcastMessage("Cabbage saw you drop that!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && 
            event.getMaterial() == Material.CHEST &&
            !event.getPlayer().isSneaking())
            Bukkit.broadcastMessage("Cabbage saw that!");
    }
}