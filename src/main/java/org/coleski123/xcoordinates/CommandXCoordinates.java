package org.coleski123.xcoordinates;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandXCoordinates implements CommandExecutor, TabCompleter {

    private final XCoordinates plugin;
    private ChatMessages chatMessages;

    public CommandXCoordinates(XCoordinates plugin) {
        this.plugin = plugin;
        chatMessages = new ChatMessages(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            chatMessages.plyOnlyMessage(sender);
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has the required permission
        if (!player.hasPermission("xcoordinates.use")) {
            chatMessages.noPermMessage(player, sender);
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            chatMessages.cmdUsageMessage(player, sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (plugin.isGlobalCoordinatesEnabled()) {
                chatMessages.globalCoordsEnableFailMessage(player, sender);
                return true;
            }
            plugin.setCoordinatesEnabled(player, true);
            chatMessages.coordsEnabledMessage(player, sender);
        } else if (args[0].equalsIgnoreCase("disable")) {
            if (plugin.isGlobalCoordinatesEnabled()) {
                chatMessages.globalCordsDisableFailMessage(player, sender);
                return true;
            }
            plugin.setCoordinatesEnabled(player, false);
            chatMessages.coordsDisabledMessage(player, sender);
        } else if (args[0].equalsIgnoreCase("global")) {
            if (!player.hasPermission("xcoordinates.use.global")) {
                chatMessages.globalCoordsPermFail(player, sender);
                return true;
            }

            if (args.length != 2) {
                chatMessages.globalCoordsUsageMessage(player, sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("enable")) {
                plugin.setGlobalCoordinatesEnabled(true);
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    plugin.setCoordinatesEnabled(p, true);
                }
                chatMessages.globalCoordsEnabledMessage(player, sender);
            } else if (args[1].equalsIgnoreCase("disable")) {
                plugin.setGlobalCoordinatesEnabled(false);
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    plugin.setCoordinatesEnabled(p, false);
                }
                chatMessages.globalCoordsDisabledMessage(player, sender);
            } else {
                chatMessages.globalCoordsUsageMessage(player, sender);
            }
        } else {
            chatMessages.mainCmdFailMessage(player, sender);
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