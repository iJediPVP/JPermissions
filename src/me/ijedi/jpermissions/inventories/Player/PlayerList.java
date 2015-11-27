package me.ijedi.jpermissions.inventories.Player;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlayerList {

    //Variables
    private JavaPlugin plugin;
    private Menu menu;
    private MenuManager menuManager = new MenuManager();
    private final String name = "Player List";

    //Constructor
    public PlayerList(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Get inventory
    public Inventory getInventory(){
        menu = new Menu(name);

        //Get all online players
        List<ItemStack> itemList = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            ItemStack iStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) iStack.getItemMeta();
            skullMeta.setOwner(player.getName());
            skullMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + player.getName());
            skullMeta.setLore(Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Edit this player's permissions"));
            iStack.setItemMeta(skullMeta);
            itemList.add(iStack);
        }

        menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

        //Set buttons
        CreateItem ci = new CreateItem();
        ci.setButtons(menu);
        ci.addReturn(menu);

        return menuManager.getMenu(name);
    }

    //Get name
    public String getName(){
        return name;
    }

}
