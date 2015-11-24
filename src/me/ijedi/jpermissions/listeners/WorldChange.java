package me.ijedi.jpermissions.listeners;

import me.ijedi.jpermissions.permissions.GroupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldChange implements Listener {

    //Variables
    private JavaPlugin plugin;
    private GroupManager groupManager;
    private boolean enabled = false;

    //Constructor
    public WorldChange(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        groupManager = new GroupManager(plugin);
        enabled = plugin.getConfig().getBoolean("enabled");
    }

    //Event
    @EventHandler
    public void playerWorldChange(PlayerChangedWorldEvent event){
        if(enabled){
            groupManager.getUser(event.getPlayer().getUniqueId()).setPermissions(event.getPlayer().getWorld().getName());
        }
    }
}
