package me.ijedi.jpermissions.inventories.Group;

import me.ijedi.jpermissions.inventories.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

public class GroupAddPerm {

    //Variables
    private JavaPlugin plugin;
    private final String name = "Add Group Permission";
    private String groupName;
    private String permission;

    //Constructor
    public GroupAddPerm(JavaPlugin plugin, String groupName){
        this.plugin = plugin;
        this.groupName = groupName;
    }

    //Get inventory
    public Inventory getInventory(String pluginName, String permission){
        CreateItem ci = new CreateItem();
        this.permission = permission;
        Inventory inventory = Bukkit.createInventory(null, 36, name);

        //Set items
        //Set items
        inventory.setItem(12, ci.makeItem(Material.WOOL, (short) 13, "Confirm",
                Arrays.asList("Click to add this permission",
                        "Group: " + groupName,
                        "Permission: " + permission,
                        "Plugin: " + pluginName),
                false));
        inventory.setItem(14, ci.makeItem(Material.WOOL, (short) 14, "Cancel", Collections.singletonList("Click to cancel the addition of perm from " + groupName), false));
        inventory.setItem(22, ci.makeItem(Material.ARROW, (short) 0, "Back", Collections.singletonList("Go back"), false));

        return inventory;
    }

    //Get name
    public String getName(){
        return name;
    }

}
