package me.ijedi.jpermissions.inventories.Group;

import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupListInv {

    //Variables
    private JavaPlugin plugin;
    private Menu menu;
    private MenuManager menuManager = new MenuManager();
    private final String name = "JPermissions Groups";

    //Constructor
    public GroupListInv(JavaPlugin plugin){
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

        //Get groups from GroupManager
        List<ItemStack> itemList = new ArrayList<>();
        GroupManager gm = new GroupManager(plugin);
        for(String groupName : gm.getGroupSet()){
            itemList.add(createItem(Material.PAPER, (short) 0, groupName, Collections.singletonList("Click to edit"), false));
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
