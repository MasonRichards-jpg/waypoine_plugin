package me.richardsm.waypoine_plugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    private Database db;
    private Plugin plugin; // Add a Plugin instance to schedule tasks
    private Map<Player, BukkitRunnable> navigationTasks = new HashMap<>(); // Track active navigation tasks

    public CommandHandler(Database db, Plugin plugin) {
        this.db = db;
        this.plugin = plugin; // Initialize the plugin instance
    }

    public void handleSetWaypoint(Player player, String waypointName) {
        String uuid = player.getUniqueId().toString();
        Location location = player.getLocation();
        db.saveWaypoint(uuid, waypointName, location.getWorld().getName(), location.getX(), location.getY(), location.getZ());

        int roundedX = (int) Math.round(location.getX());
        int roundedY = (int) Math.round(location.getY());
        int roundedZ = (int) Math.round(location.getZ());

        player.sendMessage("Waypoint saved: " + waypointName + " at " + roundedX + " " + roundedY + " " + roundedZ);
    }

    public void handleListWaypoints(Player player) {
        String uuid = player.getUniqueId().toString();
        List<Waypoint> waypoints = db.getWaypoints(uuid);
        if (waypoints.isEmpty()) {
            player.sendMessage("No waypoints found.");
        } else {
            for (Waypoint waypoint : waypoints) {
                int roundedX = (int) Math.round(waypoint.getX());
                int roundedY = (int) Math.round(waypoint.getY());
                int roundedZ = (int) Math.round(waypoint.getZ());

                player.sendMessage(waypoint.getName() + " at " + waypoint.getWorld() + " " + roundedX + " " + roundedY + " " + roundedZ);
            }
        }
    }

    public void handleRemoveWaypoint(Player player, String waypointName) {
        String uuid = player.getUniqueId().toString();
        db.deleteWaypoint(uuid, waypointName);
        player.sendMessage("Waypoint removed: " + waypointName);
    }

    public void handleCompassNavi(Player player, String waypointName) {
        String uuid = player.getUniqueId().toString();
        Waypoint waypoint = db.getWaypoint(uuid, waypointName);

        if (waypoint == null) {
            player.sendMessage("Waypoint not found.");
        } else {
            // Set the compass target to the waypoint
            Location waypointLocation = waypoint.toLocation();
            player.setCompassTarget(waypointLocation);

            // Round the waypoint coordinates
            int roundedX = (int) Math.round(waypoint.getX());
            int roundedY = (int) Math.round(waypoint.getY());
            int roundedZ = (int) Math.round(waypoint.getZ());

            // Notify the player
            player.sendMessage("Navigating to " + waypointName + roundedX + " " + roundedY + " " + roundedZ);

            // Cancel any existing navigation task for this player
            if (navigationTasks.containsKey(player)) {
                navigationTasks.get(player).cancel();
                navigationTasks.remove(player);
            }

            // Start a new repeating task to update the action bar
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    // Calculate the distance to the waypoint
                    double distance = player.getLocation().distance(waypointLocation);

                    // If the player is within 10 blocks, stop the task
                    if (distance <= 10) {
                        player.sendActionBar(new TextComponent("§aYou have reached your destination!"));
                        this.cancel();
                        navigationTasks.remove(player);
                        return;
                    }

                    // Update the action bar with the coordinates and distance
                    String actionBarMessage = "§eNavigating to " + waypointName +
                            " " + roundedX + " " + roundedY + " " + roundedZ +
                            " §7(" + (int) distance + "m away)";

                    player.sendActionBar(new TextComponent(actionBarMessage));
                }
            };

            // Schedule the task to run every 20 ticks (1 second)
            task.runTaskTimer(plugin, 0L, 20L);

            // Store the task for this player
            navigationTasks.put(player, task);
        }
    }
    public void handleCancelNavigation(Player player) {
        // Cancel the current navigation task if it exists
        if (navigationTasks.containsKey(player)) {
            navigationTasks.get(player).cancel();
            navigationTasks.remove(player);
        }

        // Clear the action bar message by sending an empty message
        player.sendActionBar(new TextComponent(""));

        player.sendMessage("Waypoint navigation canceled.");
    }


    public void handleRemoveAllWaypoints(Player player) {
        String uuid = player.getUniqueId().toString();  // Get the player's unique UUID
        db.deleteAllWaypoints(uuid);  // Call the database method to delete all waypoints
    }
}