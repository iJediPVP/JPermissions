package me.ijedi.jpermissions.inventories.Group;

import me.ijedi.jpermissions.inventories.CreateItem;
import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupPerm {

    //Variables
    private JavaPlugin plugin;
    private static Menu menu;
    private MenuManager menuManager = new MenuManager();
    private String invName, groupName;

    //Constructor
    public GroupPerm(JavaPlugin plugin, String groupName){
        this.plugin = plugin;
        invName = "Group Permissions: " + groupName;
        this.groupName = groupName;

        menu = new Menu(invName);
    }

    //Get inventory
    public Inventory getInventory(){
        CreateItem ci = new CreateItem();

        //Get selected groups perms
        List<ItemStack> itemList = new ArrayList<>();
        for(String perm : new GroupManager(plugin).getGroup(groupName).getPermissions()){
            itemList.add(ci.makeItem(Material.INK_SACK, (short) 8, perm, Collections.singletonList("Click to remove."), false));
        }

        try{
            menu.setContents(itemList.toArray(new ItemStack[itemList.size()]));
        }catch(NullPointerException npe){ //No perms
            ItemStack[] item = {new ItemStack(Material.AIR)};
            menu.setContents(item);
        }

        ci.setButtons(menu);

        //Create and set return & add buttons for all menu pages
        ci.addReturn(menu);
        ItemStack add = ci.makeItem(Material.INK_SACK, (short) 10, "Add", Collections.singletonList("Add permission"), false);
        for(Inventory inventory : menuManager.getMenuPageList(invName)){
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
