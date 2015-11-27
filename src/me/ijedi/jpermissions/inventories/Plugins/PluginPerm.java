package me.ijedi.jpermissions.inventories.Plugins;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.Group;
import me.ijedi.jpermissions.permissions.GroupManager;
import me.ijedi.jpermissions.permissions.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    private GroupManager groupManager;
    private String invName = "Permission List: ";

    //Constructor
    public PluginPerm(JavaPlugin javaPlugin){
        this.javaPlugin = javaPlugin;
        groupManager = new GroupManager(javaPlugin);
    }

    //Get inventory
    public Inventory getInventory(String pluginName, final List<String> lore){

        //Remove first part that bukkit's chatcolor screws up.
        try{
            pluginName = pluginName.split(" ")[1];
        }catch(ArrayIndexOutOfBoundsException e){} //Do nothing.

        //Create menu
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        invName = invName + pluginName;
        Menu menu = new Menu(invName);
        CreateItem ci = new CreateItem();

        lore.add("");
        lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Plugin: " + ChatColor.GREEN + "" + ChatColor.ITALIC + pluginName);

        String objectName = ChatColor.stripColor(lore.get(1).split(": ")[1]);

        List<ItemStack> permItems = new ArrayList<>();
        for(Permission perm : plugin.getDescription().getPermissions()){

            if(hasPerm(objectName, perm.getName())){
                lore.set(0, ChatColor.GOLD + "" + ChatColor.ITALIC + "Already has this permission..");
                permItems.add(ci.makeItem(Material.INK_SACK, (short) 10, ChatColor.GREEN + "" + ChatColor.BOLD + perm.getName(), lore, false));
            }else{
                lore.set(0, ChatColor.GOLD + "" + ChatColor.ITALIC + "Add this permission..");
                permItems.add(ci.makeItem(Material.INK_SACK, (short) 8, ChatColor.RED + "" + ChatColor.BOLD + perm.getName(), lore, false));
            }
        }

        menu.setContents(permItems.toArray(new ItemStack[permItems.size()]));

        //Add buttons
        ci.setButtons(menu);

        ItemStack returnItem = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = returnItem.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Return");
        lore.set(0, ChatColor.GOLD + "" + ChatColor.ITALIC + "Return to the previous menu");
        lore.remove(lore.size() - 1); //Remove plugin name
        lore.remove(lore.size() - 1); //Remove blank space
        itemMeta.setLore(lore);
        returnItem.setItemMeta(itemMeta);
        ci.addReturn(menu, returnItem);


        return menuManager.getMenu(invName);
    }

    //Is player
    private boolean hasPerm(String objectName, String permission){
        //Check for player
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getName().equalsIgnoreCase(objectName)){
                User user = groupManager.getUser(player.getUniqueId());
                for(World world : Bukkit.getWorlds()){
                    if(user.hasPermission(world.getName(), permission)){
                        Bukkit.broadcastMessage("true");
                        return true;
                    }
                }
                return false;
            }
        }

        //Check for group
        if(groupManager.getGroup(objectName).hasPermission(permission)){
            return true;
        }
        Bukkit.broadcastMessage("OI");
        return false;

    }

    //Get name
    public String getName(){
        return invName;
    }


}
