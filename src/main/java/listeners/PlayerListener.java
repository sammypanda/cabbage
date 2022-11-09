package main.java.listeners;

import org.bukkit.Bukkit;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

// game package
import main.java.game.PlayerHit;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        PlayerHit playerhit = new PlayerHit();
        
        playerhit.RegisterHit(event); // pass our event through to PlayerHit's main function
    }
}