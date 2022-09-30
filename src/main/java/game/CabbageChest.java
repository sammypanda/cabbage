package main.java.game;

import main.java.Main;
import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CabbageChest {
    private Block block;
    private boolean expired;
    private Instant expiryDate;

    public CabbageChest(Location loc, Instant expiry) {
        expired = false;
        expiryDate = expiry;
        block = World.getBlockAt(loc);
        block.setType(Material.CHEST);

        // To lock a chest, we need to set a key, but we don't want it to be
        // unlocked until we say so
        block.getState().setLock("Anyone holding this key hates women");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
            Main.getPlugin(),
            this.expire,
            ChronoUnit.SECONDS.between(Instant.now(), expiryDate) * 20
        );
    }

    public void expire() {
        expired = true;
        Chest data = block.getState();
        data.setLock(null);
        data.open();
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public Block getBlock() {
        return block;
    }
}