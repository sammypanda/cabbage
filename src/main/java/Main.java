package main.java;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic
		System.out.println("Hello World");
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
