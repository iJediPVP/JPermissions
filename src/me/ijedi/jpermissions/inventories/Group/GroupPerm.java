package me.ijedi.jpermissions.inventories.Group;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupPerm {

    //Variables
    private JavaPlugin plugin;
    private static Menu menu;
    private MenuManager menuManager = new MenuManager();
    private String invName = "Group Permissions: ";

    //Constructor
    public GroupPerm(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(String groupName){

        //Try to remove extra color crap
        try{
            groupName = groupName.split(" ")[1];
        }catch(ArrayIndexOutOfBoundsException e){} //Do nothing;

        //Create menu
        invName = invName + groupName;
        menu = new Menu(invName);
        CreateItem ci = new CreateItem();

        //Get selected groups perms
        List<ItemStack> itemList = new ArrayList<>();
        for(String perm : new GroupManager(plugin).getGroup(groupName).getPermissions()){
            itemList.add(ci.makeItem(Material.INK_SACK, (short) 10,
                    ChatColor.GREEN + "" + ChatColor.BOLD + perm,
                    Arrays.asList(
                            ChatColor.GOLD + "" + ChatColor.ITALIC + "Click to remove this permission from..",
                            ChatColor.GOLD + "" + ChatColor.ITALIC + "Group: " + ChatColor.GREEN + "" + ChatColor.ITALIC + groupName),
                    false));
        }

        try{
            menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));
        }catch(NullPointerException npe){ //No perms
            ItemStack[] item = {new ItemStack(Material.AIR)};
            menu.setContents(item);
        }

        ci.setButtons(menu);

        //Create and set return & add buttons for all menu pages
        ci.addReturn(menu);
        ItemStack add = ci.makeItem(Material.INK_SACK, (short) 1,
                "Add",
                Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.ITALIC + "Add permissions",
                        ChatColor.GOLD + "" + ChatColor.ITALIC + "Group: " + ChatColor.GREEN + "" + ChatColor.ITALIC + groupName),
                false);
        for(Inventory inventory : menuManager.getMenuPageList(invName)){
            inventory.setItem(inventory.getSize() - 1, add);
        }

        return menuManager.getMenu(invName);
    }

    //Get names
    public String getName(){
        return invName;
    }


}
