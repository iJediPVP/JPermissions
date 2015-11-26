package me.ijedi.jpermissions.inventories;

import me.ijedi.jpermissions.menulib.Menu;
import me.ijedi.jpermissions.menulib.MenuManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class CreateItem {

    //Create item
    public ItemStack makeItem(Material material, short id, String name, List<String> lore, boolean enchant){
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

    //Set items
    public void setButtons(Menu menu){
        ItemStack exit = makeItem(Material.BARRIER, (short) 0, "Exit", Collections.singletonList("Exit"), false);
        ItemStack back = makeItem(Material.ARROW, (short) 0, "Back", Collections.singletonList("Back"), false);
        ItemStack next = makeItem(Material.ARROW, (short) 0, "Next", Collections.singletonList("Next"), false);

        menu.setButtons(exit, back, next);
    }

    //Add return button
    public void addReturn(Menu menu){
        ItemStack returnItem = makeItem(Material.ARROW, (short) 0, "Return", Collections.singletonList("Return"), false);
        for(Inventory inventory : new MenuManager().getMenuPageList(menu.getName())){
            inventory.setItem(inventory.getSize() - 9, returnItem);
        }
    }
    public void addReturn(Menu menu, ItemStack item){
        for(Inventory inventory : new MenuManager().getMenuPageList(menu.getName())){
            inventory.setItem(inventory.getSize() - 9, item);
        }
    }
}
