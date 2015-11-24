package me.ijedi.jpermissions.permissions;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Group {

    //Variables
    private JavaPlugin plugin;
    private File file;
    private FileConfiguration config;
    private GroupManager groupManager;
    private String name;
    private List<String> permList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private String prefix = "";
    private String suffix = "";
    private int rank = 0;

    //Constructor
    public Group(JavaPlugin plugin, String name){
        this.plugin = plugin;
        this.name = name;
        groupManager = new GroupManager(plugin);
        file = new File(plugin.getDataFolder() + "/groups.yml");
        if(!file.exists()){
            plugin.saveResource("groups.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);

        //Try to get permissions and child groups
        try{
            for(String perm : config.getStringList(name + ".permissions")){
                perm = perm.toLowerCase();
                permList.add(perm);
            }
        }catch(NullPointerException npe){ //permissions key doesn't exist
            config.set(name + ".permissions", new ArrayList<>());
        }
        try{
            childList = config.getStringList(name + ".children");
            for(String child : config.getStringList(name + ".children")){
                childList.add(child.toLowerCase());
            }
        }catch(NullPointerException npe){ //Children key does not exist
            config.set(name + ".children", new ArrayList<>());
        }

        //Try and get prefix & suffix
        try{
            String str = config.getString(name + ".prefix");
            if(!str.isEmpty()){
                prefix = str;
            }
        }catch(NullPointerException npe){
            config.set(name + ".prefix", name);
            prefix = name;
        }
        try{
            String str = config.getString(name + ".suffix");
            if(!str.isEmpty()){
                suffix = str;
            }
        }catch(NullPointerException npe){
            config.set(name + ".suffix", name);
            suffix = name;
        }

        //Try to get rank
        try{
            rank = config.getInt(name + ".rank");
        }catch(NullPointerException | NumberFormatException e){
            config.set(name + ".rank", 0);
            rank = 0;
        }
        save();
    }

    //Other methods
    private void save(){
        try{
            config.save(file);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    public void saveGroup(){
        file = new File(plugin.getDataFolder() + "/groups.yml");
        config = YamlConfiguration.loadConfiguration(file);
        config.set(name + ".permissions", permList);
        config.set(name + ".children", childList);
        config.set(name + ".prefix", prefix);
        config.set(name + ".suffix", suffix);
        config.set(name + ".rank", rank);
        save();
    }
    public String getName(){
        return name;
    }

    //Prefix & Suffix
    public String getPrefix(){
        return prefix;
    }
    public void setPrefix(String prefix){
        this.prefix = prefix;
        saveGroup();
    }

    public String getSuffix(){
        return suffix;
    }
    public void setSuffix(String suffix){
        this.suffix = suffix;
        saveGroup();
    }

    public int getRank(){
        return rank;
    }
    public void setRank(int rank){
        this.rank = rank;
        saveGroup();
    }

    //Permissions
    public boolean hasPermission(String permission){
        permission = permission.toLowerCase();
        return permList.contains(permission);
    }
    public void addPermission(String permission){
        permission = permission.toLowerCase();
        if(!hasPermission(permission)){
            permList.add(permission);
            saveGroup();
        }
    }
    public void removePermission(String permission){
        permission = permission.toLowerCase();
        if(hasPermission(permission)){
            permList.remove(permission);
            saveGroup();
        }
    }
    public List<String> getPermissions(){
        return permList;
    }

    //Children
    public boolean hasChild(String childName){
        childName = childName.toLowerCase();
        return childList.contains(childName);
    }
    public void addChild(String childName){
        childName = childName.toLowerCase();
        if(!hasChild(childName)){
            childList.add(childName);
            saveGroup();
        }
    }
    public void removeChild(String childName){
        childName = childName.toLowerCase();
        if(hasChild(childName)){
            childList.remove(childName);
            saveGroup();
        }
    }
    public List<String> getChildren(){
        return childList;
    }

}
