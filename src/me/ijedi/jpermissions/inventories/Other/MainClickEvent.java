
package me.ijedi.jpermissions.inventories.Other;

import me.ijedi.jpermissions.inventories.Group.GroupList;
import me.ijedi.jpermissions.inventories.Group.GroupPerm;
import me.ijedi.jpermissions.inventories.Player.PlayerList;
import me.ijedi.jpermissions.inventories.Player.PlayerPerm;
import me.ijedi.jpermissions.inventories.Plugins.PluginPerm;
import me.ijedi.jpermissions.menulib.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MainClickEvent implements Listener {

    //Variables
    private JavaPlugin plugin;
    private MenuManager menuManager = new MenuManager();
    private String mainName, addPerm, removePerm;



    //Constructor
    public MainClickEvent(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        //Get inv names
        mainName = new MainInv().getName();
        addPerm = new AddPerm().getName();
        removePerm = new RemovePerm().getName();
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
                itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            }catch(NullPointerException npe){
                return;
            }

            //Check which inventory
            String invName = event.getInventory().getName();

            //MAIN - Options to reload, edit groups, or edit players.
            if(invName.equals(mainName)){

                //Check itemName
                if(itemName.equalsIgnoreCase("GROUPS")){
                    //Check perms
                    if(player.hasPermission("jp.group")){
                        //Open GroupList
                        player.openInventory(new GroupList(plugin).getInventory());
                    }else{
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                    }

                }else if(itemName.equalsIgnoreCase("PLAYERS")){
                    //Check perms
                    if(player.hasPermission("jp.player")){
                        //Open PlayerList
                        player.openInventory(new PlayerList(plugin).getInventory());
                    }else{
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                    }

                }else if(itemName.equalsIgnoreCase("RELOAD")){
                    //Check perms
                    if(player.hasPermission("jp.reload")){
                        //Reload perms
                        player.performCommand("jp reload");
                    }else{
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                    }

                }else if(itemName.equalsIgnoreCase("Exit")){
                    //Close
                    player.closeInventory();
                }

                event.setCancelled(true);
                return;
            }

            //ADD PERM
            if(invName.equals(addPerm)){
                //Get lore off confirm item
                List<String> lore = event.getInventory().getItem(12).getItemMeta().getLore();

                //Object info
                String[] objectArg = lore.get(1).split(": ");
                String objectType = ChatColor.stripColor(objectArg[0]); //Group or Player
                String objectName = ChatColor.stripColor(objectArg[1]); //Name of above

                //Perm
                String permission = ChatColor.stripColor(lore.get(lore.size() - 1).split(": ")[1]);

                //Check itemName
                if(itemName.equalsIgnoreCase("CONFIRM")){
                    //Check if Player or Group
                    if(objectType.equalsIgnoreCase("PLAYER")){
                        String worldName = ChatColor.stripColor(lore.get(lore.size() - 2).split(": ")[1]);

                        //Remove perm
                        player.performCommand(String.format("jp p %s p a %s %s", objectName, worldName, permission));

                    }else if(objectType.equalsIgnoreCase("GROUP")){
                        //Remove perm
                        player.performCommand(String.format("jp g %s p a %s", objectName, permission));
                    }
                }

                /*Go back to object's perm list
                if(objectType.equalsIgnoreCase("PLAYER")){
                    //PlayerPerm
                    String worldName = ChatColor.stripColor(lore.get(lore.size() - 2).split(": ")[1]);
                    player.openInventory(new PlayerPerm(plugin).getInventory(worldName, lore));

                }else if(objectType.equalsIgnoreCase("GROUP")){
                    //GroupPerm
                    player.openInventory(new GroupPerm(plugin).getInventory(objectName));

                }*/
                String pluginName = ChatColor.stripColor(lore.get(lore.size() - 1).split(": ")[1]);
                player.openInventory(new PluginPerm(plugin).getInventory(pluginName, lore));


                event.setCancelled(true);
                return;

            }

            //REMOVE PERM
            if(invName.equals(removePerm)){
                //Get lore off confirm item
                List<String> lore = event.getInventory().getItem(12).getItemMeta().getLore();

                //Object info
                String[] objectArg = lore.get(1).split(": ");
                String objectType = ChatColor.stripColor(objectArg[0]); //Group or Player
                String objectName = ChatColor.stripColor(objectArg[1]); //Name of above

                //Perm
                String permission = ChatColor.stripColor(lore.get(lore.size() - 1).split(": ")[1]);

                //Check itemName
                if(itemName.equalsIgnoreCase("CONFIRM")){
                    //Check if Player or Group
                    if(objectType.equalsIgnoreCase("PLAYER")){
                        String worldName = ChatColor.stripColor(lore.get(lore.size() - 2).split(": ")[1]);

                        //Remove perm
                        player.performCommand(String.format("jp p %s p r %s %s", objectName, worldName, permission));

                    }else if(objectType.equalsIgnoreCase("GROUP")){
                        //Remove perm
                        player.performCommand(String.format("jp g %s p r %s", objectName, permission));
                    }
                }

                //Go back to object's perm list
                if(objectType.equalsIgnoreCase("PLAYER")){
                    //PlayerPerm
                    String worldName = ChatColor.stripColor(lore.get(lore.size() - 2).split(": ")[1]);
                    player.openInventory(new PlayerPerm(plugin).getInventory(worldName, lore));

                }else if(objectType.equalsIgnoreCase("GROUP")){
                    //GroupPerm
                    player.openInventory(new GroupPerm(plugin).getInventory(objectName));

                }

                event.setCancelled(true);
                return;
            }

        }

    }


}
