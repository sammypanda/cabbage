package main.java.command;
import main.java.command.AdminCommand;
import main.java.Main; // needed for getPlugin

import java.util.ArrayList; // import ArrayList program
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Location;

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

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

// game package
import main.java.game.Team;

public class MainCommand implements CommandExecutor {

    public static void endGame(List<String> players) {
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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

                sender.sendMessage("Enter team you intend to join (any of the dye colours)");
                return false;

            }

            else if (args[0].equalsIgnoreCase("start")) {

                if (Main.getPlugin().getConfig().getBoolean("game.ongoing") == true) {
                    sender.sendMessage("Game already ongoing");
                    return false;
                }

                if (!Main.getPlugin().getConfig().contains("arenas.default")) {
                    sender.sendMessage("No 'default' arena made :(");
                    return false;
                }

                Main.getPlugin().getConfig().set("game.ongoing", true);
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

            if (args[0].equalsIgnoreCase("forcefinish")) {
            
                sender.sendMessage(AdminCommand.forceFinish());

            }

            if (args[0].equalsIgnoreCase("editor")) {

                sender.sendMessage("Need name of arena to edit");
            }

            if (args[0].equalsIgnoreCase("list")) {
                int loop = 0;

                for(String arena : Main.getPlugin().getConfig().getConfigurationSection("arenas").getKeys(false)) {
                    Bukkit.broadcastMessage("1. " + arena);
                    loop++;
                }
            }
        }

        else if (args.length == 2) {
            
            if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {

                Boolean userHasTeam = false;
                String userTeam = null;

                for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                    UUID realUUID = UUID.fromString(uuid);
                    Player playerObject = Bukkit.getPlayer(realUUID);

                    if (!Main.getPlugin().getConfig().isSet("teams." + team + ".players")) { // if players *not* isSet (if no players set in team)
                        Main.getPlugin().getConfig().createSection("teams." + team + ".players"); // create the players object
                    } else { // we have some players in this team!
                        if (Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false).contains(uuid)) { // if one of the players matches our uuid
                            userHasTeam = true;
                            userTeam = team;
    
                            Main.getPlugin().getConfig().set("teams." + team + ".players." + uuid + ".origin", playerObject.getLocation());
                        }
                    }
                }

                if (!userHasTeam) {

                    String list_map;

                    for (Material material : Material.values()) {
                        if (material.toString().endsWith("_DYE")) {
                            String color = material.toString().replace("_DYE", "").toLowerCase();

                            if (args[1].toLowerCase().equals(color)) {
                                sender.sendMessage("You joined the " + color + " team");
                                list_map = "teams."+color+".players";

                                Main.getPlugin().getConfig().getConfigurationSection(list_map).createSection(uuid);

                                Main.getPlugin().getConfig().set(list_map + "." + uuid + ".origin", Bukkit.getPlayer(UUID.fromString(uuid)).getLocation());

                                Main.getPlugin().saveConfig();
                            }
                        }
                    }

                } else {

                    sender.sendMessage("you already joined the " + userTeam + " team!"); // could use java reflection to colour this?

                }

            }

            if (args[0].equalsIgnoreCase("editor")) {

                AdminCommand.arenaEditor(Bukkit.getPlayer(UUID.fromString(uuid)), args[1]);
                
            }
        }

        return false; // if nothing handled say command failed
    }
}