package main.java.listeners;

import org.bukkit.Bukkit;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

// game package
import main.java.game.RegisterHit;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        RegisterHit registerhit = new RegisterHit();
        
        registerhit.Player(event); // pass our event through to RegisterHit main function
    }
}