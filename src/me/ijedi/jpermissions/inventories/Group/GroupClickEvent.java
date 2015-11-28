package me.ijedi.jpermissions.inventories.Group;


import me.ijedi.jpermissions.inventories.Other.MainInv;
import me.ijedi.jpermissions.inventories.Plugins.PluginList;
import me.ijedi.jpermissions.inventories.Other.RemovePerm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GroupClickEvent implements Listener {

    //Variables
    private JavaPlugin plugin;
    private String groupList, groupPerm;
    private List<String> blackList = new ArrayList<String>(){{
        add("EXIT");
        add("BACK");
        add("NEXT");
    }};

    //Constructor
    public GroupClickEvent(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        groupList = new GroupList(plugin).getName();
        groupPerm = new GroupPerm(plugin).getName();

    }

    //Event
    @EventHandler
    public void groupClick(InventoryClickEvent event){

        //Check for item name
        String itemName;
        try{
            itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
        }catch(NullPointerException npe){
            return;
        }


        //Check inventory name
        try{
            String invName = ChatColor.stripColor(event.getInventory().getName());
            Player player = (Player) event.getWhoClicked();

            //GROUP LIST
            if(invName.equals(groupList)){

                //Check itemName
                if(itemName.equalsIgnoreCase("RETURN")){
                    //Back to MainInv
                    player.openInventory(new MainInv().getInventory());

                }else if(!blackList.contains(itemName.toUpperCase())){
                    //Open GroupPerm
                    player.openInventory(new GroupPerm(plugin).getInventory(itemName));
                }

                event.setCancelled(true);
                return;
            }

            //GROUP PERM
            try{
                if(invName.substring(0, groupPerm.length()).equals(groupPerm)){

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Back to GroupList
                        player.openInventory(new GroupList(plugin).getInventory());

                    }else if(itemName.equalsIgnoreCase("ADD")){
                        //Open PluginList
                        player.openInventory(new PluginList(plugin).getInventory(event.getCurrentItem().getItemMeta().getLore()));

                    }else if(!blackList.contains(itemName.toUpperCase())){
                        //Open RemovePerm
                        player.openInventory(new RemovePerm().getInventory(itemName, event.getCurrentItem().getItemMeta().getLore()));
                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing


        }catch(NullPointerException npe){} //No inv name
    }
}
