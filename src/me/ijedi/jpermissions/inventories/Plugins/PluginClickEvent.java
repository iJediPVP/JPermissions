package me.ijedi.jpermissions.inventories.Plugins;

import me.ijedi.jpermissions.inventories.Group.GroupAddPerm;
import me.ijedi.jpermissions.inventories.Group.GroupPerm;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
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
                //There *should* at least be one item in the list if they are using this plugin..
                String groupName = ChatColor.stripColor(event.getInventory().getItem(10).getItemMeta().getLore().get(1));

                //Check item name
                if(itemName.equalsIgnoreCase("RETURN")){
                    //Go back to group perms
                    player.openInventory(new GroupPerm(plugin, groupName).getInventory());

                }else if(!blackList.contains(itemName.toUpperCase())){
                    //Open perm list

                    player.openInventory(new PluginPerm(plugin).getInventory(Bukkit.getPluginManager().getPlugin(itemName), groupName));

                }
                event.setCancelled(true);
                return;
            }

            //PLUGIN PERM
            try{
                if(invName.substring(0, pluginPerm.length()).equals(pluginPerm)){

                    //Get group name off return button
                    String groupName = ChatColor.stripColor(event.getInventory().getItem(event.getInventory().getSize() - 9).getItemMeta().getLore().get(1));
                    String pluginName = ChatColor.stripColor(event.getInventory().getName().substring(pluginPerm.length()));

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Go back to plugin list
                        player.openInventory(new PluginList(plugin).getInventory(groupName));

                    }else if(!blackList.contains(itemName.toUpperCase())){

                        //Check if group has this perm
                        if(new GroupManager(plugin).getGroup(groupName).hasPermission(itemName)){
                            //Tell player
                            player.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' already has this permission.", groupName)));

                            //Replace item
                            ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                            item.setItemMeta(event.getCurrentItem().getItemMeta());
                            event.getInventory().setItem(event.getSlot(), item);
                        }else{
                            //Open group add perm
                            player.openInventory(new GroupAddPerm(plugin, groupName).getInventory(pluginName, itemName));
                        }

                    }
                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing






        }catch(NullPointerException npe){} //No inv name

    }

}
