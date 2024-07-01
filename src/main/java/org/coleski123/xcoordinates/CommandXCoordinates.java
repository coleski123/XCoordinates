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

        String prefix = ChatColor.GOLD + "[XCoordinates]" + " ";
        Player player = (Player) sender;

        // Check if the player has the required permission
        if (!player.hasPermission("xcoordinates.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "Usage: /xcoordinates <enable|disable|global> [enable|disable]");
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (plugin.isGlobalCoordinatesEnabled()) {
                player.sendMessage(prefix + ChatColor.RED + "Cannot enable coordinates because global coordinates are enabled.");
                return true;
            }
            plugin.setCoordinatesEnabled(player, true);
            player.sendMessage(prefix + ChatColor.GREEN + "Coordinates have been enabled.");
        } else if (args[0].equalsIgnoreCase("disable")) {
            if (plugin.isGlobalCoordinatesEnabled()) {
                player.sendMessage(prefix + ChatColor.RED + "Cannot disable coordinates because global coordinates are enabled.");
                return true;
            }
            plugin.setCoordinatesEnabled(player, false);
            player.sendMessage(prefix + ChatColor.RED + "Coordinates have been disabled.");
        } else if (args[0].equalsIgnoreCase("global")) {
            if (!player.hasPermission("xcoordinates.use.global")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the global command!");
                return true;
            }

            if (args.length != 2) {
                player.sendMessage(prefix + ChatColor.RED + "Usage: /xcoordinates global <enable|disable>");
                return true;
            }

            if (args[1].equalsIgnoreCase("enable")) {
                plugin.setGlobalCoordinatesEnabled(true);
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    plugin.setCoordinatesEnabled(p, true);
                }
                player.sendMessage(prefix + ChatColor.GREEN + "Global coordinates have been enabled.");
            } else if (args[1].equalsIgnoreCase("disable")) {
                plugin.setGlobalCoordinatesEnabled(false);
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    plugin.setCoordinatesEnabled(p, false);
                }
                player.sendMessage(prefix + ChatColor.RED + "Global coordinates have been disabled.");
            } else {
                player.sendMessage(prefix + ChatColor.RED + "Usage: /xcoordinates global <enable|disable>");
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Usage: /xcoordinates <enable|disable|global> [enable|disable]");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            if ("enable".startsWith(partial)) {
                completions.add("enable");
            }
            if ("disable".startsWith(partial)) {
                completions.add("disable");
            }
            if ("global".startsWith(partial)) {
                completions.add("global");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("global")) {
            String partial = args[1].toLowerCase();
            if ("enable".startsWith(partial)) {
                completions.add("enable");
            }
            if ("disable".startsWith(partial)) {
                completions.add("disable");
            }
        }

        return completions;
    }
}