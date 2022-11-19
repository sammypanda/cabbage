package main.java.listeners;
import main.java.game.Team;
import main.java.game.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.entity.Item;

// game package
import main.java.game.RegisterHit;

public class PlayerListener implements Listener {
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
            Arena.setSpawn(block, location);
        }
    }
}