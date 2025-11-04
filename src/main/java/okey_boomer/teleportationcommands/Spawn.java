package okey_boomer.teleportationcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.entity.TeleportFlag;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class Spawn implements CommandExecutor {
    private ComponentLogger LOGGER;

    public Spawn(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            World w = p.getWorld();
            TeleportHelper.teleport(p, w.getSpawnLocation());
            LOGGER.info("Teleported player: " + ((Player)sender).getName() + " to spawn");
            return true;
        } else {
            return false;
        }
    }
}
