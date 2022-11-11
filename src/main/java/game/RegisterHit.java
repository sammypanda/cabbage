package main.java.game;
import main.java.Main; // needed for getPlugin

import java.util.UUID;

import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;

import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

// game package
import main.java.game.Team;

public class RegisterHit {
    public void Player(EntityDamageByEntityEvent event) {
        Boolean isGamer = false; // is the player being hit a gamer?
        Boolean isHitByGamer = false; // is the player doing the hitting a gamer?

        // if game not started don't continue
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            Bukkit.broadcastMessage("game not ongoing, hit doesn't count");
            return;
        }

        // if hit not by player don't continue
        if (event.getDamager().getType() != EntityType.PLAYER) {
            Bukkit.broadcastMessage("not hit by player, hit doesn't count");
            return;
        }

        // search for player
        for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            for (String player : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)) {
                if (player.equals(event.getEntity().getUniqueId().toString())) {
                    isGamer = true;
                }

                if (player.equals(event.getDamager().getUniqueId().toString())) {
                    isHitByGamer = true;
                }
            }
        }

        if (isGamer && isHitByGamer) { // do hit logic

            // retrieve our premade 'cabbage slice' ItemStack
            ItemStack theCabbage = Team.getCabbage();

            // retrieve the player object from event
            Player theGamer = Bukkit.getPlayer(event.getEntity().getUniqueId());

            // remove cabbage slice from the gamer who was hit
            theCabbage.setAmount(1);
            theGamer.getInventory().removeItem(theCabbage);

            // throw a cabbage slice on the ground
            Bukkit.getServer().getWorld("World").dropItem(theGamer.getLocation(), theCabbage);

            // temporary
            Bukkit.broadcastMessage("gamer hit gamer");

        } else {
            return;
        }
    }
}