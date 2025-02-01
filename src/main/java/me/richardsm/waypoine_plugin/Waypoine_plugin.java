package me.richardsm.waypoine_plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Waypoine_plugin extends JavaPlugin {
    private Database db;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        this.db = new Database();
        this.db.connect();
        this.commandHandler = new CommandHandler(db);

        this.getCommand("setwaypoint").setExecutor(this);
        getLogger().info("Waypoint plugin enabled bitches");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(db != null){
            db.disconnect();
        }
        getLogger().info("Waypoint plugin shut down");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof org.bukkit.entity.Player){
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("setwaypoint")){
                if(args.length == 1){
                    commandHandler.handleSetWaypoint(player, args[0]);
                    return true;
                } else {
                    player.sendMessage("Usage: /setwaypoint <waypoint name>");
                    return false;
                }
            }
            if(command.getName().equalsIgnoreCase("listwaypoints")){
                if(args.length == 1 && args[0].equalsIgnoreCase("list")){
                    commandHandler.handleListWaypoints(player);
                    return true;
                } else if (args.length == 2&& args[0].equalsIgnoreCase("tp")){
                    commandHandler.handleTeleportToWaypoint(player, args[1]);
                    return true;
                } else if (args.length == 2&& args[0].equalsIgnoreCase("remove")){
                    commandHandler.handleRemoveWaypoint(player, args[1]);
                }
            }
        }
        return false;

    }
}
