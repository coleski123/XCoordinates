package org.coleski123.xcoordinates;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class XCoordinates extends JavaPlugin {

    private final HashMap<UUID, Boolean> playerCoordinatesEnabled = new HashMap<>();

    @Override
    public void onEnable() {
        this.getCommand("xcoordinates").setExecutor(new CommandXCoordinates(this));
        new CoordinateDisplayTask().runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean isCoordinatesEnabled(Player player) {
        return playerCoordinatesEnabled.getOrDefault(player.getUniqueId(), false);
    }

    public void setCoordinatesEnabled(Player player, boolean enabled) {
        playerCoordinatesEnabled.put(player.getUniqueId(), enabled);
    }

    private class CoordinateDisplayTask extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isCoordinatesEnabled(player)) {
                    Location loc = player.getLocation();
                    String direction = getCardinalDirection(player);
                    String message = String.format(ChatColor.GOLD + "X:" + ' ' + ChatColor.WHITE + "%.1f" + ' ' + ChatColor.GOLD + "Y:" + ' ' + ChatColor.WHITE + "%.1f" + ' ' + ChatColor.GOLD + "Z:" + ' ' + ChatColor.WHITE + "%.1f %s",
                            loc.getX(), loc.getY(), loc.getZ(), direction);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }
            }
        }

        private String getCardinalDirection(Player player) {
            float yaw = player.getLocation().getYaw();
            yaw = (yaw % 360 + 360) % 360; // Ensure yaw is between 0 and 360

            // Adjust yaw by 180 degrees to flip the direction
            yaw = (yaw + 180) % 360;

            int i = (int) ((yaw + 11.25) / 22.5);
            return switch (i) {
                case 0 -> ChatColor.AQUA + "N";
                case 1 -> ChatColor.AQUA + "NNE";
                case 2 -> ChatColor.AQUA + "NE";
                case 3 -> ChatColor.AQUA + "ENE";
                case 4 -> ChatColor.AQUA + "E";
                case 5 -> ChatColor.AQUA + "ESE";
                case 6 -> ChatColor.AQUA + "SE";
                case 7 -> ChatColor.AQUA + "SSE";
                case 8 -> ChatColor.AQUA + "S";
                case 9 -> ChatColor.AQUA + "SSW";
                case 10 -> ChatColor.AQUA + "SW";
                case 11 -> ChatColor.AQUA + "WSW";
                case 12 -> ChatColor.AQUA + "W";
                case 13 -> ChatColor.AQUA + "WNW";
                case 14 -> ChatColor.AQUA + "NW";
                case 15 -> ChatColor.AQUA + "NNW";
                default -> ChatColor.AQUA + "N";
            };
        }
    }
}