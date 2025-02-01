package me.richardsm.waypoine_plugin;

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
}
