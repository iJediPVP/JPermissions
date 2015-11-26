package me.ijedi.jpermissions.inventories.Group;


import me.ijedi.jpermissions.inventories.MainInv;
import me.ijedi.jpermissions.inventories.Plugins.PluginList;
import me.ijedi.jpermissions.inventories.Plugins.PluginPerm;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GroupClickEvent implements Listener {

    //Variables
    private JavaPlugin plugin;
    private final String CHATPREFIX = ChatColor.AQUA + "" + ChatColor.BOLD + "[JPerms] ";
    private String groupList, groupPerm, groupRemovePerm, groupAddPerm;
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
        groupPerm = new GroupPerm(plugin, "").getName();
        groupRemovePerm = new GroupRemovePerm(plugin, "").getName();
        groupAddPerm = new GroupAddPerm(plugin, "").getName();

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

                //Check if GroupManager has this as a group. If so open the GroupPerm inv for this group
                GroupManager gm = new GroupManager(plugin);
                if(gm.hasGroup(itemName)){
                    player.openInventory(new GroupPerm(plugin, itemName).getInventory());

                }else if(itemName.equalsIgnoreCase("RETURN")){
                    //Go back to main in
                    player.openInventory(new MainInv().getInventory());
                }

                event.setCancelled(true);
                return;
            }

            //GROUP PERM
            try{
                if(invName.substring(0, groupPerm.length()).equals(groupPerm)){

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Go back to group list
                        player.openInventory(new GroupList(plugin).getInventory());

                    }else if(itemName.equalsIgnoreCase("ADD")){
                        //Open plugin list
                        player.openInventory(new PluginList(plugin).getInventory(invName.substring(groupPerm.length())));

                    }else if(!blackList.contains(itemName.toUpperCase())){ //Make sure the item name doesn't match a menu button name.

                        //Check if this group has this perm
                        String groupName = invName.substring(groupPerm.length());
                        if(new GroupManager(plugin).getGroup(invName.substring(groupPerm.length())).hasPermission(itemName)){
                            //Open confirm remove inv
                            player.openInventory(new GroupRemovePerm(plugin, groupName).getInventory(itemName));

                        }else{
                            //Group perms have been updated between user clicks. Re-open inv
                            player.openInventory(new GroupPerm(plugin, groupName).getInventory());
                            player.sendMessage(CHATPREFIX + ChatColor.RED + "This permission has already been removed.");
                        }
                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing

            //GROUP REMOVE PERM
            if(invName.equals(groupRemovePerm)){

                //Store group and permission
                String groupName = event.getInventory().getItem(12).getItemMeta().getLore().get(1).substring("Group: ".length());
                String perm = event.getInventory().getItem(12).getItemMeta().getLore().get(2).substring("Permission: ".length());

                //Check item
                if(itemName.equalsIgnoreCase("CONFIRM")){
                    //Remove the perm
                    player.performCommand(String.format("jp g %s p r %s", groupName, perm));

                }

                //Go back to perm list
                player.openInventory(new GroupPerm(plugin, groupName).getInventory());

                event.setCancelled(true);
                return;
            }

            //GROUP ADD PERM
            if(invName.equals(groupAddPerm)){

                //Store group and permission
                String groupName = event.getInventory().getItem(12).getItemMeta().getLore().get(1).substring("Group: ".length());
                String perm = event.getInventory().getItem(12).getItemMeta().getLore().get(2).substring("Permission: ".length());
                Plugin pl = Bukkit.getPluginManager().getPlugin(event.getInventory().getItem(12).getItemMeta().getLore().get(3).substring("Plugin: ".length()));

                //Check item
                if(itemName.equalsIgnoreCase("CONFIRM")){
                    //Remove the perm
                    player.performCommand(String.format("jp g %s p a %s", groupName, perm));

                }

                //Go back to perm list
                player.openInventory(new PluginPerm(plugin).getInventory(pl, groupName));

                event.setCancelled(true);
                return;
            }

        }catch(NullPointerException npe){} //No inv name
    }
}
