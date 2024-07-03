package org.coleski123.xcoordinates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatMessages {

    private final XCoordinates plugin;

    public ChatMessages(XCoordinates plugin){
        this.plugin = plugin;
    }

    public String Prefix(){
        return ChatColor.GOLD + "[XCoordinates]" + " ";
    }

    public void plyOnlyMessage(CommandSender sender){
        sender.sendMessage(Prefix() + ChatColor.RED + "This command can only be used by players!");
    }

    public void noPermMessage(Player player, CommandSender sender){
        player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
    }

    public void cmdUsageMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.RED + "Usage: /xcoordinates <enable | disable | global> [enable | disable]");
    }

    public void globalCoordsEnableFailMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.RED + "Cannot enable coordinates because global coordinates are enabled.");
    }

    public void coordsEnabledMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.GREEN + "Coordinates have been enabled!");
    }

    public void globalCordsDisableFailMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.RED + "Cannot disable coordinates because global coordinates are enabled.");
    }

    public void coordsDisabledMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.RED + "Coordinates have been disabled");
    }

    public void globalCoordsPermFail(Player player, CommandSender sender){
        player.sendMessage(ChatColor.RED + "You do not have permission to use the global coordinates command!");
    }

    public void globalCoordsUsageMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.YELLOW + "Usage: /xcoordinates global <enable | disable>");
    }

    public void globalCoordsEnabledMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.GREEN + "Global coordinates have been enabled!");
    }

    public void globalCoordsDisabledMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.RED + "Global coordinates have been disabled!");
    }

    public void mainCmdFailMessage(Player player, CommandSender sender){
        player.sendMessage(Prefix() + ChatColor.YELLOW + "Usage: /xcoordinates <enable | disable | global> [enable | disable]");
    }
}
