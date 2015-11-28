package me.ijedi.jpermissions.inventories.Plugins;

import me.ijedi.jpermissions.inventories.Other.AddPerm;
import me.ijedi.jpermissions.inventories.Group.GroupPerm;
import me.ijedi.jpermissions.inventories.Player.PlayerPerm;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PluginClickEvent implements Listener {

    //Variables
    private JavaPlugin plugin;
    private final String CHATPREFIX = ChatColor.AQUA + "" + ChatColor.BOLD + "[JPerms] ";
    private String pluginList, pluginPerm;
    private List<String> blackList = new ArrayList<String>(){{
        add("EXIT");
        add("BACK");
        add("NEXT");
    }};

    //Constructor
    public PluginClickEvent(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        pluginList = new PluginList(plugin).getName();
        pluginPerm = new PluginPerm(plugin).getName();
    }

    //Event
    @EventHandler
    public void pluginClick(InventoryClickEvent event){

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

            //PLUGIN LIST
            if(invName.equals(pluginList)){
                //Get object info from slot 10. The list will have this plugin at minimum
                List<String> lore = event.getInventory().getItem(10).getItemMeta().getLore();
                String[] args = lore.get(1).split(": ");
                String objectType = ChatColor.stripColor(args[0]);
                String objectName = ChatColor.stripColor(args[1]);

                //Check itemName
                if(itemName.equalsIgnoreCase("RETURN")){

                    //Check if Player or group
                    if(objectType.equalsIgnoreCase("PLAYER")){
                        //Back to PlayerPerm
                        String worldName = ChatColor.stripColor(lore.get(lore.size() - 1).split(": ")[1]);
                        player.openInventory(new PlayerPerm(plugin).getInventory(worldName, lore));

                    }else if(objectType.equalsIgnoreCase("GROUP")){
                        //Back to GroupPerm
                        player.openInventory(new GroupPerm(plugin).getInventory(objectName));
                    }

                }else if(!blackList.contains(itemName.toUpperCase())){
                    //Open PluginPerm
                    player.openInventory(new PluginPerm(plugin).getInventory(itemName, event.getCurrentItem().getItemMeta().getLore()));
                }

                event.setCancelled(true);
                return;
            }

            //PLUGIN PERM
            try{
                if(invName.substring(0, pluginPerm.length()).equals(pluginPerm)){
                    List<String> lore = event.getCurrentItem().getItemMeta().getLore();

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Back to PluginList
                        player.openInventory(new PluginList(plugin).getInventory(lore));

                    }else if(!blackList.contains(itemName.toUpperCase())){
                        //Add perm...
                        player.openInventory(new AddPerm().getInventory(lore));

                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing


        }catch(NullPointerException npe){} //No inv name

    }

}

