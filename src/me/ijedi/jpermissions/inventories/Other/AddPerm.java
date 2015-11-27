package me.ijedi.jpermissions.inventories.Other;

import me.ijedi.jpermissions.inventories.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class AddPerm {

    //Variables
    private String name = "Add Permission";

    //Get inventory
    public Inventory getInventory(List<String> lore){
        Inventory inventory = Bukkit.createInventory(null, 36, name);
        CreateItem ci = new CreateItem();

        //Set items
        lore.set(0,ChatColor.GOLD + "" + ChatColor.ITALIC + "Click to remove..");
        inventory.setItem(12, ci.makeItem(Material.WOOL, (short) 13, ChatColor.GREEN + "" + ChatColor.BOLD + "Confirm", lore, false));

        lore.set(0,ChatColor.GOLD + "" + ChatColor.ITALIC + "Click to cancel..");
        inventory.setItem(14, ci.makeItem(Material.WOOL, (short) 14, ChatColor.RED + "" + ChatColor.BOLD + "Cancel", lore, false));

        while(lore.size() > 2){
            lore.remove(lore.size() - 1);
        }
        inventory.setItem(22, ci.makeItem(Material.ARROW, (short) 0, ChatColor.RED + "" + ChatColor.BOLD + "Back to previous..", lore, false));

        return inventory;
    }

    //Get name
    public String getName() {
        return name;
    }
}
