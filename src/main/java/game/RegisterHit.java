package main.java.game;
import main.java.Main; // needed for getPlugin

import java.util.UUID;

import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityEvent;

public class RegisterHit {
    private Boolean gamer = false; // is the player a gamer? (currently in a game of cabbage)
    
    public void Player(EntityDamageByEntityEvent event) {
        // if hit not by player don't continue
        if (event.getDamager().getType() != EntityType.PLAYER) {
            Bukkit.broadcastMessage("not hit by player, hit doesn't count");
            return;
        }

        // if game not started don't continue
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            Bukkit.broadcastMessage("game not ongoing, hit doesn't count");
            return;
        }

        // search for player
        for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            for (String player : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)) {
                if (player.equals(event.getEntity().getUniqueId().toString()) && player.equals(event.getDamager().getUniqueId().toString())) {
                    this.gamer = true;    
                
                    // do hit logic
                } else {
                    return;
                }
            }
        }
    }
}