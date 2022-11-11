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
        Boolean sameTeam = false;

        String friendTeam = "a";
        String enemyTeam = "b";

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
                    friendTeam = team;
                }

                if (player.equals(event.getDamager().getUniqueId().toString())) {
                    isHitByGamer = true;
                    enemyTeam = team;
                }

                if (friendTeam.equals(enemyTeam)) {
                    sameTeam = true;
                }
            }
        }

        if (isGamer && isHitByGamer) { // do hit logic

            // retrieve the player object from event
            Player theGamer = Bukkit.getPlayer(event.getEntity().getUniqueId());
            Player theHitter = Bukkit.getPlayer(event.getDamager().getUniqueId());

            // retrieve our premade 'cabbage slice' ItemStack
            ItemStack theCabbage = Team.getCabbage();

            if (!theGamer.getInventory().containsAtLeast(theCabbage, 1) && !sameTeam) {
                event.setCancelled(true); // cancel the damage
                Bukkit.getPlayer(event.getDamager().getUniqueId()).sendMessage("this player has no cabbage slices");
                return;
            }

            theCabbage.setAmount(1);
            
            if (sameTeam) {
                event.setCancelled(true); // cancel the damage
                // remove cabbage slice from them cuz they hit us
                theHitter.getInventory().removeItem(theCabbage);

                // give us their cabbage slice! :)
                theGamer.getInventory().setItemInMainHand(theCabbage);
            } else { // they are enemy!
                // remove cabbage slice from us cuz we have one and we were hit
                theGamer.getInventory().removeItem(theCabbage);

                // throw a cabbage slice to the ground
                Bukkit.getServer().getWorld("World").dropItem(theGamer.getLocation(), theCabbage);
            }

            // temporary
            Bukkit.broadcastMessage("gamer hit gamer");

        } else {
            return;
        }
    }
}