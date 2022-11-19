package main.java.game;
import main.java.Main; // needed for getPlugin

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.Material;

import org.bukkit.entity.Player;

import org.bukkit.ChatColor;

import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class Arena {
    Boolean editing;

    public static void editor(Player player, String arena) {
        if (Main.getPlugin().getConfig().get("arenas." + arena) == null) {
            Main.getPlugin().getConfig().createSection("arenas." + arena); // create new arena if not already created

            player.sendRawMessage("- created " + ChatColor.BOLD + arena + ".");
        }
        
        player.sendRawMessage("- use" + ChatColor.MAGIC + " colour" + ChatColor.RESET + " wool to set team spawn");
    }

    public static void setSpawn(Block block, Location location) {
        BlockState state = block.getState();
        Material wool = state.getType();
        String woolColor = wool.toString().replace("_WOOL","");
        Bukkit.broadcastMessage(woolColor);

        block.setType(Material.AIR); // disappear da block
    }
}