package main.java;

import java.util.ArrayList; // import ArrayList program

import org.bukkit.plugin.java.JavaPlugin; // essential for functionality
import org.bukkit.plugin.Plugin; // plugin type
import org.bukkit.entity.Player;
import org.bukkit.Material;
// import org.bukkit.event.HandlerList

import main.java.listeners.*; // import every listener
import main.java.command.*; // import every command (ScoreboardManager/Scoreboard)
import org.bukkit.scoreboard.*; // managing teams
import org.bukkit.ChatColor;

public class Main extends JavaPlugin {

	private static Plugin plugin;
	private static Scoreboard board;

	@Override
	public void onEnable() {
		// Plugin startup logic
		plugin = this;

		// Listeners
		getServer().getPluginManager().registerEvents(new PlayerListener(), this); // register the PlayerListener event // TODO: replace getServer() with Bukkit

		// Initiating/Creating Config
		this.saveDefaultConfig();
		for (Material material : Material.values()) {
			if (material.toString().endsWith("_DYE")) {
				this.getConfig().createSection("teams."+material.toString().replace("_DYE", "").toLowerCase()+".players");
			}
		}
		Main.getPlugin().saveConfig();

		// Registering Command Executors
		this.getCommand("cabbage").setExecutor(new MainCommand());
	}
	
	@Override
	public void onDisable() {
		// HandlerList.unregisterAll(Listener);
		AdminCommand.forceFinish();
	}

	public static Plugin getPlugin() {
		return plugin; // for accessing plugin via other classes
	}

	public static Scoreboard getScoreboard() {
		return board;
	}
}
