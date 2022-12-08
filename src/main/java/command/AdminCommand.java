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

    public static String start() {
        this.start("default");
    }

    public static String start(String arena) {
        this.arena = arena;

        if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == true) {
            sender.sendMessage("Game already ongoing");
            return false;
        }

        if (!Main.getPlugin().getConfig().contains("arenas." + arena)) {
            sender.sendMessage("No '"+ arena +"' arena made :(");
            return false;
        }

        Main.getPlugin().getConfig().set("game.ongoing", true);
        Main.getPlugin().getConfig().set("game.arena", arena);
        Main.getPlugin().saveConfig();

        for(String arena : Main.getPlugin().getConfig().getConfigurationSection("arenas").getKeys(false)) {
            for(String team : Main.getPlugin().getConfig().getConfigurationSection("arenas." + arena + ".teams").getKeys(false)) {

                new Team(
                    team, 
                    Main.getPlugin().getConfig().getConfigurationSection("teams."+team+".players").getKeys(false), 
                    Color.RED, // needs to be translated from type:String to type:Color
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
        }
    }

    public static void arenaEditor(Player admin, String arenaName) {
        arena = new Arena(admin, arenaName);
    }

    public static Arena getArena() {
        return arena;
    }
}