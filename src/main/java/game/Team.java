package main.java.game;
import main.java.Main; // needed for getPlugin
import main.java.game.Arena;
import main.java.command.AdminCommand;

import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import org.bukkit.block.Chest;

import java.util.List;
import java.util.ArrayList;

public class Team {
    String team;
    Set<String> players;
    Color color;
    Location location;
    int cabbagePerPlayer;
    String arena;

    int playerCount = Main.getPlugin().getConfig().getInt("game.players");
    int totalCabbages = playerCount * cabbagePerPlayer;


    public Team(String team, Set<String> players, Color color, Location location, int cabbagePerPlayer, String arena) {
        // create bonemeal
        ItemStack theCabbage = this.getCabbage();
        theCabbage.setAmount(cabbagePerPlayer); // variable amount given to each player

        for (String player : players) {

            UUID playerUUID = UUID.fromString(player);
            Player playerObject = Bukkit.getPlayer(playerUUID);

            if (playerObject != null) {

                // change gamemode to adventure to stop player destroying world
                playerObject.setGameMode(GameMode.ADVENTURE);

                PlayerInventory inventory = playerObject.getInventory();

                inventory.clear(); // clear the players iventory before adding anything

                // teleport the player
                Location playerLocation = playerObject.getLocation();
                playerObject.teleport(
                    location
                );

                // give chestplate
                ItemStack centralChestplate = this.getChestplate(color);
                inventory.setChestplate(centralChestplate);

                // give cabbage
                inventory.setItemInMainHand(theCabbage);

            }
        }

        this.spawnCrate();
    }

    public static ItemStack getCabbage() {
        ItemStack theCabbage = new ItemStack(Material.BONE_MEAL);
        ItemMeta cabbageMeta = theCabbage.getItemMeta();
        ArrayList cabbageLore = new ArrayList<String>();

        cabbageLore.add("First team to collect all [number] wins!");

        cabbageMeta.setDisplayName("Cabbage Slice");
        cabbageMeta.setLore(cabbageLore);

        theCabbage.setItemMeta(cabbageMeta);

        return theCabbage;
    }

    public void spawnCrate() {
        Random random = new Random();
        List<Location> crateLocations = Arena.getCrates(this.arena);
        int teamCount = Main.getPlugin().getConfig().getConfigurationSection("arenas." + this.arena + ".teams").getKeys(false).size();
        ItemStack theCabbage = this.getCabbage();

        List<Location> chosenCrates = new ArrayList<Location>();
        
        for ( int i=0; i<teamCount; i++ ) { // (for each team)
            // assess list of crates
            int crateCount = crateLocations.size();

            // pull out a random crateLocation
            int randIndex = random.nextInt(crateCount);
            Location chosenCrate = crateLocations.get(randIndex);
            chosenCrates.add(chosenCrate);

            // remove out selection from list
            crateLocations.remove(chosenCrate);

            // place it in the world
            chosenCrate.getBlock().setType(Material.CHEST);
            Chest crate = (Chest) chosenCrate.getBlock().getState();
            Inventory crateContents = crate.getInventory();
            
            // fill the crate with x cabbage slices
            int cabbageCount = this.totalCabbages / (teamCount + 1);
            theCabbage.setAmount(cabbageCount);
            crateContents.addItem(theCabbage);

            Bukkit.broadcastMessage("spawned a crate! go find it >:3");
        }
    }

    public static ItemStack getChestplate(Color color) {
        ItemStack centralChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) centralChestplate.getItemMeta();
        
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "centralKnockbackResistance", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        chestplateMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);

        chestplateMeta.setColor(color); // diversify colour
        
        centralChestplate.setItemMeta(chestplateMeta);

        return centralChestplate;
    }

    public static String getPlayerTeam(String uuid) {
        String output = null;

        for(String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            if (Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false).contains(uuid)) { // if one of the players matches our uuid
                output = team;
            }
        }

        return output;
    }

    public static void validateWin(PlayerMoveEvent event) {
        String color = Team.getPlayerTeam(event.getPlayer().getUniqueId().toString());

        if (color != null) {
            String arena = Main.getPlugin().getConfig().getString("game.arena");
            Location spawn = Main.getPlugin().getConfig().getLocation("arenas." + arena + ".teams." + color + ".spawn");

            if (event.getTo().distance(spawn) <= 1) { // if distance from spawn is less than or equal to 1 (block?) ~ if is at spawn
                if (event.getPlayer().getInventory().contains(Material.BONE_MEAL, 100)) { // change from 1 cabbage to win
                    Bukkit.broadcastMessage(color + " won, they have built the ultimate cabbage!");
                    AdminCommand.forceFinish();
                } else {
                    Bukkit.broadcastMessage(color + " tried to build the ultimate cabbage, they failed with not enough cabbages");
                }
            }
        }
    }
}