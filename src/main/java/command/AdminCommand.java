package main.java.command;
import main.java.Main; // needed for getPlugin

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Color;

import org.bukkit.inventory.PlayerInventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;

// local packages
import main.java.game.Arena;
import main.java.game.Team;
import main.java.utils.ColorMap;

public class AdminCommand {
    static Arena arena;

    public static String forceFinish() {
        // don't continue if game is already stopped
        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == false) {
            return "Game already stopped";
        }

        Main.getPlugin().getConfig().set("game.ongoing", false);
        Main.getPlugin().getConfig().set("game.players", 0);

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

    public static String start() {
        return AdminCommand.start("default");
    }

    public static String start(String arena) {
        Main.getPlugin().getConfig().set("game.arena", arena);

        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == true) {
            return "game already ongoing";
        }

        if (!Main.getPlugin().getConfig().contains("arenas." + arena)) {
            return "No '"+ arena +"' arena made :(";
        }

        Main.getPlugin().getConfig().set("game.ongoing", true);
        Main.getPlugin().getConfig().set("game.arena", arena);
        Main.getPlugin().saveConfig();
        
        for(String team : Main.getPlugin().getConfig().getConfigurationSection("arenas." + arena + ".teams").getKeys(false)) {

            new Team(
                team, 
                Main.getPlugin().getConfig().getConfigurationSection("teams."+team+".players").getKeys(false), 
                ColorMap.fromDye(ColorMap.toMaterial(team)),
                Main.getPlugin().getConfig().getLocation(
                    "arenas."+arena+".teams."+team+".spawn",
                    new Location(
                        Bukkit.getServer().getWorld("World"),
                        252.500,
                        -60,
                        820.500,
                        -136,
                        34
                    )
                )
            );
        }

        return "game started";
    }

    public static void arenaEditor(Player admin, String arenaName) {
        arena = new Arena(admin, arenaName);
    }

    public static void arenaEditor(Player admin, String arenaName, Boolean destroy) {
        if (destroy) {
            arena = null;
        } else {
            arena = new Arena(admin, arenaName);
        }
    }

    public static Arena getArena() {
        return arena;
    }
}