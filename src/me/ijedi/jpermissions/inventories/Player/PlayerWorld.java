package me.ijedi.jpermissions.inventories.Player;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerWorld {

    //Variablse
    private JavaPlugin plugin;
    private String name = "World List: ";

    //Constructor
    public PlayerWorld(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(String playerName){
        name = name + playerName;
        Menu menu = new Menu(name);
        CreateItem ci = new CreateItem();

        //Try and get world list
        try{
            List<ItemStack> worldItems = new ArrayList<>();
            for(World world : Bukkit.getWorlds()){
                Material mat = Material.GLASS;
                String name = world.getName();
                if(world.getEnvironment().equals(World.Environment.NORMAL)){
                    mat = Material.GRASS;
                    name = ChatColor.GREEN + "" + ChatColor.BOLD +  name;
                }else if(world.getEnvironment().equals(World.Environment.NETHER)){
                    mat = Material.NETHERRACK;
                    name = ChatColor.RED + "" + ChatColor.BOLD + name;
                }else if(world.getEnvironment().equals(World.Environment.THE_END)){
                    mat = Material.ENDER_STONE;
                    name = ChatColor.YELLOW + "" + ChatColor.BOLD +  name;
                }

                worldItems.add(ci.makeItem(mat, (short) 0, name, Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit player permissions in this world.."), false));
            }
            menu.setContents(worldItems.toArray(new ItemStack[worldItems.size()]));

        }catch(NullPointerException npe){ //No worlds on the server.. o.O
            ItemStack[] item = {new ItemStack(Material.AIR)};
            menu.setContents(item);
        }

        //Set buttons
        ci.setButtons(menu);
        ci.addReturn(menu);

        return new MenuManager().getMenu(name);
    }

    //Get name
    public String getName(){
        return name;
    }
}
