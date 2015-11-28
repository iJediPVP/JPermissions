package me.ijedi.jpermissions.inventories.Other;

import me.ijedi.jpermissions.inventories.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class RemovePerm {

    //Variables
    private String name = "Remove Permission";

    //Get inventory
    public Inventory getInventory(String permission, List<String> lore){
        Inventory inventory = Bukkit.createInventory(null, 27, name);
        CreateItem ci = new CreateItem();

        //Set items
        lore.set(0, ChatColor.GOLD + "" + org.bukkit.ChatColor.ITALIC + "Remove this permission from..");
        lore.add(ChatColor.GOLD + "" + org.bukkit.ChatColor.ITALIC + "Permission: " + ChatColor.GREEN + "" + org.bukkit.ChatColor.ITALIC + permission);


        inventory.setItem(12, ci.makeItem(Material.WOOL, (short) 13, ChatColor.GREEN + "" + ChatColor.BOLD + "Confirm", lore, false));
        inventory.setItem(14, ci.makeItem(Material.WOOL, (short) 14, ChatColor.RED + "" + ChatColor.BOLD + "Cancel", lore, false));
        lore.clear();
        inventory.setItem(22, ci.makeItem(Material.ARROW, (short) 0, ChatColor.RED + "" + ChatColor.BOLD + "Back", lore, false));

        return inventory;
    }

    //Get name
    public String getName() {
        return name;
    }
}
