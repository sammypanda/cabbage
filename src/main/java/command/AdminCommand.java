package main.java.command;
import main.java.Main; // needed for getPlugin

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.inventory.PlayerInventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;

import main.java.game.Arena;

public class AdminCommand {
    static Arena arena;

    public static String forceFinish() {
        // don't continue if game is already stopped
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            return "Game already stopped";
        }

        Main.getPlugin().getConfig().set("game.ongoing", false);

        for (String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            for (String strUUID : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)) {

                Player player = Bukkit.getPlayer(UUID.fromString(strUUID));

                if (player != null) {
                    // clear the game items from the player (generically)
                    player.getInventory().clear();
                    
                    // teleport player back to their origin position
                    player.teleport(
                        Main.getPlugin().getConfig().getLocation("teams." + team + ".players." + strUUID + ".origin")
                    );
                }
            }

            Main.getPlugin().getConfig().set("teams." + team, "");
        }

        Main.getPlugin().saveConfig();
        return "Finishing the game";
    }

    public static void arenaEditor(Player admin, String arenaName) {
        arena = new Arena(admin, arenaName);
    }

    public static Arena getArena() {
        return arena;
    }
}