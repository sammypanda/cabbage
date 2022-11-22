package main.java.game;
import main.java.Main; // needed for getPlugin

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

public class Arena {
    String arena;
    Player player;

    public Arena(Player player, String arena) {
        this.arena = arena;
        this.player = player;

        player.getInventory().clear();

        ItemStack door = new ItemStack(Material.OAK_DOOR);
        ItemMeta doorMeta = door.getItemMeta();
        doorMeta.setDisplayName(ChatColor.ITALIC + "Exit");

        door.setItemMeta(doorMeta);
        player.getInventory().setItem(8, door); // give the admin the door in the second last hotbar slot 

        if (Main.getPlugin().getConfig().get("arenas." + arena) == null) {
            Main.getPlugin().getConfig().createSection("arenas." + arena); // create new arena if not already created
            Main.getPlugin().saveConfig();

            player.sendRawMessage("- created " + ChatColor.BOLD + arena + ".");
        }
        
        player.sendRawMessage("- use" + ChatColor.MAGIC + " colour" + ChatColor.RESET + " wool to set team spawn");
    }

    public void setSpawn(Block block, Location location) {
        BlockState state = block.getState();
        Material wool = state.getType();
        String woolColor = wool.toString().replace("_WOOL","");
        Bukkit.broadcastMessage(woolColor);

        block.setType(Material.AIR); // disappear da block

        Main.getPlugin().getConfig().set("arenas." + this.arena + "." + woolColor + ".spawn", location.toString());
        Main.getPlugin().saveConfig();
    }

    public void exit() {
        this.player.getInventory().clear();
        this.player.sendRawMessage(ChatColor.BOLD + "" + ChatColor.RED + "exited " + ChatColor.WHITE + this.arena + ChatColor.RED + " editor");
    }

    public String getName() {
        return this.arena;
    }
}