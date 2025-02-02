package me.richardsm.waypoine_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Waypoint {
    private String name;
    private String world;
    private double x, y, z;

    public Waypoint(String name, String world, double x, double y, double z) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    public Location toLocation() {
        World bukkitWorld = Bukkit.getWorld(world); // Get the World object by name
        if (bukkitWorld == null) {
            throw new IllegalStateException("World '" + world + "' is not loaded!");
        }
        return new Location(bukkitWorld, x, y, z); // Create and return the Location
    }
}
