package me.ijedi.jpermissions.listeners;

import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PQuit implements Listener {

    //Variables
    private JavaPlugin plugin;
    private GroupManager groupManager;
    private boolean enabled = false;

    //Constructor
    public PQuit(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        groupManager = new GroupManager(plugin);
        enabled = plugin.getConfig().getBoolean("enabled");
    }

    //Event
    @EventHandler
    public void playerQuit(PlayerQuitEvent event){
        if(enabled){
            groupManager.removeUser(event.getPlayer().getUniqueId());
        }
    }
}
