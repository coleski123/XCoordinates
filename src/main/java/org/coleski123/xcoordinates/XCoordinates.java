package org.coleski123.xcoordinates;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class XCoordinates extends JavaPlugin {

    private final HashMap<UUID, Boolean> playerCoordinatesEnabled = new HashMap<>();
    private String pluginPrefix = ChatColor.GOLD + "[XCoordinates]";
    private File playerDataFile;
    private FileConfiguration playerDataConfig;
    private boolean globalCoordinatesEnabled;

    @Override
    public void onEnable() {
        int pluginId = 22479;
        Metrics metrics = new Metrics(this, pluginId);

        sendConsoleMessage(ChatColor.GREEN + "XCoordinates has been enabled!");
        CommandXCoordinates commandExecutor = new CommandXCoordinates(this);
        this.getCommand("xcoordinates").setExecutor(commandExecutor);
        this.getCommand("xcoordinates").setTabCompleter(commandExecutor);
        new CoordinateDisplayTask().runTaskTimer(this, 0, 1);

        new UpdateChecker(this, 117734).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                sendConsoleMessage("&2No new versions available.");
            } else {
                sendConsoleMessage("&cA new version is now available! Download: https://www.spigotmc.org/resources/xcoordinates.117734/");
            }
        });

        // Load player data
        loadPlayerData();
    }

    @Override
    public void onDisable() {
        sendConsoleMessage(ChatColor.RED + "XCoordinates has been disabled!");

        // Save player data
        savePlayerData();
    }

    public boolean isCoordinatesEnabled(Player player) {
        return globalCoordinatesEnabled || playerCoordinatesEnabled.getOrDefault(player.getUniqueId(), false);
    }

    public void setCoordinatesEnabled(Player player, boolean enabled) {
        playerCoordinatesEnabled.put(player.getUniqueId(), enabled);
        playerDataConfig.set(player.getUniqueId().toString(), enabled);
        savePlayerData();
    }

    public boolean isGlobalCoordinatesEnabled() {
        return globalCoordinatesEnabled;
    }

    public void setGlobalCoordinatesEnabled(boolean enabled) {
        globalCoordinatesEnabled = enabled;
        playerDataConfig.set("globalCoordinatesEnabled", enabled);
        savePlayerData();

        // Update individual players' coordinates based on global state
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (enabled) {
                setCoordinatesEnabled(player, true);
            } else {
                setCoordinatesEnabled(player, false);
            }
        }
    }

    private void loadPlayerData() {
        playerDataFile = new File(getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            playerDataFile.getParentFile().mkdirs();
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        globalCoordinatesEnabled = playerDataConfig.getBoolean("globalCoordinatesEnabled", false);
        for (String key : playerDataConfig.getKeys(false)) {
            if (key.equals("globalCoordinatesEnabled")) continue;
            UUID playerUUID = UUID.fromString(key);
            boolean enabled = playerDataConfig.getBoolean(key);
            playerCoordinatesEnabled.put(playerUUID, enabled);
        }
    }

    private void savePlayerData() {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void sendConsoleMessage(String message) {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + " " + message));
    }
}