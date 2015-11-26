/*
Add perm to group
Remove perm from player
Add perm to player
Add return buttons to most inventories
* */

package me.ijedi.jpermissions.listeners;

import me.ijedi.jpermissions.inventories.Group.GroupRemovePerm;
import me.ijedi.jpermissions.inventories.Group.GroupPerm;
import me.ijedi.jpermissions.inventories.Group.GroupList;
import me.ijedi.jpermissions.inventories.MainInv;
import me.ijedi.jpermissions.inventories.PlayerListInv;
import me.ijedi.jpermissions.inventories.Plugins.PluginList;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
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
    private String mainName, groupListName, groupPermName, groupRemovePermName,
            pluginListName, playerListName;



    //Constructor
    public InvClick(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        //Get inv names
        mainName = new MainInv().getName();

        groupListName = new GroupList(plugin).getName();
        groupPermName = new GroupPerm(plugin, "").getName();
        groupRemovePermName = new GroupRemovePerm(plugin, "").getName();

        playerListName = new PluginList(plugin).getName();

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
                    player.openInventory(new GroupList(plugin).getInventory());

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



        }

    }


}
