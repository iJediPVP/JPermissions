package me.ijedi.jpermissions.commands;

import me.ijedi.jpermissions.permissions.Group;
import me.ijedi.jpermissions.permissions.GroupManager;
import me.ijedi.jpermissions.permissions.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class JPermCommand implements CommandExecutor{

    //Variables
    private JavaPlugin plugin;
    private GroupManager groupManager;
    private final String CHATPREFIX = ChatColor.AQUA + "" + ChatColor.BOLD + "[JPerms] ";
    private char colorChar;
    private final List<String> HELPLIST = new ArrayList<String>(){{
        add(ChatColor.GOLD + "" + ChatColor.BOLD + "========= " + ChatColor.AQUA + "" + ChatColor.BOLD + " JediPermissions " + ChatColor.GOLD + "" + ChatColor.BOLD + "========= ");
        add(ChatColor.GREEN + "/jp reload" + ChatColor.AQUA + ": reload permissions");
        add(ChatColor.GREEN + "/jp player" + ChatColor.AQUA + ": list of player commands");
        add(ChatColor.GREEN + "/jp group" + ChatColor.AQUA + ": list of group commands");
    }};
    private final List<String> PLAYERLIST = new ArrayList<String>(){{
        add(ChatColor.GOLD + "" + ChatColor.BOLD + "========= " + ChatColor.AQUA + "" + ChatColor.BOLD + " Player Help " + ChatColor.GOLD + "" + ChatColor.BOLD + "========= ");
        add(ChatColor.GREEN + "/jp player <player> perm add <world> <permission>");
        add(ChatColor.GREEN + "/jp player <player> perm remove <world> <permission>");
        add(ChatColor.GREEN + "/jp player <player> group add <world> <group>");
        add(ChatColor.GREEN + "/jp player <player> group remove <world> <group>");
    }};
    private final List<String> GROUPLIST = new ArrayList<String>(){{
        add(ChatColor.GOLD + "" + ChatColor.BOLD + "========= " + ChatColor.AQUA + "" + ChatColor.BOLD + " Group Help " + ChatColor.GOLD + "" + ChatColor.BOLD + "========= ");
        add(ChatColor.GREEN + "/jp group <group> create");
        add(ChatColor.GREEN + "/jp group <group> remove");
        add(ChatColor.GREEN + "/jp group <group> perm add <permission>");
        add(ChatColor.GREEN + "/jp group <group> perm remove <permission>");
        add(ChatColor.GREEN + "/jp group <group> child add <group>");
        add(ChatColor.GREEN + "/jp group <group> child remove <group>");
        add(ChatColor.DARK_RED + "/jp group <group> rank <rank>");
        add(ChatColor.DARK_RED + "/jp group <group> suffix <suffix>");
        add(ChatColor.DARK_RED + "/jp group <group> prefix <prefix>");
    }};

    //Constructor
    public JPermCommand(JavaPlugin plugin){
        this.plugin = plugin;
        groupManager = new GroupManager(plugin);
        try{
            colorChar = plugin.getConfig().getString("colorChar").charAt(0);
        }catch(NullPointerException npe){
            colorChar = '&';
        }

    }

    //Command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        //Check command
        String cmd = command.getName().toUpperCase();
        if(cmd.equals("JPERM")){

            //Check first arg
            if(args.length > 0){
                String arg1 = args[0].toUpperCase();

                //Player
                if(arg1.equals("P") || arg1.equals("PLAYER")){
                    if(!sender.hasPermission("jp.player")){
                        sender.sendMessage(CHATPREFIX + ChatColor.RED + "You do not have permission to use this command.");
                        return true;
                    }
                    modifyPlayerPerms(sender, args);
                    return true;
                //GROUP
                }else if(arg1.equals("G") || arg1.equals("GROUP")){
                    if(!sender.hasPermission("jp.group")){
                        sender.sendMessage(CHATPREFIX + ChatColor.RED + "You do not have permission to use this command.");
                        return true;
                    }
                    modifyGroup(sender, args);
                    return true;
                //RELOAD
                }else if(arg1.equals("RL") || arg1.equals("RELOAD")){
                    if(!sender.hasPermission("jp.reload")){
                        sender.sendMessage(CHATPREFIX + ChatColor.RED + "You do not have permission to use this command.");
                        return true;
                    }
                    groupManager.reloadPermissions();
                    sender.sendMessage(CHATPREFIX + ChatColor.GREEN + "JPermissions has been reloaded.");
                    return true;
                }
            }
            //None or invalid args
            showHelp(sender);
        }


        return true;
    }

    //Show help list
    public void showHelp(CommandSender sender){
        for(String string : HELPLIST){
            sender.sendMessage(string);
        }
    }

    //Modify player permissions
    public void modifyPlayerPerms(CommandSender sender, String[] args){
        //Check for player arg
        if(args.length > 1){
            String playerArg = args[1];
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getName().equalsIgnoreCase(playerArg)){

                    //Get perm player
                    User user;
                    try{
                        user = groupManager.getUser(player.getUniqueId());
                    }catch(NullPointerException npe){
                        user = new User(player, plugin);
                        groupManager.addUser(user);
                    }

                    //Check for permArg
                    if(hasPlayerPermArg(args)){

                        //Check for add arg
                        if(hasPlayerAddArg(args)){

                            //Check for world arg
                            if(args.length > 4){
                                String worldArg = args[4];
                                for(World world : Bukkit.getWorlds()){
                                    if(world.getName().equalsIgnoreCase(worldArg)){

                                        //Check for permission arg
                                        if(hasPlayerPermStringArg(args)){
                                            String permArg = args[5];

                                            //Add permission & save player perms
                                            if(!user.hasPermission(worldArg, permArg)){
                                                user.addPermission(worldArg, permArg);
                                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe permission has been added to &6%s &ain world '&6%s&a'.", player.getName(), worldArg)));
                                            }else{
                                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&6%s &calready has this permission in in world '&6%s&c'.", player.getName(), worldArg)));
                                            }

                                        }else{ //Invalid args
                                            showPlayerHelp(sender);
                                        }
                                        return;
                                    }
                                }
                                //Else world not found
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe world '&6%s&c' does not exist.", worldArg)));
                            }else{ //Invalid args
                                showPlayerHelp(sender);
                            }

                        //Check for remove arg
                        }else if(hasPlayerRemoveArg(args)){

                            //Check for world arg
                            if(args.length > 4){
                                String worldArg = args[4];
                                for(World world : Bukkit.getWorlds()){
                                    if(world.getName().equalsIgnoreCase(worldArg)){

                                        //Check for permission arg
                                        if(hasPlayerPermStringArg(args)){
                                            String permArg = args[5];

                                            //Remove permission & save player perms
                                            if(user.hasPermission(worldArg, permArg)){
                                                user.removePermission(worldArg, permArg);
                                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe permission has been removed from &6%s &ain the world '&6%s&a'.", player.getName(), worldArg)));
                                            }else{
                                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&6%s &cdoes not have this permission in the world '&6%s&c'.", player.getName(), worldArg)));
                                            }

                                        }else{ //Invalid args
                                            showPlayerHelp(sender);
                                        }
                                        return;
                                    }
                                }
                                //Else world not found
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe world '&6%s&c' does not exist.", worldArg)));
                            }else{ //Invalid args
                                showPlayerHelp(sender);
                            }

                        }else{ //Invalid args
                            showPlayerHelp(sender);
                        }

                    //Check for group arg
                    }else if(hasPlayerGroupArg(args)){

                        //Check for add arg
                        if(hasPlayerGroupAddArg(args)){

                            //Check for worldArg
                            if(args.length > 4){
                                String worldArg = args[4];
                                for(World world : Bukkit.getWorlds()){
                                    if(world.getName().equalsIgnoreCase(worldArg)){

                                        //Check for group arg
                                        if(args.length > 5){
                                            String groupArg = args[5];
                                            //Check if group exists
                                            if(groupManager.hasGroup(groupArg)){

                                                //If player does not have this group for this world, add it
                                                if(!user.hasGroup(worldArg, groupArg)){
                                                    user.addGroup(worldArg, groupArg);
                                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe group '&6%s&a' has been added to &6%s &ain the world '&6%s&a'.", groupArg, player.getName(), worldArg)));

                                                }else{ //Player already has it
                                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&6%s &calready has the group '&6%s&c' in world '&6%s&c'.", player.getName(),groupArg, worldArg)));
                                                }

                                            }else{ //Group does not exist
                                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' does not exist.", groupArg)));
                                            }
                                        }else{
                                            showPlayerHelp(sender);
                                        }
                                        return;
                                    }
                                }
                                //Else world not found
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe world '&6%s&c' does not exist.", worldArg)));

                            }else{ //Invalid args
                                showPlayerHelp(sender);
                            }

                        //Check for remove arg
                        }else if(hasPlayerGroupRemoveArg(args)){

                            //Check for worldArg
                            if(args.length > 4){
                                String worldArg = args[4];
                                for(World world : Bukkit.getWorlds()){
                                    if(world.getName().equalsIgnoreCase(worldArg)){

                                        //Check for group arg
                                        if(args.length > 5){
                                            String groupArg = args[5];
                                            //Check if group exists
                                            if(groupManager.hasGroup(groupArg)){

                                                //If the player has this group, remove it
                                                if(user.hasGroup(worldArg, groupArg)){
                                                    user.removeGroup(worldArg, groupArg);
                                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe group '&6%s&a' has been removed from &6%s &ain the world '&6%s&a'.", groupArg, player.getName(), worldArg)));

                                                }else{ //Player already has it
                                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&6%s &cdoes not have the group '&6%s&c' in world '&6%s&c'.", player.getName(), groupArg, worldArg)));
                                                }

                                            }else{ //Group does not exist
                                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' does not exist.", groupArg)));
                                            }
                                        }else{
                                            showPlayerHelp(sender);
                                        }
                                        return;
                                    }
                                }
                                //Else world not found
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe world '&6%s&c' does not exist.", worldArg)));

                            }else{ //Invalid args
                                showPlayerHelp(sender);
                            }

                        }else{ //Invalid args
                            showPlayerHelp(sender);
                        }

                    }else{ //Invalid args
                        showPlayerHelp(sender);
                    }
                    return;
                }
            }
            //Else player is not online
            sender.sendMessage(CHATPREFIX + ChatColor.RED + "The specified player is not online.");

        }else{ //No player arg
            showPlayerHelp(sender);
        }
    }
    public boolean hasPlayerPermArg(String[] args){
        if(args.length > 2){
            //Check for perm
            String permArg = args[2].toUpperCase();
            if(permArg.equals("P") || permArg.equals("PERM") || permArg.equals("PERMISSION")){
                return true;
            }
        }
        return false;
    }
    public boolean hasPlayerAddArg(String[] args){
        if(args.length > 3){
            //Check for add
            String addArg = args[3].toUpperCase();
            if(addArg.equals("A") || addArg.equals("ADD")){
                return true;
            }
        }
        return false;
    }
    public boolean hasPlayerRemoveArg(String[] args){
        if(args.length > 3){
            //Check for remove
            String removeArg = args[3].toUpperCase();
            if(removeArg.equals("R") || removeArg.equals("REMOVE")){
                return true;
            }
        }
        return false;
    }
    public boolean hasPlayerPermStringArg(String[] args){
        if(args.length > 5){
            return true;
        }
        return false;
    }
    public boolean hasPlayerGroupArg(String[] args){
        if(args.length > 2){
            //Check for group
            String groupArg = args[2].toUpperCase();
            if(groupArg.equals("G") || groupArg.equals("GROUP")){
                return true;
            }
        }
        return false;
    }
    public boolean hasPlayerGroupAddArg(String[] args){
        if(args.length > 3){
            //Check for add arg
            String addArg = args[3].toUpperCase();
            if(addArg.equals("A") || addArg.equals("ADD")){
                return true;
            }
        }
        return false;
    }
    public boolean hasPlayerGroupRemoveArg(String[] args){
        if(args.length > 3){
            //Check for remove arg
            String removeArg = args[3].toUpperCase();
            if(removeArg.equals("R") || removeArg.equals("REMOVE")){
                return true;
            }
        }
        return false;
    }
    public void showPlayerHelp(CommandSender sender){
        for(String string : PLAYERLIST){
            sender.sendMessage(string);
        }
    }

    //Modify groups
    public void modifyGroup(CommandSender sender, String[] args){

        //Check for group arg
        if(args.length > 1){
            String groupArg = args[1];

            //Check for create
            if(hasGroupCreateArg(args)){

                //Check if group exists
                if(!groupManager.hasGroup(groupArg)){
                    groupManager.createGroup(groupArg);
                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe group '&6%s&a' has been created.", groupArg)));

                }else{ //Group exists
                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' already exists.", groupArg)));
                }
                return;

            //Check for remove arg
            }else if(hasGroupRemoveArg(args)){

                //Check if it exists
                if(groupManager.hasGroup(groupArg)){
                    groupManager.removeGroup(groupManager.getGroup(groupArg));
                    //groupManager.reloadPermissions();
                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe group '&6%s&a' has been removed.", groupArg)));

                }else{ //Group does not exist
                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' does not exist.", groupArg)));
                }
                return;

            //Check for perm arg
            }else if(hasGroupPermArg(args)){

                //Check if group exists
                if(groupManager.hasGroup(groupArg)){
                    Group group = groupManager.getGroup(groupArg);

                    //Check for add arg
                    if(hasGroupAddPermArg(args)){
                        //Check for permissionString
                        if(args.length > 4){
                            String permString = args[4];

                            //Check if this group has this permission
                            if(!group.hasPermission(permString)){
                                group.addPermission(permString);
                                group.saveGroup();
                               // groupManager.reloadPermissions();
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe permission has been added to the group '&6%s&a'.", groupArg)));

                            }else{ //Group already has this perm
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' already has this permission.", groupArg)));
                            }
                            return;

                        }//No perm arg

                    //Check for remove arg
                    }else if(hasGroupRemovePermArg(args)){
                        //Check for perm string
                        if(args.length > 4){
                            String permString = args[4];

                            //Check if group has this permission
                            if(group.hasPermission(permString)){
                                group.removePermission(permString);
                                group.saveGroup();
                               // groupManager.reloadPermissions();
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe permission has been removed from the group '&6%s&a'.", groupArg)));

                            }else{ //Group doesn't have this perm
                                sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' does not have this permission.", groupArg)));
                            }
                            return;

                        }//No perm arg
                    }//Invalid args
                    showGroupHelp(sender);

                }else{ //Group doesn't exist
                    sender.sendMessage(CHATPREFIX + ChatColor.RED + "This group does not exist.");
                }
                return;

            //Check for child arg
            }else if(hasGroupChildArg(args)){

                //Make sure group exists
                if(groupManager.hasGroup(groupArg)){
                    Group group = groupManager.getGroup(groupArg);

                    //Check for add/remove arg
                    if(args.length > 3){
                        String addRemArg = args[3].toUpperCase();

                        //ADD
                        if(addRemArg.equals("A") || addRemArg.equals("ADD")){
                            //Check for child string
                            if(args.length > 4){
                                String childString = args[4];

                                //Make sure the child isn't the same group as groupArg
                                if(groupArg.equalsIgnoreCase(childString)){
                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cYou cannot add the group '&6%s&c' to itself.", childString)));
                                    return;
                                }

                                //Check if group has this child
                                if(!group.hasChild(childString)){
                                    //Check if child group exists
                                    if(groupManager.hasGroup(childString)){
                                        group.addChild(childString);
                                        group.saveGroup();
                                       // groupManager.reloadPermissions();
                                        sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe child group '&6%s&a' was added to the group '&6%s&a'.", childString, groupArg)));

                                    }else{ //Child group does not exist
                                        sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe child group '&6%s&c' does not exist.", childString)));
                                    }

                                }else{
                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' already has the child group '&6%s&c'.", groupArg, childString)));
                                }
                                return;

                            }//Else no child arg

                        //REMOVE
                        }else if(addRemArg.equals("R") || addRemArg.equals("REMOVE")){
                            //Check for child string
                            if(args.length > 4){
                                String childString = args[4];
                                //Check if group has this child
                                if(group.hasChild(childString)){
                                    //Check if child group exists
                                    if(groupManager.hasGroup(childString)){
                                        group.removeChild(childString);
                                        group.saveGroup();
                                       // groupManager.reloadPermissions();
                                        sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&aThe child group '&6%s&a' was removed from the group '&6%s&a'.", childString, groupArg)));

                                    }else{ //Child group does not exist
                                        sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe child group '&6%s&c' does not exist.", childString)));
                                    }

                                }else{
                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes('&', String.format("&cThe group '&6%s&c' does not have the child group '&6%s&c'.", groupArg, childString)));
                                }
                                return;

                            }//Else no child arg
                        }

                    }//Else show help
                    showGroupHelp(sender);

                }else{
                    sender.sendMessage(CHATPREFIX + ChatColor.RED + "This group does not exist.");
                }
                return;

            //Check for prefix/suffix/rank args
            }else{
                if(args.length > 2){
                    String arg3 = args[2].toUpperCase();

                    //Check if group exists
                    if(groupManager.hasGroup(groupArg)){
                        //RANK
                        if(arg3.equals("RANK")){

                            //Check for rank arg
                            if(args.length > 3){
                                try{
                                    int rank = Integer.parseInt(args[3]);
                                    Group g = groupManager.getGroup(groupArg);
                                    g.setRank(rank);
                                    sender.sendMessage(CHATPREFIX + ChatColor.translateAlternateColorCodes(colorChar, ""));

                                }catch(NumberFormatException nfe){ //Not an int
                                    sender.sendMessage(CHATPREFIX + ChatColor.RED + "Must enter a number for rank.");
                                }

                            }else{ //Invalid args
                                showGroupHelp(sender);
                            }
                            return;

                        //PREFIX
                        }else if(arg3.equals("PREFIX")){

                            //Check for prefix arg
                            if(args.length > 3){
                                String prefix = args[3];
                                Group g = groupManager.getGroup(groupArg);
                                g.setPrefix(prefix);
                                sender.sendMessage(CHATPREFIX + ChatColor.RED + "Group rank set to " + prefix);
                            }else{ //Invalid args
                                showGroupHelp(sender);
                            }
                            return;

                        //SUFFIX
                        }else if(arg3.equals("SUFFIX")){

                            //Check for suffix arg
                            if(args.length > 3){
                                String suffix = args[3];
                                Group g = groupManager.getGroup(groupArg);
                                g.setSuffix(suffix);
                                sender.sendMessage(CHATPREFIX + ChatColor.RED + "Group prefix set to " + suffix);

                            }else{ //Invalid args
                                showGroupHelp(sender);
                            }
                            return;

                        }

                    }else{ //Group doesn't exist
                        sender.sendMessage(CHATPREFIX + ChatColor.RED + "This group does not exist.");
                    }
                    return;
                }//Invalid args
            }
            //Else invalid args
        }
        //Else invalid args
        showGroupHelp(sender);
    }
    public boolean hasGroupCreateArg(String[] args){
        if(args.length > 2){
            //Check for create arg
            String createArg = args[2].toUpperCase();
            if(createArg.equals("C") || createArg.equals("CREATE")){
                return true;
            }
        }
        return false;
    }
    public boolean hasGroupRemoveArg(String[] args){
        if(args.length > 2){
            //Check for remove arg
            String removeArg = args[2].toUpperCase();
            if(removeArg.equals("R") || removeArg.equals("REMOVE")){
                return true;
            }
        }
        return false;
    }
    public boolean hasGroupPermArg(String[] args){
        if(args.length > 2){
            //Check for perm arg
            String permArg = args[2].toUpperCase();
            if(permArg.equals("P") || permArg.equals("PERM") || permArg.equals("PERMISSION")){
                return true;
            }
        }
        return false;
    }
    public boolean hasGroupAddPermArg(String[] args){
        if(args.length > 3){
            //Check for add arg
            String addArg = args[3].toUpperCase();
            if(addArg.equals("A") || addArg.equals("ADD")){
                return true;
            }
        }
        return false;
    }
    public boolean hasGroupRemovePermArg(String[] args){
        if(args.length > 3){
            //Check for remove arg
            String removeArg = args[3].toUpperCase();
            if(removeArg.equals("R") || removeArg.equals("REMOVE")){
                return true;
            }
        }
        return false;
    }
    public boolean hasGroupChildArg(String[] args){
        if(args.length > 2){
            String childArg = args[2].toUpperCase();
            if(childArg.equals("CH") || childArg.equals("CHILD")){
                return true;
            }
        }
        return false;
    }
    public void showGroupHelp(CommandSender sender){
        for(String string : GROUPLIST){
            sender.sendMessage(string);
        }
    }

}
