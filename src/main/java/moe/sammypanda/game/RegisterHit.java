package moe.sammypanda.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import moe.sammypanda.Main;

public class RegisterHit {
    public void Player(EntityDamageByEntityEvent event) {
        Boolean isGamer = false; // is the player being hit a gamer?
        Boolean isHitByGamer = false; // is the player doing the hitting a gamer?

        String friendTeam = null;
        String enemyTeam = null;

        // if game not started don't continue
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            Bukkit.getLogger().finest("Game not ongoing, hit doesn't count");
            return;
        }

        // if hit not by player don't continue
        if (event.getDamager().getType() != EntityType.PLAYER) {
            Bukkit.getLogger().finest("Hit by non-player entity, hit doesn't count");
            return;
        }

        // search for player
        for (String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            for (String player : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players")
                    .getKeys(false)) {
                if (player.equals(event.getEntity().getUniqueId().toString())) {
                    isGamer = true;
                    friendTeam = team;
                }

                if (player.equals(event.getDamager().getUniqueId().toString())) {
                    isHitByGamer = true;
                    enemyTeam = team;
                }
            }
        }

        if (isGamer && isHitByGamer) { // do hit logic

            // retrieve the player object from event
            Player theGamer = Bukkit.getPlayer(event.getEntity().getUniqueId());
            Player theHitter = Bukkit.getPlayer(event.getDamager().getUniqueId());

            // retrieve our premade 'cabbage slice' ItemStack
            ItemStack theCabbage = Team.getCabbage();

            theCabbage.setAmount(1); // amount that gets passed or dropped

            if (friendTeam.equals(enemyTeam)) {
                event.setCancelled(true); // cancel the damage
            }

            if (theHitter.getInventory().containsAtLeast(theCabbage, 1) && friendTeam.equals(enemyTeam)) {
                // remove cabbage slice from hitter cuz they have one and "passed" it to us
                theHitter.getInventory().removeItem(theCabbage);

                // give us their cabbage slice! :)
                theGamer.getInventory().addItem(theCabbage);
            } else if (theGamer.getInventory().containsAtLeast(theCabbage, 1) && !friendTeam.equals(enemyTeam)) { // they
                                                                                                                  // are
                                                                                                                  // enemy!
                // remove cabbage slice from us cuz we have one and we were hit
                theGamer.getInventory().removeItem(theCabbage);

                // throw a cabbage slice to the ground for anyone to have
                Bukkit.getServer().getWorld("World").dropItem(theGamer.getLocation(), theCabbage);
            } else {
                event.setCancelled(true); // cancel the damage

                Bukkit.getPlayer(event.getDamager().getUniqueId()).sendMessage("this player has no cabbage slices");

                return;
            }
        }
    }
}