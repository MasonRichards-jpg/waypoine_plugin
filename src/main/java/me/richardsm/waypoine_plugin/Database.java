package me.richardsm.waypoine_plugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:waypoints.db");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS waypoints (" +
                    "player_uuid TEXT NOT NULL, " +
                    "waypoint_name TEXT NOT NULL, " +
                    "x REAL NOT NULL, " +
                    "y REAL NOT NULL, " +
                    "z REAL NOT NULL, " +
                    "world TEXT NOT NULL, " +
                    "PRIMARY KEY (player_uuid, waypoint_name))";
            connection.createStatement().execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWaypoint(String uuid, String waypointName, String worldName, double x, double y, double z) {
        try {
            String query = "INSERT OR REPLACE INTO waypoints (player_uuid, waypoint_name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, uuid);
                stmt.setString(2, waypointName);
                stmt.setString(3, worldName);
                stmt.setDouble(4, x);
                stmt.setDouble(5, y);
                stmt.setDouble(6, z);
                stmt.executeUpdate();
                System.out.println("Waypoint saved: " + waypointName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all waypoints for a player and return as a list
    public List<Waypoint> getWaypoints(String uuid) {
        List<Waypoint> waypoints = new ArrayList<>();
        try {
            String query = "SELECT waypoint_name, world, x, y, z FROM waypoints WHERE player_uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String waypointName = rs.getString("waypoint_name");
                String world = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                waypoints.add(new Waypoint(waypointName, world, x, y, z));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return waypoints;
    }

    // Get a specific waypoint by name for a player and return a waypoint object
    public Waypoint getWaypoint(String uuid, String waypointName) {
        try {
            String query = "SELECT world, x, y, z FROM waypoints WHERE player_uuid = ? AND waypoint_name = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, uuid);
            stmt.setString(2, waypointName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String world = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                rs.close();
                return new Waypoint(waypointName, world, x, y, z);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteWaypoint(String uuid, String waypointName) {
        try {
            String query = "DELETE FROM waypoints WHERE player_uuid = ? AND waypoint_name = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, uuid);
            stmt.setString(2, waypointName);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Waypoint removed: " + waypointName);
            } else {
                System.out.println("No waypoint found to remove: " + waypointName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the database connection
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
