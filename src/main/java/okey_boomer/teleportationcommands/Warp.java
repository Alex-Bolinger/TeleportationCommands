package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Warp implements CommandExecutor {
    private ComponentLogger LOGGER;

    public Warp(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {

            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat"));
            boolean found = false;
            String line = bfr.readLine();
            String warpName = args[0];
            for (int i = 1; i < args.length; i++) {
                warpName += " " + args[i];
            }
            warpName = warpName.toLowerCase();
            while (!found && line != null) {
                String otherWarp = line.substring(0, line.indexOf(","));
                if (otherWarp.toLowerCase().equals(warpName)) {
                    found = true;
                } else {
                    line = bfr.readLine();
                }
            }
            bfr.close();
            Player p = (Player) sender;
            if (!found) {
                p.sendMessage("Invalid Warp");
                return true;
            }
            String coords = line.substring(line.indexOf(",") + 2);
            Location l = LocationHelper.deserializeLocation(coords);
            p.teleport(l);
            p.sendMessage("Warped to " + warpName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
