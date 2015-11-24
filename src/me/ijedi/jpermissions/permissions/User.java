package me.ijedi.jpermissions.permissions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class User {

    //Variables
    private Player player;
    private HashMap<String, List<String>> worldPermList = new HashMap<>(); //Store perms for all worlds
    private HashMap<String, List<String>> worldGroupList = new HashMap<>(); //Store groups for all worlds

    private PermissionAttachment permAttachment, groupAttachment;
    private GroupManager groupManager;
    private JavaPlugin plugin;
    private char negativeChar;
    private File file;
    private FileConfiguration config;

    private String prefix = "";
    private String suffix = "";

    //Constructor
    public User(Player player, JavaPlugin plugin){
        this.player = player;
        this.plugin = plugin;
        permAttachment = player.addAttachment(plugin);
        groupAttachment = player.addAttachment(plugin);
        groupManager = new GroupManager(plugin);
        file = new File(plugin.getDataFolder() + "/playerData/" + player.getUniqueId() + ".yml");
        config = YamlConfiguration.loadConfiguration(file);

        //Get permissions
        try{
            for(String world : config.getConfigurationSection("permissions").getKeys(false)){
                for(String perm : config.getStringList("permissions." + world)){
                    world = world.toLowerCase();
                    perm = perm.toLowerCase();
                    try{
                        worldPermList.get(world).add(perm);
                    }catch(NullPointerException e){ //World not in map
                        List<String> tempList = new ArrayList<>();
                        tempList.add(perm);
                        worldPermList.put(world, tempList);
                    }
                }
            }
        }catch(NullPointerException npe){ //permissions key does not exist
            for(World world : Bukkit.getWorlds()){
                config.set("permissions." + world.getName(), new ArrayList<>());
            }
        }

        //Get groups
        try{
            for(String world : config.getConfigurationSection("groups").getKeys(false)){
                List<String> groupList = config.getStringList("groups." + world);
                if(groupList.size() == 0){
                    world = world.toLowerCase();
                    try{
                        worldGroupList.get(world).add("default");
                    }catch(NullPointerException e){ //World not in map
                        List<String> tempList = new ArrayList<>();
                        tempList.add("default");
                        worldGroupList.put(world, tempList);
                    }
                    continue;
                }else{
                    for(String group : groupList){
                        if(groupManager.hasGroup(group)){
                            world = world.toLowerCase();
                            group = group.toLowerCase();
                            try{
                                worldGroupList.get(world).add(group);
                            }catch(NullPointerException e){ //World not in map
                                List<String> tempList = new ArrayList<>();
                                tempList.add(group);
                                worldGroupList.put(world, tempList);
                            }
                        }
                    }
                }

            }
        }catch(NullPointerException npe){ //groups key does not exist
            config.set("groups", new ArrayList<>());
        }
        saveUser();

        //Remove negative perms from player
        try{
            negativeChar = plugin.getConfig().getString("negativeChar").charAt(0);
        }catch(NullPointerException npe){
            negativeChar = '^';
        }
        for(String world : worldPermList.keySet()){
            for(String perm : worldPermList.get(world)){
                if(negativeChar == perm.charAt(0)){

                }
            }
        }

    }

    //Save
    private void save(){
        try{
            config.save(file);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    public void saveUser(){
        for(String world : worldPermList.keySet()){
            config.set("permissions." + world, worldPermList.get(world));
        }
        for(String world : worldGroupList.keySet()){
            config.set("groups." + world, worldGroupList.get(world));
        }
        save();
    }

    //Other
    public UUID getUUID(){
        return player.getUniqueId();
    }
    public void reset(){
        Set<String> permSet = permAttachment.getPermissions().keySet();
        for(String perm : permSet){
            permAttachment.unsetPermission(perm);
        }
        permSet = groupAttachment.getPermissions().keySet();
        for(String perm : permSet){
            groupAttachment.unsetPermission(perm);
        }
    }
    public String getPrefix(){
        int rank = 0;
        String world = player.getWorld().getName().toLowerCase();
        if(worldGroupList.containsKey(world)){
            for(String group : worldGroupList.get(world)){
                if(groupManager.hasGroup(group)){
                    Group g = groupManager.getGroup(group);
                    if(g.getRank() > rank){
                        rank = g.getRank();
                        prefix = g.getPrefix();
                    }
                }
            }
        }
        return prefix;
    }
    public String getSuffix(){
        int rank = 0;
        String world = player.getWorld().getName().toLowerCase();
        if(worldGroupList.containsKey(world)){
            for(String group : worldGroupList.get(world)){
                if(groupManager.hasGroup(group)){
                    Group g = groupManager.getGroup(group);
                    if(g.getRank() > rank){
                        rank = g.getRank();
                        suffix = g.getSuffix();
                    }
                }
            }
        }
        return suffix;
    }

    //Permissions
    public boolean hasPermission(String worldName, String permission){
        worldName = worldName.toLowerCase();
        permission = permission.toLowerCase();

        if(worldPermList.containsKey(worldName)){
            if(worldPermList.get(worldName).contains(permission)){
                return true;
            }
        }
        return false;
    }
    public void addPermission(String worldName, String permission){
        worldName = worldName.toLowerCase();
        permission = permission.toLowerCase();
        if(!hasPermission(worldName, permission)){
            try{
                worldPermList.get(worldName).add(permission);
            }catch(NullPointerException npe){
                List<String> tempList = new ArrayList<>();
                tempList.add(permission);
                worldPermList.put(worldName, tempList);
            }
            saveUser();
        }
    }
    public void removePermission(String worldName, String permission){
        worldName = worldName.toLowerCase();
        permission = permission.toLowerCase();
        if(hasPermission(worldName, permission)){
            worldPermList.get(worldName).remove(permission);
            saveUser();
        }
    }
    public void setPermissions(String worldName){
        //Clear current
        reset();

        worldName = worldName.toLowerCase();

        //Add new
        if(worldPermList.containsKey(worldName)){
            for(String perm : worldPermList.get(worldName)){
                permAttachment.setPermission(perm, true);
            }
        }
        if(worldGroupList.containsKey(worldName)){
            for(String group : worldGroupList.get(worldName)){
                if(groupManager.hasGroup(group)){
                    Group g = groupManager.getGroup(group);
                    for(String perm : g.getPermissions()){
                        groupAttachment.setPermission(perm, true);
                    }
                    for(String child : g.getChildren()){
                        if(groupManager.hasGroup(child)){
                            Group c = groupManager.getGroup(child);
                            for(String perm : c.getPermissions()){
                                groupAttachment.setPermission(perm, true);
                            }
                        }
                    }
                }
            }
        }

        for(String perm : permAttachment.getPermissions().keySet()){
            player.sendMessage(perm);
        }
    }

    //Groups
    public boolean hasGroup(String worldName, String group){
        worldName = worldName.toLowerCase();
        group = group.toLowerCase();
        if(worldGroupList.containsKey(worldName)){
            if(worldGroupList.get(worldName).contains(group)){
                return true;
            }
        }
        return false;
    }
    public void addGroup(String worldName, String group){
        worldName = worldName.toLowerCase();
        group = group.toLowerCase();
        if(!hasGroup(worldName, group)){
            try{
                worldGroupList.get(worldName).add(group);
            }catch(NullPointerException npe){
                List<String> tempList = new ArrayList<>();
                tempList.add(group);
                worldGroupList.put(worldName, tempList);
            }
            saveUser();
        }
    }
    public void removeGroup(String worldName, String group){
        worldName = worldName.toLowerCase();
        group = group.toLowerCase();
        if(hasGroup(worldName, group)){
            worldGroupList.get(worldName).remove(group);
            saveUser();
        }
    }

}
