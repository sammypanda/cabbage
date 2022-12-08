package main.java.listeners;
import main.java.Main; // needed for getPlugin

import main.java.game.Team;
import main.java.game.Arena;
import main.java.command.AdminCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;

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
        String color = Team.getPlayerTeam(event.getPlayer().getUniqueId().toString());
        String arena = Main.getPlugin().getConfig().getString("game.arena");
        Location spawn = Main.getPlugin().getConfig().getLocation("arenas." + arena + ".teams." + color + ".spawn");
        Bukkit.broadcastMessage(color + " moved: " + event.getTo().toString() + " target: " + spawn.toString());

        if (event.getTo().equals(spawn)) {
            Bukkit.broadcastMessage(color + " wins!");
            AdminCommand.forceFinish();
        }
    }
}