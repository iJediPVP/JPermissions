package me.ijedi.jpermissions;

import me.ijedi.jpermissions.commands.JPermCommand;
import me.ijedi.jpermissions.inventories.Group.GroupClickEvent;
import me.ijedi.jpermissions.inventories.Other.MainInv;
import me.ijedi.jpermissions.inventories.Player.PlayerClickEvent;
import me.ijedi.jpermissions.inventories.Plugins.PluginClickEvent;
import me.ijedi.jpermissions.inventories.Other.MainClickEvent;
import me.ijedi.jpermissions.listeners.PJoin;
import me.ijedi.jpermissions.listeners.PQuit;
import me.ijedi.jpermissions.listeners.WorldChange;
import me.ijedi.jpermissions.menulib.MenuListener;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    //Variables
    private boolean enabled = false;
    private GroupManager groupManager = new GroupManager(this);

    //Enabled
    @Override
    public void onEnable(){
        saveDefaultConfig();
        enabled = getConfig().getBoolean("enabled");

        //Check if enabled.
        if(enabled){

            //Load groups and add all online players as Users
            groupManager.reloadPermissions();

            //Get command
            getCommand("JPERM").setExecutor(new JPermCommand(this));

            //Call events
            new PJoin(this);
            new PQuit(this);
            new WorldChange(this);
            new MenuListener(this);
            new MainClickEvent(this);
            new GroupClickEvent(this);
            new PluginClickEvent(this);
            new PlayerClickEvent(this);

            //Log
            getLogger().info("JPermissions enabled");

        }else{
            //Log
            getLogger().info("JPermissions NOT enabled");
        }
    }

    //Disabled
    @Override
    public void onDisable(){
        if(enabled){
            groupManager.resetUsers();
            //Log
            this.getLogger().info("JPermissions has been disabled.");
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toUpperCase();

        if(cmd.equals("TEST")){
            if(sender instanceof Player){
                ((Player) sender).openInventory(new MainInv().getInventory());
            }
        }

        return true;
    }
}
