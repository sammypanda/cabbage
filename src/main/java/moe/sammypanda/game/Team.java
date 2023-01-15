package moe.sammypanda.game;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import moe.sammypanda.Main;

public class Team {
    String team;
    Set<String> players;
    Color color;
    Location location;
    int cabbagePerPlayer;
    String arena;

    public Team(String team, Set<String> players, Color color, Location location, int cabbagePerPlayer, String arena) {
        // create bonemeal
        ItemStack theCabbage = Team.getCabbage();
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
                playerObject.teleport(location);

                // give chestplate
                ItemStack centralChestplate = Team.getChestplate(color);
                inventory.setChestplate(centralChestplate);

                // give cabbage
                // inventory.setItemInMainHand(theCabbage);

            }
        }
    }

    public static ItemStack getCabbage() {
        ItemStack theCabbage = new ItemStack(Material.BONE_MEAL);
        ItemMeta cabbageMeta = theCabbage.getItemMeta();
        ArrayList<String> cabbageLore = new ArrayList<String>();

        cabbageLore.add("First team to collect all [number] wins!");

        cabbageMeta.setDisplayName("Cabbage Slice");
        cabbageMeta.setLore(cabbageLore);

        theCabbage.setItemMeta(cabbageMeta);

        return theCabbage;
    }

    public static ItemStack getChestplate(Color color) {
        ItemStack centralChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) centralChestplate.getItemMeta();

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "centralKnockbackResistance", 10,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        chestplateMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);

        chestplateMeta.setColor(color); // diversify colour

        centralChestplate.setItemMeta(chestplateMeta);

        return centralChestplate;
    }

    public static String getPlayerTeam(String uuid) {
        String output = null;

        for (String team : Main.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
            if (Main.getPlugin().getConfig().getConfigurationSection("teams." + team + ".players").getKeys(false)
                    .contains(uuid)) { // if one of the players matches our uuid
                output = team;
            }
        }

        return output;
    }
}