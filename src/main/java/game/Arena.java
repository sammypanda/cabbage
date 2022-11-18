package main.java.game;
import main.java.Main; // needed for getPlugin

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import org.bukkit.ChatColor;

import org.bukkit.configuration.ConfigurationSection;

public class Arena {
    public static String editor(Player player, String arena) {
        if (Main.getPlugin().getConfig().getConfigurationSection("arena").get(arena) == null) {
            Main.getPlugin().getConfig().createSection("arena." + arena); // create new arena if not already created

            player.sendRawMessage("- created " + ChatColor.BOLD + arena + ".");
        }
        
        player.sendRawMessage("- use" + ChatColor.MAGIC + " colour" + ChatColor.RESET + " wool to set team spawn");

        return "editing " + arena;
    }   
}