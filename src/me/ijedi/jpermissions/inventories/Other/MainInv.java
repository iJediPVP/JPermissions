package me.ijedi.jpermissions.inventories.Other;

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
    private Inventory inventory = Bukkit.createInventory(null, 36, "JPermissions");

    //Constructor
    public MainInv(){
        //Add items
        inventory.setItem(12, createItem(Material.BOOK, (short) 0,
                ChatColor.GREEN + "" + ChatColor.BOLD + "Groups",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit groups"), false));

        inventory.setItem(14, createItem(Material.SKULL_ITEM, (short) 3,
                ChatColor.AQUA + "" + ChatColor.BOLD + "Players",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit players"), false));

        inventory.setItem(22, createItem(Material.ARROW, (short) 0,
                ChatColor.RED + "" + ChatColor.BOLD + "Exit",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Exit"), false));

        inventory.setItem(26, createItem(Material.NETHER_STAR, (short) 0,
                ChatColor.GOLD + "" + ChatColor.BOLD + "Reload",
                Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Reload permissions"), false));
    }

    //Create item
    private ItemStack createItem(Material material, short id, String name, List<String> lore, boolean enchant){
        ItemStack iStack = new ItemStack(material, 1, id);
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(name);
        iMeta.setLore(lore);
        if(enchant){
            iMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            iMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        iStack.setItemMeta(iMeta);
        return iStack;
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
