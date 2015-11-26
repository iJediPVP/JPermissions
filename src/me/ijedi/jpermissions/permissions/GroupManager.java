package me.ijedi.jpermissions.permissions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GroupManager {

    //Variables
    private JavaPlugin plugin;
    private File groupFile;
    private FileConfiguration groupConfig;
    private static HashMap<String, Group> groupMap = new HashMap<>();
    private static HashMap<UUID, User> userMap = new HashMap<>();

    //Constructor
    public GroupManager(JavaPlugin plugin){
        this.plugin = plugin;
        groupFile = new File(plugin.getDataFolder() + "/groups.yml");
        if(!groupFile.exists()){
            plugin.saveResource("groups.yml", false);
        }
        groupConfig = YamlConfiguration.loadConfiguration(groupFile);
    }
    public void save(){
        try{
            groupConfig.save(groupFile);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    public void reloadPermissions(){
        loadGroups();

        Collection<User> userList = userMap.values();
        for(User user : userList){
            user.saveUser();
            user.reset();
        }
        userMap.clear();

        for(Player player : Bukkit.getOnlinePlayers()){
            User user = new User(player, plugin);
            addUser(user);
            user.setPermissions(player.getWorld().getName());
        }
    }

    //Groups
    public void loadGroups(){
        //Clear out map & add new groups
        groupMap.clear();
        for(String group : groupConfig.getConfigurationSection("").getKeys(false)){
            addGroup(new Group(plugin, group));
        }

        //If group map is 0 save the default groups.yml
        if(groupMap.size() == 0){
            plugin.saveResource("groups.yml", true);
            groupFile = new File(plugin.getDataFolder() + "/groups.yml");
            groupConfig = YamlConfiguration.loadConfiguration(groupFile);
            loadGroups();
        }

    }
    public boolean hasGroup(String groupName){
        groupName = groupName.toLowerCase();
        return groupMap.containsKey(groupName);
    }
    public void addGroup(Group group){
        String name = group.getName().toLowerCase();
        if(!hasGroup(name)){
            groupMap.put(name, group);
        }
    }
    public void removeGroup(Group group){
        String name = group.getName();
        if(hasGroup(name)){
            groupMap.remove(name);
            groupConfig.set(name, null);
            save();
            List<World> worldList = Bukkit.getWorlds();
            for(User user : userMap.values()){
                for(World world : worldList){
                    if(user.hasGroup(world.getName(), group.getName())){
                        user.removeGroup(world.getName(), group.getName());
                    }
                }
            }
        }
    }
    public Group getGroup(String groupName){
        groupName = groupName.toLowerCase();
        if(hasGroup(groupName)){
            return groupMap.get(groupName);
        }
        throw new NullPointerException("Group not found.");
    }
    public void createGroup(String groupName){
        if(!hasGroup(groupName)){
            groupFile = new File(plugin.getDataFolder() + "/groups.yml");
            groupConfig = YamlConfiguration.loadConfiguration(groupFile);
            groupConfig.set(groupName + ".permissions", new ArrayList<>());
            groupConfig.set(groupName + ".children", new ArrayList<>());
            groupConfig.set(groupName + ".prefix", "");
            groupConfig.set(groupName + ".suffix", "");
            groupConfig.set(groupName + ".rank", 0);
            save();
            Group group = new Group(plugin, groupName);
            addGroup(group);
        }
    }
    public Set<String> getGroupSet(){
        return groupMap.keySet();
    }

    //Users
    public boolean hasUser(UUID uuid){
        return userMap.containsKey(uuid);
    }
    public void addUser(User user){
        if(!hasUser(user.getUUID())){
            userMap.put(user.getUUID(), user);
        }

    }
    public void removeUser(UUID uuid){
        if(hasUser(uuid)){
            User user = getUser(uuid);
            user.reset();
            userMap.remove(uuid);
        }
    }
    public User getUser(UUID uuid){
        if(hasUser(uuid)){
            return userMap.get(uuid);
        }
        throw new NullPointerException("User not found");
    }
    public void resetUsers(){
        for(User user : userMap.values()){
            user.reset();
        }
    }

}
