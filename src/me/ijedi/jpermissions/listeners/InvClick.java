/*
Add perm to group
Remove perm from player
Add perm to player
Add return buttons to most inventories
* */

package me.ijedi.jpermissions.listeners;

import me.ijedi.jpermissions.inventories.Group.GroupRemovePermInv;
import me.ijedi.jpermissions.inventories.Group.GroupPermInv;
import me.ijedi.jpermissions.inventories.Group.GroupListInv;
import me.ijedi.jpermissions.inventories.MainInv;
import me.ijedi.jpermissions.inventories.PlayerListInv;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InvClick implements Listener {

    //Variables
    private JavaPlugin plugin;
    private MenuManager menuManager = new MenuManager();
    private String mainName, groupListName, groupPermName, groupRemovePermName, playerListName;



    //Constructor
    public InvClick(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        //Get inv names
        mainName = new MainInv().getName();

        groupListName = new GroupListInv(plugin).getName();
        groupPermName = new GroupPermInv(plugin, "").getName();
        groupRemovePermName = new GroupRemovePermInv(plugin, "").getName();

        playerListName = new PlayerListInv(plugin).getName();
    }

    //Event
    @EventHandler
    public void inventoryClick(InventoryClickEvent event){
        //Check for player
        if(event.getWhoClicked() instanceof Player){
            final Player player = (Player) event.getWhoClicked();

            //Try and get item name
            String itemName;
            try{
                itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase();
            }catch(NullPointerException npe){
                return;
            }

            //Check which inventory
            String invName = event.getInventory().getName();

            //MAIN - Options to reload, edit groups, or edit players.
            if(invName.equals(mainName)){

                //Check item name
                if(itemName.equals("GROUPS")){
                    //Open group list inventory
                    player.openInventory(new GroupListInv(plugin).getInventory());

                }else if(itemName.equals("PLAYERS")){
                    //Open player list inventory after 2 ticks
                    final Inventory inventory = new PlayerListInv(plugin).getInventory();
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            player.openInventory(inventory);
                        }
                    }.runTaskLater(plugin, 2l);

                }else if(itemName.equals("EXIT")){
                    //Close inventory
                    player.closeInventory();

                }else if(itemName.equals("RELOAD")){
                    //Reload perms
                    player.performCommand("jp reload");
                }
                event.setCancelled(true);
                return;
            }

            //GROUP LIST - List current groups
            if(invName.equals(groupListName)){

                //Check item name
                GroupManager gm = new GroupManager(plugin);
                if(gm.hasGroup(itemName)){
                    player.openInventory(new GroupPermInv(plugin, itemName).getInventory());
                }

                event.setCancelled(true);
                return;
            }

            //GROUP PERM - Edit perms for a group
            try{
                if(invName.substring(0, groupPermName.length()).equals(groupPermName)){

                    //Check itemName
                    if(!itemName.equalsIgnoreCase("EXIT") && !itemName.equalsIgnoreCase("BACK") && !itemName.equalsIgnoreCase("RETURN")){
                        //Open confirm inv
                        player.openInventory(new GroupRemovePermInv(plugin, invName.substring(groupPermName.length())).getInventory(itemName));

                    }else if(itemName.equals("RETURN")){
                        //Return to group list
                        player.openInventory(new GroupListInv(plugin).getInventory());

                    }else if(itemName.equals("ADD")){
                        //Show plugin list
                        player.sendMessage("Add this ");
                    }
                    event.setCancelled(true);
                    return;
                }

            }catch(NullPointerException npe){} //Do nothing.

            //GROUP REMOVE CONFIRM - Confirmation of permission removal from group
            if(invName.equals(groupRemovePermName)){

                //Check item
                if(itemName.equals("CONFIRM")){

                    //Remove perm
                    String groupName = event.getInventory().getItem(12).getItemMeta().getLore().get(1).substring("Group: ".length());
                    String perm = event.getInventory().getItem(12).getItemMeta().getLore().get(2).substring("Permission: ".length());
                    player.performCommand("jp g " + groupName + " p r " + perm);

                    //Return to group perm
                    player.openInventory(new GroupPermInv(plugin, groupName).getInventory());

                }else if(itemName.equals("CANCEL") || itemName.equals("BACK")){
                    //Return to group perm
                    String groupName = event.getInventory().getItem(12).getItemMeta().getLore().get(1).substring("Group: ".length());
                    player.openInventory(new GroupPermInv(plugin, groupName).getInventory());
                }
                event.setCancelled(true);
                return;
            }

        }

    }


}
