package main.java.game;
import main.java.Main; // needed for getPlugin

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;
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

    public Team() {
        for (String player : players) {

            UUID playerUUID = UUID.fromString(player);
            Player playerObject = Bukkit.getPlayer(playerUUID);

            if (playerObject != null) {

                PlayerInventory inventory = playerObject.getInventory();
                ItemStack centralChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);

                LeatherArmorMeta meta = (LeatherArmorMeta) centralChestplate.getItemMeta();
                
                AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "centralKnockbackResistance", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);
                meta.setColor(color);

                centralChestplate.setItemMeta(meta);

                inventory.clear(); // clear the players iventory before adding anything
                inventory.setChestplate(centralChestplate);

                // teleport the player
                Location playerLocation = playerObject.getLocation();
                playerObject.teleport(
                    location
                );

            }
        }
    }
}