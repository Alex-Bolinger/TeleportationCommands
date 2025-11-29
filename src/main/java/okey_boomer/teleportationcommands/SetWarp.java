package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;

public class SetWarp implements CommandExecutor {
    private ComponentLogger LOGGER;

    public SetWarp(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0) {
                sender.sendMessage("§cPlease specify warp name");
                return true;
            }
            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat"));
            String line = bfr.readLine();
            Player p = (Player) sender;
            String warpName = args[0];
            for (int i = 1; i < args.length; i++) {
                warpName += " " + args[i];
            }
            while (line != null) {
                String otherWarp = line.substring(0, line.indexOf(","));
                if (otherWarp.equals(warpName)) {
                    p.sendMessage("§cWarp name: " + warpName + " is already taken");
                    bfr.close();
                    return true;
                } else {
                    line = bfr.readLine();
                }
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat", true));
            Location l = p.getLocation();
            bfw.write(warpName + ", " + l.serialize() + "\n");
            bfw.flush();
            bfw.close();
            p.sendMessage("Set warp " + warpName +  " at " + l.getX() + ", " + l.getY() + ", " + l.getZ());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
