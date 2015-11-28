package me.ijedi.jpermissions.inventories.Other;

import me.ijedi.jpermissions.inventories.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class MainInv {

    //Variables
    private Inventory inventory = Bukkit.createInventory(null, 27, "JPermissions");

    //Constructor
    public MainInv(){
        CreateItem ci = new CreateItem();
        //Add items
        inventory.setItem(12, ci.makeItem(Material.BOOK, (short) 0,
                ChatColor.GREEN + "" + ChatColor.BOLD + "Groups",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit groups"), false));

        inventory.setItem(14, ci.makeItem(Material.SKULL_ITEM, (short) 3,
                ChatColor.AQUA + "" + ChatColor.BOLD + "Players",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit players"), false));

        inventory.setItem(22, ci.makeItem(Material.ARROW, (short) 0,
                ChatColor.RED + "" + ChatColor.BOLD + "Exit",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Exit"), false));

        inventory.setItem(26, ci.makeItem(Material.NETHER_STAR, (short) 0,
                ChatColor.GOLD + "" + ChatColor.BOLD + "Reload",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Reload permissions"), false));
    }

    //Get inventory
    public Inventory getInventory(){
        return inventory;
    }

    //Get name
    public String getName(){
        return inventory.getName();
    }
}
