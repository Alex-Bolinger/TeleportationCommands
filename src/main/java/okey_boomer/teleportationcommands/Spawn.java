package okey_boomer.teleportationcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {
    private final Plugin plugin;

    public Spawn(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            World w = p.getWorld();
            p.teleport(w.getSpawnLocation());
            p.sendMessage("Teleported to Spawn");
            return true;
        } else {
            return false;
        }
    }
}
