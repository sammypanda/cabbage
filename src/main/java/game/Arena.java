package main.java.game;
import main.java.Main; // needed for getPlugin

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import org.bukkit.configuration.ConfigurationSection;

public class Arena {
    public static String editor(Player player, String arena) {
        Main.getPlugin().getConfig().createSection("arena." + arena);

        player.sendRawMessage("test, this is a 'raw' message.. you are working on " + arena);

        return "editing " + arena;
    }   
}