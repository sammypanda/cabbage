package main.java.command;
import main.java.Main; // needed for getPlugin

import java.util.ArrayList; // import ArrayList program
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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
                
                ArrayList<String> all_players = new ArrayList<>();

                List<String> blue_players = (List<String>) Main.getPlugin().getConfig().getStringList("teams.blue.players");

                List<String> red_players = (List<String>) Main.getPlugin().getConfig().getStringList("teams.red.players");

                List<String> green_players = (List<String>) Main.getPlugin().getConfig().getStringList("teams.green.players");

                Stream.of(blue_players, red_players, green_players).forEach(all_players::addAll); // loop through the lists and execute addAll into all_players

                for (String player : all_players) {
                    sender.sendMessage("- " + player );
                }

                // TEST:

                for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                    Bukkit.getLogger().info(team); // logs out all teams
                }
                
            }
        }

        else if (args.length == 2) {
            
            if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {

                String list_map;

                switch(args[1].toLowerCase()) {

                    case "blue":
                        sender.sendMessage("You joined the blue team");
                        list_map = "teams.blue.players";
                        break;

                    case "red":
                        sender.sendMessage("You joined the red team");
                        list_map = "teams.red.players";
                        break;
                    
                    case "green":
                        sender.sendMessage("You joined the green team");
                        list_map = "teams.green.players";
                        break;

                    default:
                        sender.sendMessage("Failed");
                        return false;
                }

                ArrayList<String> players = new ArrayList<String>(Main.getPlugin().getConfig().getStringList(list_map)); // prepare new list with existing list from config

                String uuid = Bukkit.getPlayer(sender.getName()).getUniqueId().toString();

                players.add(uuid);
                Main.getPlugin().getConfig().set(list_map, players);
                
                //test
                // sender.sendMessage(Main.getPlugin().getConfig().getStringList(list_map)(0)); // sendMessage only accepts strings :(

                Main.getPlugin().saveConfig();

            }
        }

        return false; // if nothing handled say command failed
    }
}