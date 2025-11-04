package okey_boomer.teleportationcommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class Back implements CommandExecutor{
    private ComponentLogger LOGGER;

    public Back(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName());
        if (home.exists()) {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(home));
                String coord = bfr.readLine();
                boolean found = false;
                while (coord != null && !found) {
                    if (coord.substring(0, coord.indexOf(' ')).equals("death")) {
                        coord = coord.substring(coord.indexOf(' ')+1);
                        found = true;
                    } else {
                        coord = bfr.readLine();
                    }
                }
                bfr.close();
                if (coord == null) {
                    sender.sendMessage("Death location not found");
                    return true;
                }
                Location l = TeleportHelper.deserializeLocation(coord);
                Player p = (Player) sender;
                LOGGER.info("Teleporting player: " + p.getName() + " to " + l.toString());
                TeleportHelper.teleport(p, l);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            sender.sendMessage("Death location not found");
        }
        return true;
    }
}
