package main.java.game;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import main.java.Main; // needed for getPlugin

public class Arena {
    String arena;
    Player player;
    Boolean deleting = false;

    public Arena(Player player, String arena) {
        this.arena = arena;
        this.player = player;

        player.getInventory().clear();

        ItemStack door = new ItemStack(Material.OAK_DOOR);
        ItemMeta doorMeta = door.getItemMeta();
        doorMeta.setDisplayName(ChatColor.ITALIC + "Exit");

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(ChatColor.ITALIC + "Delete");

        door.setItemMeta(doorMeta);
        barrier.setItemMeta(barrierMeta);
        player.getInventory().setItem(8, barrier); // give the admin the barrier in the last hotbar slot
        player.getInventory().setItem(7, door); // give the admin the door in the second last hotbar slot

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
        String woolColor = wool.toString().replace("_WOOL", "");
        Bukkit.broadcastMessage(woolColor);

        block.setType(Material.AIR); // disappear da block

        Main.getPlugin().getConfig().set("arenas." + this.arena + ".teams." + woolColor.toLowerCase() + ".spawn",
                location);
        Main.getPlugin().saveConfig();
    }

    /**
     * Adds a new axis-aligned bounding box to the existing arena boundaries. Makes
     * no check or guarantee that the new bounding box doesn't overlap with or
     * contain any pre-existing ones, but if the same bounding box is made twice, it
     * will only register once.
     * 
     * @param positionA one corner of the bounding box to be added
     * @param positionB the opposite corner of the box
     * @see Arena.removeBounds, arena.getBounds
     */
    public void addBounds(Location positionA, Location positionB) {
        BoundingBox newBounds = BoundingBox.of(positionA, positionB);
        String path = "arenas." + this.arena + ".bounds." + newBounds.hashCode();
        Main.getPlugin().getConfig().set(path + ".from", positionA);
        Main.getPlugin().getConfig().set(path + ".to", positionB);
    }

    /**
     * Gets the axis-aligned bounding box specifying the arena region at is
     * currently in. If multiple regions are overlapping, returns only one of them,
     * with no guarantee as to which one will be returned. If the point is not
     * within the arena boundaries, returns null.
     * 
     * @param at the point to check
     * @return the axis-aligned bounding box representing the region containing at
     */
    public BoundingBox getBounds(Location at) {
        Set<String> boundses = Main
                .getPlugin()
                .getConfig()
                .getConfigurationSection("arenas." + this.arena + ".bounds")
                .getKeys();
        for (String bounds : boundses) {
            BoundingBox current = BoundingBox.of(
                    Main.getPlugin().getConfig().getLocation("arenas." + this.arena + ".bounds." + bounds + ".from"),
                    Main.getPlugin().getConfig().getLocation("arenas." + this.arena + ".bounds." + bounds + ".to"));
            if (current.contains(at.toVector())) {
                return current;
            }
        }
        return null;
    }

    /**
     * Removes the region containing at. If multiple overlapping regions contain at,
     * then only one will be removed. No guarantee is made which will be removed. If
     * at is not within the arena boundaries, does nothing.
     * 
     * @param at a location within the region to be removed
     */
    public void removeBounds(Location at) {
        BoundingBox toRemove = this.getBounds(at);
        if (toRemove != null) {
            Main.getPlugin().getConfig().set("arenas" + this.arena + ".bounds." + toRemove.hashCode(), null);
        }
    }

    /**
     * Determines the smallest world border that will enclose the current arena
     * boundaries, and sets it as the perceived world border for all current
     * players.
     */
    public void enforceBounds() {
        BoundingBox accumulator = new BoundingBox();
        Set<String> boundses = Main
                .getPlugin()
                .getConfig()
                .getConfigurationSection("arenas." + this.arena + ".bounds")
                .getKeys();
        for (String bounds : boundses) {
            BoundingBox current = BoundingBox.of(
                    Main.getPlugin().getConfig().getLocation("arenas." + this.arena + ".bounds." + bounds + ".from"),
                    Main.getPlugin().getConfig().getLocation("arenas." + this.arena + ".bounds." + bounds + ".to"));
            accumulator = accumulator.union(current);
        }
        WorldBorder updated = Bukkit.createWorldBorder();
        updated.setCenter(accumulator.getCenterX(), accumulator.getCenterZ());
        updated.setSize(Math.max(accumulator.getWidthX(), accumulator.getWidthZ()));
        for (String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            String path = "teams." + team + ".players";
            for (String player : main.getPlugin().getConfig().getConfigurationSection(path).getKeys(false)) {
                Bukkit.getPlayer(UUID.fromString(player)).setWorldBorder(updated);
            }
        }
    }

    public void exit() {
        this.player.getInventory().clear();
        this.player.sendRawMessage(ChatColor.BOLD + "" + ChatColor.RED + "exited " + ChatColor.WHITE + this.arena
                + ChatColor.RED + " editor");
    }

    public String getName() {
        return Main.getPlugin().getConfig().getString("game.arena");
    }

    public void delete() {
        if (this.deleting) {
            Main.getPlugin().getConfig().set("arenas." + this.arena, null);
            Main.getPlugin().saveConfig();
            this.player.sendRawMessage(ChatColor.BOLD + "" + ChatColor.RED + "deleted " + ChatColor.WHITE + this.arena);
            this.exit();
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