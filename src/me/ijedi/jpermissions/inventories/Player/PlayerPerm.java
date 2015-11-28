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
    private String name = "Player Permissions";


    //Constructor
    public PlayerPerm(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(final String worldName, List<String> itemLore){

        final String playerName = ChatColor.stripColor(itemLore.get(1).split(": ")[1]);

        List<String> newLore = new ArrayList<String>(){{
            add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit player permissions in this world..");
            add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Player: " + ChatColor.GREEN + "" + ChatColor.ITALIC + playerName);
            add(ChatColor.GOLD + "" + ChatColor.ITALIC + "World: " + ChatColor.GREEN + "" + ChatColor.ITALIC + worldName);
        }};

        //Make sure player is online
        Player player = Bukkit.getPlayer(playerName);
        if(player != null){

            //Create menu
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
                    itemList.add(ci.makeItem(Material.INK_SACK, (short) 10,
                            ChatColor.GREEN + "" + ChatColor.BOLD + perm, newLore, false));
                }
                menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

            }catch(NullPointerException npe){ //User doesn't have perms for this world..
                ItemStack[] item = {new ItemStack(Material.AIR)};
                menu.setContents(item);
            }

            //Set buttons
            ci.setButtons(menu);

            //Custom add and return buttons
            newLore.set(0, ChatColor.GOLD + "" + ChatColor.ITALIC + "Return to the previous menu");
            ItemStack returnItem = ci.makeItem(Material.ARROW, (short) 0, ChatColor.RED + "" + ChatColor.BOLD + "Return", newLore, false);
            ci.addReturn(menu, returnItem);

            newLore.set(0, ChatColor.GOLD + "" + ChatColor.ITALIC + "Add player permissions..");
            ItemStack add = ci.makeItem(Material.INK_SACK, (short) 10, ChatColor.GREEN + "" + ChatColor.BOLD + "Add", newLore, false);
            ci.addAdd(menu, add);

            return menuManager.getMenu(name);
        }
        throw new NullPointerException("Player not found.");

    }

    //Get name
    public String getName() {
        return name;
    }
}
