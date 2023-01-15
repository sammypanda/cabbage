package moe.sammypanda.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import moe.sammypanda.Main;
import moe.sammypanda.command.AdminCommand;

public class Arena {
    String arena;
    Player player;
    Boolean mightDelete = false;

    public Arena(Player player, String arena) {
        this.arena = arena;
        this.player = player;

        player.getInventory().clear();
        this.setCrateVisibility(true);

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
        player.getInventory().setItem(0, chest); // give the chest to set locations for crates to spawn in first hotbar
                                                 // slot

        FileConfiguration config = Main.getPlugin().getConfig();
        if (config.get("arenas." + arena) == null) {
            config.createSection("arenas." + arena); // create new arena if not already created
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
        String woolColor = wool.toString().replace("_WOOL", "");
        Bukkit.broadcastMessage(woolColor);

        block.setType(Material.AIR); // disappear da block

        FileConfiguration config = Main.getPlugin().getConfig();
        String path = "arenas." + this.arena + ".teams." + woolColor.toLowerCase() + ".spawn";
        config.set(path, location);
        Main.getPlugin().saveConfig();
    }

    public static List<Location> getCrates(String arena) {
        List<Location> locations = new ArrayList<Location>();
        FileConfiguration config = Main.getPlugin().getConfig();
        String cratePath = "arenas." + arena + ".crates";
        if (config.get(cratePath) == null) {
            config.set(cratePath, new ArrayList<Location>()); // create empty crates list
        } else {
            List<Location> existingLocations = (List<Location>) config.getList(cratePath);
            locations.addAll(existingLocations); // pull in existing crates list for editing
        }
        return locations;
    }

    public void addCrate(Location location) {
        List<Location> locations = Arena.getCrates(this.arena);

        if (!locations.contains(location)) {
            locations.add(location);
            Main.getPlugin().getConfig().set("arenas." + this.arena + ".crates", locations);

            Main.getPlugin().saveConfig();

            this.player.sendRawMessage(
                    "+ " + ChatColor.BOLD + "" + ChatColor.GREEN + "added" + ChatColor.RESET + " crate location");
        }
    }

    public void deleteCrate(Location location) {
        List<Location> locations = Arena.getCrates(this.arena);

        locations.remove(location);

        Main.getPlugin().getConfig().set("arenas." + this.arena + ".crates", locations);
        Main.getPlugin().saveConfig();

        String message = "- " + ChatColor.BOLD + "" + ChatColor.RED + "deleted" + ChatColor.RESET + " crate location";
        this.player.sendRawMessage(message);
    }

    public void setCrateVisibility(Boolean visible) {
        List<Location> crateLocations = Arena.getCrates(this.arena);
        for (Location crate : crateLocations) {
            if (visible) {
                crate.getBlock().setType(Material.CHEST);
            } else {
                crate.getBlock().setType(Material.AIR);
            }
        }
    }

    public void exit() {
        this.player.getInventory().clear();
        this.setCrateVisibility(false);
        AdminCommand.arenaEditor(this.player, this.arena, true);
        String message = ChatColor.BOLD + "" + ChatColor.RED + "exited " + ChatColor.WHITE + this.arena
                + ChatColor.RED + " editor";
        this.player.sendRawMessage(message);
    }

    public String getName() {
        return Main.getPlugin().getConfig().getString("game.arena");
    }

    public void delete() {
        if (this.mightDelete) {
            Main.getPlugin().getConfig().set("arenas." + this.arena, null);
            Main.getPlugin().saveConfig();
            this.player.sendRawMessage(ChatColor.BOLD + "" + ChatColor.RED + "deleted " + ChatColor.WHITE + this.arena);
        } else {
            this.player.sendRawMessage("right click to delete, left click to cancel");
            this.mightDelete = true;
        }
    }

    public void cancel() {
        // resets all the pending actions to default
        if (this.mightDelete) {
            this.mightDelete = false;
            this.player.sendRawMessage("cancelled deletion");
        }
    }
}