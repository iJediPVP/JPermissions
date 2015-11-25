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

public class GroupPermInv {

    //Variables
    private JavaPlugin plugin;
    private Menu menu;
    private MenuManager menuManager = new MenuManager();
    private String invName, groupName;

    //Constructor
    public GroupPermInv(JavaPlugin plugin, String name){
        this.plugin = plugin;
        invName = "JPerms Group: " + name;
        groupName = name;
    }

    //Create item
    public ItemStack createItem(Material material, short id, String name, List<String> lore, boolean enchant){
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
        menu = new Menu(invName);

        //Get selected groups perms
        List<ItemStack> itemList = new ArrayList<>();
        for(String perm : new GroupManager(plugin).getGroup(groupName).getPermissions()){
            itemList.add(createItem(Material.INK_SACK, (short) 8, perm, Collections.singletonList("Click to remove."), false));
        }
        menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));

        //Set buttons
        ItemStack exit = createItem(Material.BARRIER, (short) 0, "Exit", Collections.singletonList("Exit"), false);
        ItemStack back = createItem(Material.ARROW, (short) 0, "Back", Collections.singletonList("Back"), false);
        ItemStack next = createItem(Material.ARROW, (short) 0, "Next", Collections.singletonList("Next"), false);

        menu.setButtons(exit, back, next);

        //Create and set return & add buttons for all menu pages
        ItemStack returnItem = createItem(Material.ARROW, (short) 0, "Return", Collections.singletonList("Return"), false);
        ItemStack add = createItem(Material.INK_SACK, (short) 10, "Add", Collections.singletonList("Add permission"), false);
        for(Inventory inventory : menuManager.getMenuPageList(invName)){
            inventory.setItem(inventory.getSize() - 9, returnItem);
            inventory.setItem(inventory.getSize() - 1, add);
        }

        return menuManager.getMenu(invName);
    }

    //Get names
    public String getGroupName(){
        return groupName;
    }
    public String getName(){
        return invName;
    }


}
