package me.richardsm.waypoine_plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Waypoine_plugin extends JavaPlugin {
    private Database db;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        this.db = new Database();
        this.db.connect();
        this.commandHandler = new CommandHandler(db, this); // Pass 'this' (the plugin instance)

        this.getCommand("setwaypoint").setExecutor(this);
        this.getCommand("listwaypoints").setExecutor(this);
        this.getCommand("removewaypoint").setExecutor(this);
        this.getCommand("waypoint").setExecutor(this);
        this.getCommand("wp").setExecutor(this);
        this.getCommand("lstwp").setExecutor(this);
        this.getCommand("rmwp").setExecutor(this);
        this.getCommand("wpcancel").setExecutor(this);



        getLogger().info("Waypoint plugin enabled bitches");
    }

    @Override
    public void onDisable() {
        if (db != null) {
            db.disconnect();
        }
        getLogger().info("Waypoint plugin shut down");
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (command.getName().equalsIgnoreCase("setwaypoint")) {
                if (args.length == 1) {
                    commandHandler.handleSetWaypoint(player, args[0]);
                    return true;
                } else {
                    player.sendMessage("Usage: /setwaypoint <waypoint name>");
                    return false;
                }
            }
            if (command.getName().equalsIgnoreCase("listwaypoints") || command.getName().equalsIgnoreCase("lstwp")) {
                commandHandler.handleListWaypoints(player);
                return true;
            }
            if (command.getName().equalsIgnoreCase("removewaypoint") || command.getName().equalsIgnoreCase("rmwp")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        commandHandler.handleRemoveAllWaypoints(player);
                        player.sendMessage("All waypoints removed.");
                    } else {
                        commandHandler.handleRemoveWaypoint(player, args[0]);
                    }
                    return true;
                }
            }
            if (command.getName().equalsIgnoreCase("waypoint") || command.getName().equalsIgnoreCase("wp")) {
                if (args.length == 1) {
                    commandHandler.handleCompassNavi(player, args[0]);
                    return true;
                } else {
                    player.sendMessage("Usage: /waypoint <waypoint name>");
                    return false;
                }
            }
            if (command.getName().equalsIgnoreCase("wpcancel")) {
                commandHandler.handleCancelNavigation(player);
                return true;
            }

        }
        return false;
    }
}