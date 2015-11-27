package me.ijedi.jpermissions.inventories.Plugins;

import me.ijedi.jpermissions.inventories.Other.AddPerm;
import me.ijedi.jpermissions.inventories.Group.GroupPerm;
import me.ijedi.jpermissions.inventories.Player.PlayerPerm;
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
            try{
                if(invName.substring(0, pluginList.length()).equals(pluginList)){
                    List<String> lore = event.getCurrentItem().getItemMeta().getLore();

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Go back to object's perm list (GroupPerm or PlayerPerm)
                        String[] args = lore.get(1).split(": ");

                        //Player
                        if(ChatColor.stripColor(args[0]).equalsIgnoreCase("Player")){
                            String playerName = ChatColor.stripColor(args[1]);
                            String worldName = ChatColor.stripColor(lore.get(2).split(" ")[1]);
                            player.openInventory(new PlayerPerm(plugin).getInventory(playerName, worldName));

                        }else{ //Group
                            player.openInventory(new GroupPerm(plugin).getInventory(ChatColor.stripColor(args[1])));
                        }

                    }else if(!blackList.contains(itemName.toUpperCase())){
                        //Open PluginPerm
                        player.openInventory(new PluginPerm(plugin).getInventory(itemName, lore));
                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing

            //PLUGIN PERM
            try{
                if(invName.substring(0, pluginPerm.length()).equals(pluginPerm)){
                    List<String> lore = event.getCurrentItem().getItemMeta().getLore();

                    //Check itemName
                    if(itemName.equalsIgnoreCase("RETURN")){
                        //Go back to PermList
                        String objectName = ChatColor.stripColor(lore.get(1).split(": ")[1]);
                        player.openInventory(new PluginList(plugin).getInventory(objectName, lore));

                    }else if(!blackList.contains(itemName.toUpperCase())){
                        //Open AddPerm
                        lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Permission: " + ChatColor.GREEN + "" + ChatColor.ITALIC + itemName);
                        player.openInventory(new AddPerm().getInventory(lore));
                    }

                    event.setCancelled(true);
                    return;
                }
            }catch(NullPointerException | StringIndexOutOfBoundsException e){} //Do nothing






        }catch(NullPointerException npe){} //No inv name

    }

    private boolean isPlayer(String name){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    private void doReturn(Player whoClicked, List<String> lore){

    }

}
