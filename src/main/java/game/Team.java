package main.java.game;
import main.java.Main; // needed for getPlugin

import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Location;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;

import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class Team {
    String team;
    Set<String> players;
    Color color;
    Location location;

    public Team(String team, Set<String> players, Color color, Location location) {
        // create bonemeal
        ItemStack theCabbage = this.getCabbage();
        theCabbage.setAmount(1); // variable amount given to each player

        for (String player : players) {

            UUID playerUUID = UUID.fromString(player);
            Player playerObject = Bukkit.getPlayer(playerUUID);

            if (playerObject != null) {

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

    public static ItemStack getChestplate(Color color) {
        ItemStack centralChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) centralChestplate.getItemMeta();
        
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "centralKnockbackResistance", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        chestplateMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);

        chestplateMeta.setColor(color); // diversify colour
        
        centralChestplate.setItemMeta(chestplateMeta);

        return centralChestplate;
    }
}