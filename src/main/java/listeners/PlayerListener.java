package main.java.listeners;
import main.java.Main; // needed for getPlugin

import main.java.game.Team;
import main.java.game.Arena;
import main.java.command.AdminCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import org.bukkit.entity.Item;

// game package
import main.java.game.RegisterHit;

public class PlayerListener implements Listener {
    String currentArena;

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        RegisterHit registerhit = new RegisterHit();
        
        registerhit.Player(event); // pass our event through to RegisterHit main function
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().isSimilar(Team.getCabbage())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        BlockState blockState = block.getState(); // static data on 'block' in this exact moment in time
        Location location = block.getLocation();

        if (blockState.getType().toString().endsWith("WOOL")) { // if the block is wool
            AdminCommand.getArena().setSpawn(block, location);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().startsWith("RIGHT") && event.getItem().getType().toString().equals("OAK_DOOR")) {
            event.setCancelled(true);
            AdminCommand.getArena().exit();
        }

        if (event.getAction().toString().startsWith("RIGHT") && event.getItem().getType().toString().equals("BARRIER")) {
            event.setCancelled(true);
            AdminCommand.getArena().delete();
        }

        if (event.getAction().toString().startsWith("LEFT") && event.getItem().getType().toString().equals("BARRIER")) {
            event.setCancelled(true);
            AdminCommand.getArena().cancel();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            return;
        }

        String color = Team.getPlayerTeam(event.getPlayer().getUniqueId().toString());

        if (color != null) {
            int total_cabbages = Main.getPlugin().getConfig().getInt("game.players");
            String arena = Main.getPlugin().getConfig().getString("game.arena");
            Location spawn = Main.getPlugin().getConfig().getLocation("arenas." + arena + ".teams." + color + ".spawn");

            if (event.getTo().distance(spawn) <= 1) { // if distance from spawn is less than or equal to 1 (block?) ~ if is at spawn
                if (event.getPlayer().getInventory().contains(Material.BONE_MEAL, total_cabbages)) {
                    Bukkit.broadcastMessage(color + " won, they have built the ultimate cabbage!");
                    AdminCommand.forceFinish();
                } else {
                    Bukkit.broadcastMessage(color + " tried to build the ultimate cabbage, they failed with not enough cabbages");
                }
            }
        }
    }
}