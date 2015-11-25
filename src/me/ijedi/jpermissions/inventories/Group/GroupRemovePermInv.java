package me.ijedi.jpermissions.inventories.Group;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupRemovePermInv {

    //Variables
    private JavaPlugin plugin;
    private final String name = "Remove Group Permission";
    private String groupName;
    private String permission;

    //Constructor
    public GroupRemovePermInv(JavaPlugin plugin, String groupName){
        this.plugin = plugin;
        this.groupName = groupName;
    }

    //Create item
    public ItemStack createItem(Material material, short id, String name, List<String> lore, boolean enchant){
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
    public Inventory getInventory(String permission){
        this.permission = permission;
        Inventory inventory = Bukkit.createInventory(null, 36, name);

        //Set items
        inventory.setItem(12, createItem(Material.WOOL, (short) 13, "Confirm",
                Arrays.asList("Click to remove this permission",
                        "Group: " + groupName,
                        "Permission: " + permission),
                false));
        inventory.setItem(14, createItem(Material.WOOL, (short) 14, "Cancel", Collections.singletonList("Click to cancel the removal of perm from " + groupName), false));
        inventory.setItem(22, createItem(Material.ARROW, (short) 0, "Back", Collections.singletonList("Go back"), false));

        return inventory;
    }

    //Get name
    public String getName(){
        return name;
    }
    public String getGroupName(){
        return groupName;
    }

    //Get permission
    public String getPermission(){
        return permission;
    }
}
