package com.sammypanda.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sammypanda.Main;
// local packages
import com.sammypanda.utils.ColorMap;

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

                sender.sendMessage(AdminCommand.start()); // start with all game defaults
            }

            if (args[0].equalsIgnoreCase("forcefinish")) {

                sender.sendMessage(AdminCommand.forceFinish());

            }

            if (args[0].equalsIgnoreCase("editor")) {

                sender.sendMessage("Need name of arena to edit");
            }

            if (args[0].equalsIgnoreCase("list")) {
                for (String arena : Main.getPlugin().getConfig().getConfigurationSection("arenas").getKeys(false)) {
                    Bukkit.broadcastMessage("1. " + arena);
                }
            }
        }

        else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {
                String userTeam = null;

                for (String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                    UUID realUUID = UUID.fromString(uuid);
                    Player playerObject = Bukkit.getPlayer(realUUID);

                    if (!Main.getPlugin().getConfig().isSet("teams." + team + ".players")) { // if players *not* isSet
                                                                                             // (if no players set in
                                                                                             // team)
                        Main.getPlugin().getConfig().createSection("teams." + team + ".players"); // create the players
                                                                                                  // object
                    } else { // we have some players in this team!
                        if (Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players")
                                .getKeys(false).contains(uuid)) { // if one of the players matches our uuid
                            userTeam = team;

                            Main.getPlugin().getConfig().set("teams." + team + ".players." + uuid + ".origin",
                                    playerObject.getLocation());
                        }
                    }
                }

                if (userTeam == null) {
                    String list_map;

                    for (Material material : Material.values()) {
                        if (material.toString().endsWith("_DYE")) {
                            String color = material.toString().replace("_DYE", "").toLowerCase();

                            if (args[1].toLowerCase().equals(color)) {
                                sender.sendMessage("You joined the " + color + " team");
                                list_map = "teams." + color + ".players";
                                int curr_players = Main.getPlugin().getConfig().getInt("game.players");

                                Main.getPlugin().getConfig().getConfigurationSection(list_map).createSection(uuid);

                                Main.getPlugin().getConfig().set(list_map + "." + uuid + ".origin",
                                        Bukkit.getPlayer(UUID.fromString(uuid)).getLocation());
                                Main.getPlugin().getConfig().set("game.players", curr_players + 1);

                                Main.getPlugin().saveConfig();
                            }
                        }
                    }

                } else {
                    Material teamAsDye = Material.valueOf(userTeam.toUpperCase() + "_DYE");

                    sender.sendMessage("you already joined the " + ColorMap.toChatColor(ColorMap.fromDye(teamAsDye))
                            + userTeam + ChatColor.RESET + " team!");

                }

            }

            if (args[0].equalsIgnoreCase("editor")) {

                AdminCommand.arenaEditor(Bukkit.getPlayer(UUID.fromString(uuid)), args[1]);

            }

            if (args[0].equalsIgnoreCase("start")) {

                sender.sendMessage(AdminCommand.start(args[1], 1)); // pass in arena name

            }
        }

        else if (args.length == 3) {

            if (args[0].equalsIgnoreCase("start")) {

                sender.sendMessage(AdminCommand.start(args[1], Integer.parseInt(args[2]))); // pass in arena name and
                                                                                            // slices per person

            }

        }

        return false; // if nothing handled say command failed
    }
}