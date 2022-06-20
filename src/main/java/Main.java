package main.java;

import org.bukkit.plugin.java.JavaPlugin; // essential for functionality
// import org.bukkit.event.HandlerList

import main.java.listeners.*; // import every listener
import main.java.command.*; // import every command

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic

		// Listeners
		getServer().getPluginManager().registerEvents(new PlayerListener(), this); // register the PlayerListener event

		// Initiating/Creating Config
		this.saveDefaultConfig();
		this.getConfig().set("teams.blue.players", new List<String>());
		this.getConfig().set("teams.red.players", new List<String>());
		this.getConfig().set("teams.green.players", new List<String>());

		// Registering Command Executors
		this.getCommand("cabbage").setExecutor(new MainCommand());
	}
	
	@Override
	public void onDisable() {
		// HandlerList.unregisterAll(Listener);
	}

	public static Plugin getPlugin() {
		return this.getServer().getPlugin("Cabbage");; // for accessing plugin via other classes
	}
}
