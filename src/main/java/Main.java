package main.java;

import org.bukkit.plugin.java.JavaPlugin; // essential for functionality
// import org.bukkit.event.HandlerList

import main.java.listeners.*; // import every listener

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic
		getServer().getPluginManager().registerEvents(new PlayerListener(), this); // register the PlayerListener event
	}
	
	@Override
	public void onDisable() {
		// HandlerList.unregisterAll(Listener);
	}
}
