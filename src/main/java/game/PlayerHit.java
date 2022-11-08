package main.java.game;
import main.java.Main; // needed for getPlugin

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHit {
    
    public void RegisterHit(EntityDamageByEntityEvent event) {
        Bukkit.broadcastMessage("RegisterHit function in PlayerHit class");
    }
}