package main.java.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Test");
        if (!(command.getName().equalsIgnoreCase(label))) {
            sender.sendMessage("Ran test command with alias: \"" + label + "!\"");
        }
        return true;
    }
}