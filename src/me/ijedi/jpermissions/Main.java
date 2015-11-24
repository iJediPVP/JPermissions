package me.ijedi.jpermissions;

import me.ijedi.jpermissions.commands.JPermCommand;
import me.ijedi.jpermissions.listeners.PJoin;
import me.ijedi.jpermissions.listeners.PQuit;
import me.ijedi.jpermissions.listeners.WorldChange;
import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    //Variables
    private boolean enabled = false;
    private GroupManager groupManager;
    private List<String> todoList = new ArrayList<String>(){{
        add("TODO LIST");
        add("Add suffix/prefix/rank command for groups.");
        add("Comments in config.yml");
        add("Integrate into core plugin");
        add("REMOVE THIS");
    }};

    //Enabled
    @Override
    public void onEnable(){
        saveDefaultConfig();
        enabled = getConfig().getBoolean("enabled");

        //Check if enabled.
        if(enabled){

            //Load groups and add all online players as Users
            groupManager = new GroupManager(this);
            groupManager.reloadPermissions();

            //Get command
            getCommand("JPERM").setExecutor(new JPermCommand(this));

            //Call events
            new PJoin(this);
            new PQuit(this);
            new WorldChange(this);

            //Log
            getLogger().info("JPermissions enabled");

            //REMOVE THIS
            for(String string : todoList){
                this.getLogger().info(string);
            }
        }else{
            //Log
            getLogger().info("JPermissions NOT enabled");
        }
    }

    //Disabled
    @Override
    public void onDisable(){
        if(enabled){
            //Log
            this.getLogger().info("JPermissions has been disabled.");
        }
    }

}
