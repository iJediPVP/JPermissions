/*
Add perm to group
Remove perm from player
Add perm to player
Add return buttons to most inventories
* */

package me.ijedi.jpermissions.inventories.Other;

import me.ijedi.jpermissions.inventories.Other.AddPerm;
import me.ijedi.jpermissions.inventories.Group.GroupList;
import me.ijedi.jpermissions.inventories.Group.GroupPerm;
import me.ijedi.jpermissions.inventories.Other.MainInv;
import me.ijedi.jpermissions.inventories.Player.PlayerList;
import me.ijedi.jpermissions.inventories.Player.PlayerPerm;
import me.ijedi.jpermissions.inventories.Plugins.PluginPerm;
import me.ijedi.jpermissions.inventories.Other.RemovePerm;
import me.ijedi.jpermissions.menulib.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
                    //Check for perms
                    if(player.hasPermission("jp.group")){
                        //Open group list inventory
                        player.openInventory(new GroupList(plugin).getInventory());

                    }else{
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                    }

                }else if(itemName.equals("PLAYERS")){
                    //Check for perms
                    if(player.hasPermission("jp.player")){
                        //Open player list inventory after 2 ticks
                        final Inventory inventory = new PlayerList(plugin).getInventory();
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                player.openInventory(inventory);
                            }
                        }.runTaskLater(plugin, 2l);

                    }else{
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                    }

                }else if(itemName.equals("EXIT")){
                    //Close inventory
                    player.closeInventory();

                }else if(itemName.equals("RELOAD")){
                    //Check for perms
                    if(player.hasPermission("jp.reload")){
                        //Reload perms
                        player.performCommand("jp reload");

                    }else{
                        player.setPlayerListName(ChatColor.RED + "You do not have permission to use this.");
                    }
                }

                event.setCancelled(true);
                return;
            }

            //ADD PERM
            if(invName.equals(addPerm)){
                List<String> lore = event.getCurrentItem().getItemMeta().getLore();
                ItemMeta meta = event.getInventory().getItem(12).getItemMeta();
                String pluginName = ChatColor.stripColor(meta.getLore().get(meta.getLore().size() - 2).substring("Plugin: ".length()));

                //Check itemName
                if(itemName.equalsIgnoreCase("CONFIRM")){

                    //Check if player or group
                    String[] args = lore.get(1).split(": ");
                    String perm = ChatColor.stripColor(lore.get(lore.size() - 1).substring("Permission: ".length()));
                    try{
                        perm = perm.split(" ")[1];
                    }catch(ArrayIndexOutOfBoundsException e){}

                    if(ChatColor.stripColor(args[0]).equalsIgnoreCase("Player")){
                        //Add perm to player
                        String playerName = ChatColor.stripColor(lore.get(1).substring("Player: ".length()));
                        try{
                            playerName = playerName.split(" ")[1];
                        }catch(ArrayIndexOutOfBoundsException e){}
                        String worldName = ChatColor.stripColor(lore.get(2).substring("World: ".length()));
                        player.performCommand(String.format("jp p %s p a %s %s", playerName, worldName, perm));

                    }else{
                        //Add perm to group
                        String groupName = ChatColor.stripColor(lore.get(1).substring("Group: ".length()));
                        try{
                            groupName = groupName.split(" ")[1];
                        }catch(ArrayIndexOutOfBoundsException e){}
                        Bukkit.broadcastMessage(groupName);
                        player.performCommand(String.format("jp g %s p a %s", groupName, perm));

                    }
                }
                //Trim down lore
                if(ChatColor.stripColor(lore.get(1).split(": ")[0]).equalsIgnoreCase("Player")){
                    while(lore.size() > 3){
                        lore.remove(lore.size() - 1);
                    }
                }else{
                    while(lore.size() > 2){
                        lore.remove(lore.size() - 1);
                    }
                }

                //Return to PluginPerm
                player.openInventory(new PluginPerm(plugin).getInventory(pluginName, lore));

                event.setCancelled(true);
                return;

            }

            //REMOVE PERM
            if(invName.equals(removePerm)){
                List<String> lore = event.getCurrentItem().getItemMeta().getLore();

                //Check itemName
                if(itemName.equalsIgnoreCase("CONFIRM")){
                    String[] args = lore.get(1).split(": ");
                    String perm = ChatColor.stripColor(lore.get(lore.size() - 1).substring("Permission: ".length()));
                    try{
                        perm = perm.split(" ")[1];
                    }catch(ArrayIndexOutOfBoundsException e){}

                    //Player
                    if(ChatColor.stripColor(args[0]).equalsIgnoreCase("Player")){
                        String playerName = ChatColor.stripColor(lore.get(1).substring("Player: ".length()));
                        try{
                            playerName = playerName.split(" ")[1];
                        }catch(ArrayIndexOutOfBoundsException e){}
                        String worldName = ChatColor.stripColor(lore.get(2).split(" ")[1]);
                        player.performCommand(String.format("jp p %s p r %s %s", playerName, worldName, perm));

                    }else{ //Group
                        String groupName = ChatColor.stripColor(lore.get(1).substring("Group: ".length()));
                        try{
                            groupName = groupName.split(" ")[1];
                        }catch(ArrayIndexOutOfBoundsException e){}
                        player.performCommand(String.format("jp g %s p r %s", groupName, perm));
                    }
                }

                //Trim down lore
                if(ChatColor.stripColor(lore.get(1).split(": ")[0]).equalsIgnoreCase("Player")){
                    while(lore.size() > 3){
                        lore.remove(lore.size() - 1);
                    }
                    String playerName = ChatColor.stripColor(lore.get(1).substring("Player: ".length()));
                    try{
                        playerName = playerName.split(" ")[1];
                    }catch(ArrayIndexOutOfBoundsException e){}
                    String worldName = ChatColor.stripColor(lore.get(2).split(" ")[1]);
                    player.openInventory(new PlayerPerm(plugin).getInventory(playerName, worldName));
                }else{
                    while(lore.size() > 2){
                        lore.remove(lore.size() - 1);
                    }
                    String groupName = ChatColor.stripColor(lore.get(1).substring("Group: ".length()));
                    try{
                        groupName = groupName.split(" ")[1];
                    }catch(ArrayIndexOutOfBoundsException e){}
                    player.openInventory(new GroupPerm(plugin).getInventory(groupName));
                }

                event.setCancelled(true);
                return;
            }

        }

    }


}
