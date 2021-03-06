package main.java.command;
import main.java.Main; // needed for getPlugin

import java.util.ArrayList; // import ArrayList program
import java.util.List;
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

    public static void teamPrep(String team, List<String> players, Color color, CommandSender sender, Location location) {
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // TEST: Main.getScoreboard();
        for (Team scoreboardTeam : Main.getScoreboard().getTeams()) {
            Bukkit.getLogger().info(scoreboardTeam.getName());
            for (String scoreboardTeamEntries : scoreboardTeam.getEntries()) {
                Bukkit.getLogger().info(scoreboardTeamEntries);
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

                teamPrep(
                    "blue", 
                    (List<String>) Main.getPlugin().getConfig().getStringList("teams.blue.players"), 
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
                    (List<String>) Main.getPlugin().getConfig().getStringList("teams.red.players"), 
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
                    (List<String>) Main.getPlugin().getConfig().getStringList("teams.green.players"), 
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
                    for (String player : Main.getPlugin().getConfig().getStringList("teams." + team + ".players")) {
                        Bukkit.getLogger().info(team + " player: " + player);
                    }
                }
                // end test
                
            }
        }

        else if (args.length == 2) {
            
            if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {

                Boolean userHasTeam = false;
                String userTeam = null;

                for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                    if (Main.getPlugin().getConfig().getStringList("teams." + team + ".players").contains(uuid)) {
                        userHasTeam = true;
                        userTeam = team;
                    }
                }

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

                    ArrayList<String> players = new ArrayList<String>(Main.getPlugin().getConfig().getStringList(list_map)); // prepare new list with existing list from config

                    players.add(uuid);
                    Main.getPlugin().getConfig().set(list_map, players);
                    
                    //test
                    // sender.sendMessage(Main.getPlugin().getConfig().getStringList(list_map)(0)); // sendMessage only accepts strings :(

                    Main.getPlugin().saveConfig();

                } else {

                    sender.sendMessage("you already joined the " + userTeam + " team!"); // could use java reflection to colour this?

                }

            }
        }

        return false; // if nothing handled say command failed
    }
}