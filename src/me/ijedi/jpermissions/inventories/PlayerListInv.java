package me.ijedi.jpermissions.inventories;

import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Bukkit;
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

public class PlayerListInv {

    //Variables
    private JavaPlugin plugin;
    private Menu menu;
    private MenuManager menuManager = new MenuManager();
    private final String name = "JPermissions Players";

    //Constructor
    public PlayerListInv(JavaPlugin plugin){
        this.plugin = plugin;
    }

    //Create item
    private ItemStack createItem(Material material, short id, String name, List<String> lore, boolean enchant){
        ItemStack iStack = new ItemStack(material, 1, id);
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(name);
        iMeta.setLore(lore);
        if(enchant){
            iMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            iMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        iStack.setItemMeta(iMeta);
        return iStack;
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
            skullMeta.setDisplayName(player.getName());
            skullMeta.setLore(Collections.singletonList("Edit this player"));
            iStack.setItemMeta(skullMeta);
            itemList.add(iStack);
        }

        menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

        //Set buttons
        ItemStack exit = createItem(Material.BARRIER, (short) 0, "Exit", Collections.singletonList("Exit"), false);
        ItemStack back = createItem(Material.ARROW, (short) 0, "Back", Collections.singletonList("Back"), false);
        ItemStack next = createItem(Material.ARROW, (short) 0, "Next", Collections.singletonList("Next"), false);

        menu.setButtons(exit, back, next);

        return menuManager.getMenu(name);
    }

    //Get name
    public String getName(){
        return name;
    }

}
