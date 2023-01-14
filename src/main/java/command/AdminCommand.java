package main.java.command;
import main.java.Main; // needed for getPlugin

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

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
        return AdminCommand.start("default", 1);
    }

    public static String start(String arena) {
        return AdminCommand.start(arena, 1);
    }

    public static String start(String arena, int cabbagePerPlayer) {
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
                ),
                cabbagePerPlayer,
                arena
            );
        }

        // drop crates
        Random random = new Random();
        List<Location> crateLocations = Arena.getCrates(arena);
        int teamCount = Main.getPlugin().getConfig().getConfigurationSection("arenas." + arena + ".teams").getKeys(false).size();
        ItemStack theCabbage = Team.getCabbage();

        int playerCount = Main.getPlugin().getConfig().getInt("game.players");
        int totalCabbages = playerCount * cabbagePerPlayer;

        List<Location> chosenCrates = new ArrayList<Location>();
        
        for ( int i=0; i<teamCount; i++ ) { // (for each team)
            // assess list of crates
            int crateCount = crateLocations.size();

            // pull out a random crateLocation
            int randIndex = random.nextInt(crateCount);
            Location chosenCrate = crateLocations.get(randIndex);
            chosenCrates.add(chosenCrate);

            // remove out selection from list
            crateLocations.remove(chosenCrate);

            // place it in the world
            chosenCrate.getBlock().setType(Material.CHEST);
            Chest crate = (Chest) chosenCrate.getBlock().getState();
            Inventory crateContents = crate.getInventory();
            
            // fill the crate with x cabbage slices
            int cabbageCount = totalCabbages / (teamCount + 1);
            theCabbage.setAmount(cabbageCount);
            crateContents.addItem(theCabbage);

            Bukkit.broadcastMessage("spawned a crate! go find it >:3");
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