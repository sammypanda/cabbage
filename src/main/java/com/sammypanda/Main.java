package com.sammypanda;

import org.bukkit.Material;
// import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin; // plugin type
import org.bukkit.plugin.java.JavaPlugin; // essential for functionality

import com.sammypanda.command.AdminCommand;
import com.sammypanda.command.MainCommand;
import com.sammypanda.listeners.PlayerListener;

public class Main extends JavaPlugin {

	private static Plugin plugin;

	@Override
	public void onEnable() {
		// Plugin startup logic
		plugin = this;

		// Listeners
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		// register the PlayerListener event
		// TODO: replace getServer() with
		// Bukkit

		// Initiating/Creating Config
		this.saveDefaultConfig();
		for (Material material : Material.values()) {
			if (material.toString().endsWith("_DYE")) {
				this.getConfig().createSection("teams." + material.toString().replace("_DYE", "").toLowerCase());
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
}
