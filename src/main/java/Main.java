package main.java;

import java.util.ArrayList; // import ArrayList program

import org.bukkit.plugin.java.JavaPlugin; // essential for functionality
import org.bukkit.plugin.Plugin; // plugin type
import org.bukkit.entity.Player;
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
		this.getConfig().createSection("teams.blue.players");
		this.getConfig().createSection("teams.red.players");
		this.getConfig().createSection("teams.green.players");

		// Initiating Teams Container
		ScoreboardManager manager = getServer().getScoreboardManager();
		board = manager.getNewScoreboard();

		// Filling out teams
		Team blueTeam = board.registerNewTeam("blue");
		blueTeam.setDisplayName("Blue");
		blueTeam.setPrefix(ChatColor.BLUE + "");

		Team redTeam = board.registerNewTeam("red");
		blueTeam.setDisplayName("Red");
		redTeam.setPrefix(ChatColor.RED + "");
		
		Team greenTeam = board.registerNewTeam("green");
		blueTeam.setDisplayName("Green");
		greenTeam.setPrefix(ChatColor.GREEN + "");

		// Populating Scoreboard
		Objective objective = board.registerNewObjective("main", "dummy", "Cabbage!");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		// TEST: scoreboard set as visible
		for(Player online : getServer().getOnlinePlayers()){
			online.setScoreboard(board);
		}
		// end test

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
