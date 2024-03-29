package main.java.utils;

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material; // toDye()
import org.bukkit.Color; // both from*() and to*()
import org.bukkit.ChatColor; // toChatColor()

public class ColorMap {
    public static ChatColor toChatColor(Color color) {

        Map<Color, ChatColor> map = new HashMap<Color, ChatColor>();

        map.put(Color.AQUA, ChatColor.AQUA);
        map.put(Color.BLACK, ChatColor.BLACK);
        map.put(Color.BLUE, ChatColor.BLUE);
        map.put(Color.FUCHSIA, ChatColor.LIGHT_PURPLE);
        map.put(Color.GRAY, ChatColor.GRAY);
        map.put(Color.GREEN, ChatColor.GREEN);
        map.put(Color.LIME, ChatColor.GREEN);
        map.put(Color.MAROON, ChatColor.DARK_RED);
        map.put(Color.NAVY, ChatColor.DARK_BLUE);
        map.put(Color.OLIVE, ChatColor.DARK_GREEN);
        map.put(Color.ORANGE, ChatColor.GOLD);
        map.put(Color.PURPLE, ChatColor.DARK_PURPLE);
        map.put(Color.RED, ChatColor.RED);
        map.put(Color.SILVER, ChatColor.GRAY);
        map.put(Color.TEAL, ChatColor.DARK_AQUA);
        map.put(Color.WHITE, ChatColor.WHITE);
        map.put(Color.YELLOW, ChatColor.YELLOW);        

        return map.get(color);
    }

    public static Material toDye(Color color) {

        Map<Color, Material> map = new HashMap<Color, Material>();

        map.put(Color.AQUA, Material.LIGHT_BLUE_DYE);
        map.put(Color.BLACK, Material.BLACK_DYE);
        map.put(Color.BLUE, Material.BLUE_DYE);
        map.put(Color.FUCHSIA, Material.MAGENTA_DYE);
        map.put(Color.GRAY, Material.GRAY_DYE);
        map.put(Color.GREEN, Material.GREEN_DYE);
        map.put(Color.LIME, Material.LIME_DYE);
        map.put(Color.MAROON, Material.RED_DYE);
        map.put(Color.NAVY, Material.BLUE_DYE);
        map.put(Color.OLIVE, Material.GREEN_DYE);
        map.put(Color.ORANGE, Material.ORANGE_DYE);
        map.put(Color.PURPLE, Material.PURPLE_DYE);
        map.put(Color.RED, Material.RED_DYE);
        map.put(Color.SILVER, Material.LIGHT_GRAY_DYE);
        map.put(Color.TEAL, Material.CYAN_DYE);
        map.put(Color.WHITE, Material.WHITE_DYE);
        map.put(Color.YELLOW, Material.YELLOW_DYE);

        return map.get(color);
    }

    public static Color fromDye(Material dye) {

        Map<Material, Color> map = new HashMap<Material, Color>();

        map.put(Material.LIGHT_BLUE_DYE, Color.AQUA);
        map.put(Material.BLACK_DYE, Color.BLACK);
        map.put(Material.BLUE_DYE, Color.BLUE);
        map.put(Material.MAGENTA_DYE, Color.FUCHSIA);
        map.put(Material.GRAY_DYE, Color.GRAY);
        map.put(Material.GREEN_DYE, Color.GREEN);
        map.put(Material.LIME_DYE, Color.LIME);
        map.put(Material.RED_DYE, Color.MAROON);
        map.put(Material.BLUE_DYE, Color.NAVY);
        map.put(Material.GREEN_DYE, Color.OLIVE);
        map.put(Material.ORANGE_DYE, Color.ORANGE);
        map.put(Material.PURPLE_DYE, Color.PURPLE);
        map.put(Material.RED_DYE, Color.RED);
        map.put(Material.LIGHT_GRAY_DYE, Color.SILVER);
        map.put(Material.CYAN_DYE, Color.TEAL);
        map.put(Material.WHITE_DYE, Color.WHITE);
        map.put(Material.YELLOW_DYE, Color.YELLOW);

        return map.get(dye);
    }   

    public static Color fromChatColor(ChatColor chat_color) {

        Map<ChatColor, Color> map = new HashMap<ChatColor, Color>();

        map.put(ChatColor.AQUA, Color.AQUA);
        map.put(ChatColor.BLACK, Color.BLACK);
        map.put(ChatColor.BLUE, Color.BLUE);
        map.put(ChatColor.DARK_AQUA, Color.TEAL);
        map.put(ChatColor.DARK_BLUE, Color.BLUE);
        map.put(ChatColor.DARK_GRAY, Color.GRAY);
        map.put(ChatColor.DARK_GREEN, Color.GREEN);
        map.put(ChatColor.DARK_PURPLE, Color.PURPLE);
        map.put(ChatColor.DARK_RED, Color.MAROON);
        map.put(ChatColor.GOLD, Color.ORANGE);
        map.put(ChatColor.GRAY, Color.GRAY);
        map.put(ChatColor.GREEN, Color.GREEN);
        map.put(ChatColor.LIGHT_PURPLE, Color.PURPLE);
        map.put(ChatColor.RED, Color.RED);
        map.put(ChatColor.WHITE, Color.WHITE);
        map.put(ChatColor.YELLOW, Color.YELLOW);

        return map.get(chat_color);
    }

    public static Material toMaterial(String color) {
        color = color.toLowerCase();

        Map<String, Material> map = new HashMap<String, Material>();

        map.put("light_blue", Material.LIGHT_BLUE_DYE);
        map.put("black", Material.BLACK_DYE);
        map.put("blue", Material.BLUE_DYE);
        map.put("magenta", Material.MAGENTA_DYE);
        map.put("gray", Material.GRAY_DYE);
        map.put("light_gray", Material.LIGHT_GRAY_DYE);
        map.put("green", Material.GREEN_DYE);
        map.put("lime", Material.LIME_DYE);
        map.put("red", Material.RED_DYE);
        map.put("brown", Material.BROWN_DYE);
        map.put("cyan", Material.CYAN_DYE);
        map.put("white", Material.WHITE_DYE);
        map.put("orange", Material.ORANGE_DYE);
        map.put("yellow", Material.YELLOW_DYE);
        map.put("pink", Material.PINK_DYE);
        map.put("purple", Material.PURPLE_DYE);

        return map.get(color);
    }
}
