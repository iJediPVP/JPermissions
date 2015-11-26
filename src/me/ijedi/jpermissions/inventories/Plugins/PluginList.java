package me.ijedi.jpermissions.inventories.Plugins;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginList {

    //Variables
    private JavaPlugin plugin;
    private Menu menu;
    private MenuManager menuManager = new MenuManager();
    private String name = "Plugin List";

    //Constructor
    public PluginList(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(String objectName){
        menu = new Menu(name);
        CreateItem ci = new CreateItem();

        //Set items
        List<ItemStack> itemList = new ArrayList<>();
        for(Plugin jp : Bukkit.getPluginManager().getPlugins()){
            itemList.add(ci.makeItem(Material.EMERALD, (short) 0, jp.getName(),
                    Arrays.asList("Add permissions from this plugin to: ", objectName) , false));
        }

        menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

        //Set buttons
        ci.setButtons(menu);
        ci.addReturn(menu);

        return menuManager.getMenu(name);
    }

    //Get name
    public String getName() {
        return name;
    }
}
