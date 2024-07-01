package org.coleski123.xcoordinates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandXCoordinates implements CommandExecutor, TabCompleter {

    private final XCoordinates plugin;

    public CommandXCoordinates(XCoordinates plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        String Prefix = ChatColor.GOLD + "[XCoordinates]" + " ";

        Player player = (Player) sender;

        // Check if the player has the required permission
        if (!player.hasPermission("xcoordinates.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /xcoordinates <enable|disable>");
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            plugin.setCoordinatesEnabled(player, true);
            player.sendMessage(Prefix + ChatColor.GREEN + "Coordinates have been enabled.");
        } else if (args[0].equalsIgnoreCase("disable")) {
            plugin.setCoordinatesEnabled(player, false);
            player.sendMessage(Prefix + ChatColor.RED + "Coordinates have been disabled.");
        } else {
            player.sendMessage(Prefix + ChatColor.RED + "Usage: /xcoordinates <enable|disable>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            if ("enable".startsWith(partial)) {
                completions.add("Enable");
            }
            if ("disable".startsWith(partial)) {
                completions.add("Disable");
            }
        }

        return completions;
    }
}