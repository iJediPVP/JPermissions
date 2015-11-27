package me.ijedi.jpermissions.inventories.Player;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import me.ijedi.jpermissions.permissions.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerPerm {

    //Variables
    private JavaPlugin plugin;
    private String name = "Permissions: ";


    //Constructor
    public PlayerPerm(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(String playerName, String worldName){

        //Make sure player is online
        Player player = Bukkit.getPlayer(playerName);
        if(player != null){

            //Create menu
            name = name + player.getName();
            Menu menu = new Menu(name);
            MenuManager menuManager = new MenuManager();
            CreateItem ci = new CreateItem();

            //Get user
            GroupManager gm = new GroupManager(plugin);
            User user;
            if(gm.hasUser(player.getUniqueId())){
                user = gm.getUser(player.getUniqueId());
            }else{
                user = new User(player, plugin);
                gm.addUser(user);
            }

            //Try to get user perms for this world
            try{
                List<ItemStack> itemList = new ArrayList<>();
                for(String perm : user.getPermissions(worldName)){
                    itemList.add(ci.makeItem(Material.PAPER, (short) 0,
                            ChatColor.GREEN + "" + ChatColor.BOLD + perm,
                            Arrays.asList(
                                    ChatColor.GOLD + "" + ChatColor.ITALIC + "Click to remove this permission..",
                                    ChatColor.GOLD + "" + ChatColor.ITALIC + "Player: " + ChatColor.GREEN + "" + ChatColor.ITALIC + player.getName(),
                                    ChatColor.GOLD + "" + ChatColor.ITALIC + "World: " + ChatColor.GREEN + "" + ChatColor.ITALIC + worldName),
                            false));
                }
                menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

            }catch(NullPointerException npe){ //User doesn't have perms for this world..
                ItemStack[] item = {new ItemStack(Material.AIR)};
                menu.setContents(item);
            }

            //Set buttons
            ci.setButtons(menu);
            ci.addReturn(menu);

            ItemStack add = ci.makeItem(Material.INK_SACK, (short) 10,
                    ChatColor.GREEN + "" + ChatColor.BOLD + "Add",
                    Arrays.asList(
                            ChatColor.GOLD + "" + ChatColor.ITALIC + "Add permissions..",
                            ChatColor.GOLD + "" + ChatColor.ITALIC + "Player: " + ChatColor.GREEN + "" + ChatColor.ITALIC + player.getName(),
                            ChatColor.GOLD + "" + ChatColor.ITALIC + "World: " + ChatColor.GREEN + "" + ChatColor.ITALIC + worldName),
                    false);
            for(Inventory inventory : menuManager.getMenuPageList(name)){
                inventory.setItem(inventory.getSize() - 1, add);
            }

            return menuManager.getMenu(name);
        }
        throw new NullPointerException("Player not found.");

    }

    //Get name
    public String getName() {
        return name;
    }
}
