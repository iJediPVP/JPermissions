package me.ijedi.jpermissions.inventories.Player;

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

public class PlayerClickEvent implements Listener {

    //Variables
    private JavaPlugin plugin;
    private String playerList, playerPerm, playerWorld;
    private List<String> blackList = new ArrayList<String>(){{
        add("EXIT");
        add("BACK");
        add("NEXT");
    }};

    //Constructor
    public PlayerClickEvent(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        playerList = new PlayerList(plugin).getName();
        playerPerm = new PlayerPerm(plugin).getName();
        playerWorld = new PlayerWorld(plugin).getName();
    }

    //Event
    @EventHandler
    public void playerClick(InventoryClickEvent event){

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

            //PLAYER LIST
            if(invName.equals(playerList)){

                //Check itemName
                if(itemName.equalsIgnoreCase("RETURN")){
                    //Back to MainInv
                    player.openInventory(new MainInv().getInventory());

                }else if(!blackList.contains(itemName.toUpperCase())){
                    //Open up PlayerWorld
                    player.openInventory(new PlayerWorld(plugin).getInventory(itemName));
                }

                event.setCancelled(true);
                return;
            }

            //PLAYER WORLD
            try{
                if(invName.substring(0, playerWorld.length()).equals(playerWorld)){

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Go back to PlayerList
                        player.openInventory(new PlayerList(plugin).getInventory());

                    }else if(!blackList.contains(itemName.toUpperCase())){
                        //Open PlayerPerm
                        String playerName = invName.substring(playerWorld.length());
                        player.openInventory(new PlayerPerm(plugin).getInventory(playerName, itemName));
                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing

            //PLAYER PERM
            try{
                if(invName.substring(0, playerPerm.length()).equals(playerPerm)){
                    String playerName = invName.substring(playerPerm.length());
                    List<String> lore = event.getCurrentItem().getItemMeta().getLore();

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Go back to PlayerWorld
                        player.openInventory(new PlayerWorld(plugin).getInventory(playerName));

                    }else if(itemName.equalsIgnoreCase("ADD")){
                        //Open PluginList
                        player.openInventory(new PluginList(plugin).getInventory(playerName, lore));

                    }else if(!blackList.contains(itemName.toUpperCase())){
                        //Check if player has this perm
                        Player pObject = Bukkit.getPlayer(playerName);
                        if(pObject != null){
                            if(pObject.hasPermission(itemName)){
                                //Open RemovePerm
                                lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Permission: " + ChatColor.GREEN + "" + ChatColor.ITALIC + itemName);
                                player.openInventory(new RemovePerm().getInventory(lore));

                            }else{ //Player no longer has this perm. Refresh inventory
                                player.sendMessage("This player does not have this permission..");
                                player.openInventory(new PlayerPerm(plugin).getInventory(playerName, ChatColor.stripColor(lore.get(lore.size() - 1).substring("World: ".length()))));
                            }

                        }else{
                            player.sendMessage("This player is not online..");
                        }

                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing


        }catch(NullPointerException npe){} //Do nothing..

    }
}
