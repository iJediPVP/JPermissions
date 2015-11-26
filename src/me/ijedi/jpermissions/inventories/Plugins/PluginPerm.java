package me.ijedi.jpermissions.inventories.Plugins;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.Group;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PluginPerm {

    //Variables
    private JavaPlugin javaPlugin;
    private static Menu menu;
    private MenuManager menuManager = new MenuManager();
    private String invName = "Permission List: ";

    //Constructor
    public PluginPerm(JavaPlugin javaPlugin){
        this.javaPlugin = javaPlugin;
    }

    //Get inventory
    public Inventory getInventory(Plugin plugin, String objectName){

        String name = plugin.getName();
        invName = invName + name;
        menu = new Menu(invName);

        CreateItem ci = new CreateItem();

        //Get perms for this plugin
        Group group = new GroupManager(javaPlugin).getGroup(objectName);
        List<ItemStack> itemList = new ArrayList<>();
        for(Permission perm : plugin.getDescription().getPermissions()){
            if(group.hasPermission(perm.getName())){
                itemList.add(ci.makeItem(Material.INK_SACK, (short) 10, perm.getName(), Collections.singletonList(objectName + " already has this permission"), false));
            }else{
                itemList.add(ci.makeItem(Material.INK_SACK, (short) 8, perm.getName(), Arrays.asList("Click to add this perm to: ", objectName), false));
            }
        }
        try{
            menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));
        }catch(NullPointerException npe){ //No perms for this plugin
            ItemStack[] item = {new ItemStack(Material.AIR)};
            menu.setContents(item);
        }

        //Set buttons
        ci.setButtons(menu);
        ItemStack returnItem = ci.makeItem(Material.ARROW, (short) 0, "Return", Arrays.asList("Return to: ", objectName), false);
        ci.addReturn(menu, returnItem);

        return menuManager.getMenu(invName);
    }

    //Get name
    public String getName(){
        return invName;
    }


}
