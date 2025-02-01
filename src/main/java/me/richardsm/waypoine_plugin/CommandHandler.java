package me.richardsm.waypoine_plugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler {
    private Database db;
    public CommandHandler(Database db){
        this.db = db;
    }
    public void handleSetWaypoint(Player player, String waypointName){
        String uuid = player.getUniqueId().toString();
        Location location = player.getLocation();
        db.saveWaypoint(uuid, waypointName, location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
        player.sendMessage("Waypoint saved: " + waypointName + " at " + location.toString());
    }
    public void handleListWaypoints(Player player){
        String uuid = player.getUniqueId().toString();
        List<Waypoint> waypoints = db.getWaypoints(uuid);
        if(waypoints.isEmpty()){
            player.sendMessage("No waypoints found.");
        } else {
            for(Waypoint waypoint : waypoints){
                player.sendMessage(waypoint.getName() + " at " + waypoint.getWorld() + " " + waypoint.getX() + " " + waypoint.getY() + " " + waypoint.getZ());
            }
        }
    }
    public void handleRemoveWaypoint(Player player, String waypointName) {
        String uuid = player.getUniqueId().toString();
        db.deleteWaypoint(uuid, waypointName);
        player.sendMessage("Waypoint removed: " + waypointName);
    }


    public void handleTeleportToWaypoint(Player player, String waypointName){
        String uuid = player.getUniqueId().toString();
        Waypoint waypoint = db.getWaypoint(uuid, waypointName);
        if(waypoint == null){
            player.sendMessage("Waypoint not found.");
        } else {
            player.teleport(new Location(player.getWorld(), waypoint.getX(), waypoint.getY(), waypoint.getZ()));
            player.sendMessage("Teleported to " + waypointName);
        }
    }
}
