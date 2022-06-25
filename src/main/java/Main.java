package main.java;

import java.util.ArrayList; // import ArrayList program

import org.bukkit.plugin.java.JavaPlugin; // essential for functionality
import org.bukkit.plugin.Plugin; // plugin type
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
		this.getConfig().set("teams.blue.players", new ArrayList<String>());
		this.getConfig().set("teams.red.players", new ArrayList<String>());
		this.getConfig().set("teams.green.players", new ArrayList<String>());

		// Initiating Teams Container
		ScoreboardManager manager = getServer().getScoreboardManager();
		board = manager.getNewScoreboard();

		// Filling out teams
		Team blueTeam = board.registerNewTeam("blue");
		blueTeam.setPrefix(ChatColor.BLUE);

		Team redTeam = board.registerNewTeam("red");
		redTeam.setPrefix(ChatColor.RED);
		
		Team greenTeam = board.registerNewTeam("green");
		greenTeam.setPrefix(ChatColor.GREEN);

		// Registering Command Executors
		this.getCommand("cabbage").setExecutor(new MainCommand());
	}
	
	@Override
	public void onDisable() {
		// HandlerList.unregisterAll(Listener);
	}

	public static Plugin getPlugin() {
		return plugin; // for accessing plugin via other classes
	}

	public static Scoreboard getScoreboard() {
		return board;
	}
}
