package me.ijedi.jpermissions.listeners;

import me.ijedi.jpermissions.permissions.GroupManager;
import me.ijedi.jpermissions.permissions.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PJoin implements Listener {

    //Variables
    private JavaPlugin plugin;
    private GroupManager groupManager;
    private boolean enabled = false;

    //Constructor
    public PJoin(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        groupManager = new GroupManager(plugin);
        enabled = plugin.getConfig().getBoolean("enabled");
    }

    //Event
    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        //Check if this plugin is enabled
        if(enabled){
            User user = new User(event.getPlayer(), plugin);
            user.setPermissions(event.getPlayer().getWorld().getName());
            groupManager.addUser(user);
        }
    }
}
