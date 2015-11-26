package me.ijedi.jpermissions.inventories.Group;

import me.ijedi.jpermissions.inventories.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

public class GroupRemovePerm {

    //Variables
    private JavaPlugin plugin;
    private final String name = "Remove Group Permission";
    private String groupName;
    private String permission;

    //Constructor
    public GroupRemovePerm(JavaPlugin plugin, String groupName){
        this.plugin = plugin;
        this.groupName = groupName;
    }

    //Get inventory
    public Inventory getInventory(String permission){
        CreateItem ci = new CreateItem();
        this.permission = permission;
        Inventory inventory = Bukkit.createInventory(null, 36, name);

        //Set items
        inventory.setItem(12, ci.makeItem(Material.WOOL, (short) 13, "Confirm",
                Arrays.asList("Click to remove this permission",
                        "Group: " + groupName,
                        "Permission: " + permission),
                false));
        inventory.setItem(14, ci.makeItem(Material.WOOL, (short) 14, "Cancel", Collections.singletonList("Click to cancel the removal of perm from " + groupName), false));
        inventory.setItem(22, ci.makeItem(Material.ARROW, (short) 0, "Back", Collections.singletonList("Go back"), false));

        return inventory;
    }

    //Get name
    public String getName(){
        return name;
    }
}
