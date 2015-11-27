package me.ijedi.jpermissions.inventories.Group;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupList {

    //Variables
    private JavaPlugin plugin;
    private Menu menu;
    private MenuManager menuManager = new MenuManager();
    private final String name = "Group List";

    //Constructor
    public GroupList(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(){
        menu = new Menu(name);
        CreateItem ci = new CreateItem();

        //Get groups from GroupManager
        List<ItemStack> itemList = new ArrayList<>();
        GroupManager gm = new GroupManager(plugin);
        for(String groupName : gm.getGroupSet()){
            itemList.add(ci.makeItem(Material.BOOK, (short) 0,
                    ChatColor.GREEN + "" + ChatColor.BOLD + groupName,
                    Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Click to edit this group"), false));
        }

        menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

        //Set buttons
        ci.setButtons(menu);
        ci.addReturn(menu);

        return menuManager.getMenu(name);
    }

    //Get name
    public String getName(){
        return name;
    }

}
