package main.java.command;
import main.java.Main; // needed for getPlugin

import java.util.ArrayList; // import ArrayList program
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.Location;


public class MainCommand implements CommandExecutor {

    public static void teamPrep(String team, Set<String> players, Color color, CommandSender sender, Location location) {
        if (players.isEmpty()) {
            sender.sendMessage("no players joined " + team);
        } else {
            for (String player : players) {

                UUID playerUUID = UUID.fromString(player);
                Player playerObject = Bukkit.getPlayer(playerUUID);

                if (playerObject != null) {

                    PlayerInventory inventory = playerObject.getInventory();
                    ItemStack centralChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);

                    LeatherArmorMeta meta = (LeatherArmorMeta) centralChestplate.getItemMeta();
                    
                    AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "centralKnockbackResistance", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                    meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);
                    meta.setColor(color);

                    centralChestplate.setItemMeta(meta);

                    inventory.setChestplate(centralChestplate);

					// teleport the player
					Location playerLocation = playerObject.getLocation();
					playerObject.teleport(
						location
					);

                }

                sender.sendMessage("- " + player );
            }
        }
    }

    public static void endGame(List<String> players) {
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // TEST: Main.getScoreboard();
        for (Team scoreboardTeam : Main.getScoreboard().getTeams()) {
            Bukkit.getLogger().info("scoreboard team: " + scoreboardTeam.getName());
            for (String scoreboardTeamEntries : scoreboardTeam.getEntries()) {
                Bukkit.getLogger().info("scoreboard " + scoreboardTeam.getName() + " team entries: " + scoreboardTeamEntries);
            }
        }
        // End test

        String uuid = Bukkit.getPlayer(sender.getName()).getUniqueId().toString();

        if (args.length == 0) {

            sender.sendMessage("Test");
            return true;

        }

        else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {// if the first argument after the command i.e. /command *help*

                sender.sendMessage("Test");
                return true;
                
            }

            else if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {

                sender.sendMessage("Enter team you intend to join (Blue, Red, Green)");
                return false;

            }

            else if (args[0].equalsIgnoreCase("start")) {

                if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == true) {
                    sender.sendMessage("Game already ongoing");
                    return false;
                }

                Main.getPlugin().getConfig().set("game.ongoing", true);

                Main.getPlugin().saveConfig();

                teamPrep(
                    "blue", 
                    Main.getPlugin().getConfig().getConfigurationSection("teams.blue.players").getKeys(false), 
                    Color.BLUE, 
                    sender,
                    new Location(
                        Bukkit.getServer().getWorld("World"),
                        252.500,
                        -60,
                        820.500,
                        -136,
                        34
                    )
                );

                teamPrep(
                    "red", 
                    Main.getPlugin().getConfig().getConfigurationSection("teams.red.players").getKeys(false), 
                    Color.RED, 
                    sender,
                    new Location(
                        Bukkit.getServer().getWorld("World"),
                        252.500,
                        -60,
                        820.500,
                        -136,
                        34
                    )
                );

                teamPrep(
                    "green", 
                    Main.getPlugin().getConfig().getConfigurationSection("teams.green.players").getKeys(false), 
                    Color.GREEN, 
                    sender, 
                    new Location(
                        Bukkit.getServer().getWorld("World"),
                        252.500,
                        -60,
                        820.500,
                        -136,
                        34
                    )
                );

                // TEST: looping through config.yml paths
                for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                    Bukkit.getLogger().info("team: " + team); // logs out all teams
                    for (String player : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)) {
                        Bukkit.getLogger().info(team + " player: " + player);
                    }
                }
                // end test
                
            }

            if (args[0].equalsIgnoreCase("forcefinish")) {
                sender.sendMessage("finishing");

                for (String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                    for (String strUUID : Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)) {
                        Bukkit.getPlayer(UUID.fromString(strUUID)).teleport(
                            Main.getPlugin().getConfig().getLocation("teams." + team + ".players." + strUUID + ".origin")
                        ); // teleport player back to their origin position
                    }
                }
            }
        }

        else if (args.length == 2) {
            
            if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {

                Boolean userHasTeam = false;
                String userTeam = null;

                // for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                //     UUID realUUID = UUID.fromString(uuid);
                //     Player playerObject = Bukkit.getPlayer(realUUID);

                //     if (Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false).contains(uuid)) {
                //         userHasTeam = true;
                //         userTeam = team;

                //         Main.getPlugin().getConfig().set("teams." + team + ".players." + uuid + ".origin", playerObject.getLocation());
                //     }
                // }

                if (!userHasTeam) {

                    String list_map;

                    switch(args[1].toLowerCase()) {

                        case "blue":
                            sender.sendMessage("You joined the" + ChatColor.BLUE + " blue " + ChatColor.RESET + "team");
                            list_map = "teams.blue.players";
                            break;

                        case "red":
                            sender.sendMessage("You joined the" + ChatColor.RED + " red " + ChatColor.RESET + "team");
                            list_map = "teams.red.players";
                            break;
                        
                        case "green":
                            sender.sendMessage("You joined the" + ChatColor.GREEN + " green " + ChatColor.RESET + "team");
                            list_map = "teams.green.players";
                            break;

                        default:
                            sender.sendMessage("Failed");
                            return false;
                    }

                    Main.getScoreboard().getTeam(args[1].toLowerCase()).addEntry(uuid);

                    Main.getPlugin().getConfig().getConfigurationSection(list_map).createSection(uuid);

                    Main.getPlugin().getConfig().set(list_map + "." + uuid + ".origin", Bukkit.getPlayer(UUID.fromString(uuid)).getLocation());

                    Main.getPlugin().saveConfig();

                } else {

                    sender.sendMessage("you already joined the " + userTeam + " team!"); // could use java reflection to colour this?

                }

            }
        }

        return false; // if nothing handled say command failed
    }
}