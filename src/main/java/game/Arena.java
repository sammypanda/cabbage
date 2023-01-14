package main.java.game;
import main.java.Main; // needed for getPlugin
import main.java.command.AdminCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.entity.Player;

import org.bukkit.ChatColor;

import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    String arena;
    Player player;
    Boolean deleting = false;

    public Arena(Player player, String arena) {
        this.arena = arena;
        this.player = player;

        player.getInventory().clear();
        this.showCrates(true);

        ItemStack door = new ItemStack(Material.OAK_DOOR);
        ItemMeta doorMeta = door.getItemMeta();
        doorMeta.setDisplayName(ChatColor.ITALIC + "Exit");

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(ChatColor.ITALIC + "Delete");

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName(ChatColor.ITALIC + "Crate Locations");

        door.setItemMeta(doorMeta);
        barrier.setItemMeta(barrierMeta);
        chest.setItemMeta(chestMeta);
        player.getInventory().setItem(8, barrier); // give the barrier in the last hotbar slot
        player.getInventory().setItem(7, door); // give the door in the second last hotbar slot 
        player.getInventory().setItem(0, chest); // give the chest to set locations for crates to spawn in first hotbar slot

        if (Main.getPlugin().getConfig().get("arenas." + arena) == null) {
            Main.getPlugin().getConfig().createSection("arenas." + arena); // create new arena if not already created
            Main.getPlugin().saveConfig();

            player.sendRawMessage("- created " + ChatColor.BOLD + arena + ".");
        }
        
        player.sendRawMessage("- use" + ChatColor.MAGIC + " colour" + ChatColor.RESET + " wool to set team spawn");
    }

    public void setName(String name) {
        Main.getPlugin().getConfig().set("game.arena", name);
    }

    public void setSpawn(Block block, Location location) {
        BlockState state = block.getState();
        Material wool = state.getType();
        String woolColor = wool.toString().replace("_WOOL","");
        Bukkit.broadcastMessage(woolColor);

        block.setType(Material.AIR); // disappear da block

        Main.getPlugin().getConfig().set("arenas." + this.arena + ".teams." + woolColor.toLowerCase() + ".spawn", location);
        Main.getPlugin().saveConfig();
    }

    public void addCrate(Location location) {
        List<Location> locations = new ArrayList<Location>();
        
        if (Main.getPlugin().getConfig().get("arenas." + this.arena + ".crates") == null) {
            Main.getPlugin().getConfig().set("arenas." + this.arena + ".crates", new ArrayList<Location>()); // create empty crates list
        } else {
            List<Location> existingLocations = (List<Location>) Main.getPlugin().getConfig().getList("arenas." + this.arena + ".crates");
            locations.addAll(existingLocations); // pull in existing crates list for editing
        }

        if (!locations.contains(location)) {
            locations.add(location);
            Main.getPlugin().getConfig().set("arenas." + this.arena + ".crates", locations);
            
            Main.getPlugin().saveConfig();
            
            Bukkit.broadcastMessage("[wip] put chest location");
        } else {
            Bukkit.broadcastMessage("[wip] chest already placed here for this arena");
        }
    }

    // TODO: add a deleteCrate method

    public void showCrates(Boolean mode) {
        List<Location> crateLocations = (List<Location>) Main.getPlugin().getConfig().getList("arenas." + this.arena + ".crates");

        for (Location crate : crateLocations) {
            if (mode) {
                crate.getBlock().setType(Material.TRAPPED_CHEST);
            } else {
                crate.getBlock().setType(Material.AIR);
            }
        }
    }

    public void exit() {
        this.player.getInventory().clear();
        this.showCrates(false);
        AdminCommand.arenaEditor(this.player, this.arena, true);
        this.player.sendRawMessage(ChatColor.BOLD + "" + ChatColor.RED + "exited " + ChatColor.WHITE + this.arena + ChatColor.RED + " editor");
    }

    public String getName() {
        return Main.getPlugin().getConfig().getString("game.arena");
    }

    public void delete() {
        if (this.deleting) {
            Main.getPlugin().getConfig().set("arenas." + this.arena, null);
            Main.getPlugin().saveConfig();
            this.player.sendRawMessage(ChatColor.BOLD + "" + ChatColor.RED + "deleted " + ChatColor.WHITE + this.arena);
        } else {
            this.player.sendRawMessage("right click to delete, left click to cancel");
            this.deleting = true;
        }
    }

    public void cancel() {
        // resets all the pending actions to default
        if (this.deleting) {
            this.deleting = false;
            this.player.sendRawMessage("cancelled deletion");
        }
    }
}