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

		// Registering Command Executors
		this.getCommand("cabbage").setExecutor(new MainCommand(this));
	}
	
	@Override
	public void onDisable() {
		// HandlerList.unregisterAll(Listener);
	}
}
