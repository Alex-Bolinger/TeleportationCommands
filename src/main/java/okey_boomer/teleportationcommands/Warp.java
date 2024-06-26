package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Warp implements CommandExecutor {
    private String separator;

    public Warp() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {

            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + separator + "TeleportationCommands" + separator + "warps.dat"));
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
            Player p = (Player) sender;
            if (!found) {
                p.sendMessage("Invalid Warp");
                return true;
            }
            String coords = line.substring(line.indexOf(",") + 2);
            Location l = new Location(p.getWorld(), Double.parseDouble(coords.substring(0, coords.indexOf(" "))),
                    Double.parseDouble(coords.substring(coords.indexOf(" ") + 1, coords.lastIndexOf(" "))),
                    Double.parseDouble(coords.substring(coords.lastIndexOf(" ") + 1)));
            p.teleport(l);
            p.sendMessage("Warped to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
