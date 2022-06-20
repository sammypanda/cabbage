package main.java.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;


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
        }

        else if (args.length == 2) {
            
            if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("join")) {

                switch(args[1].toLowerCase()) {

                    case "blue":
                        sender.sendMessage("You joined the blue team");
                        List<String> players = new List<String>(getPlugin().getConfig().getStringList("teams.blue.players")); // prepare new list with existing list from config
                        players.add(sender);
                        getPlugin().getConfig().set("teams.blue.players", players);

                        //test
                        sender.sendMessage(getPlugin().getConfig().getStringList("teams.blue.players"));
                        break;

                    case "red":
                        sender.sendMessage("You joined the red team");
                        break;
                    
                    case "green":
                        sender.sendMessage("You joined the green team");
                        break;
                        
                }

                getPlugin().saveConfig();

            }
        }

        return false; // if nothing handled say command failed
    }
}