package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;

public class DeleteWarp implements CommandExecutor {
    private ComponentLogger LOGGER;

    public DeleteWarp(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat"));
            String line = bfr.readLine();
            Player p = (Player) sender;
            String warpName = args[0];
            for (int i = 1; i < args.length; i++) {
                warpName += " " + args[i];
            }
            warpName = warpName.toLowerCase();
            String out = "";
            boolean found = false;
            while (line != null) {
                String otherWarp = line.substring(0, line.indexOf(","));
                if (otherWarp.toLowerCase().equals(warpName)) {
                    found = true;
                    line = bfr.readLine();
                } else {
                    out += line + "\n";
                    line = bfr.readLine();
                }
            }
            bfr.close();
            if (!found) {
                return false;
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat"));
            bfw.write(out);
            bfw.flush();
            bfw.close();
            p.sendMessage("Successfully deleted warp:" + warpName);
            LOGGER.info("Player: " + ((Player)sender).getName() + "deleted warp: " + args[0]);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
