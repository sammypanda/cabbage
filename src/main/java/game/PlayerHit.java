package main.java.game;
import main.java.Main; // needed for getPlugin

import java.util.UUID;

import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHit {
    private Boolean gamer = false; // is the player a gamer? (currently in a game of cabbage)
    
    public void RegisterHit(EntityDamageByEntityEvent event) {
        // if game not started don't continue
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            Bukkit.broadcastMessage("game not ongoing, hit doesn't count");
            return;
        }

        // search for player
        for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            for (String player : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)) {
                if (player.equals(event.getEntity().getUniqueId().toString())) {
                    this.gamer = true;
                    // do hit logic
                }
            }
        }
    }
}