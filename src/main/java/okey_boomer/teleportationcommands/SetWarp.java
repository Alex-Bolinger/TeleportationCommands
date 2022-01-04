package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Locale;

public class SetWarp implements CommandExecutor {
    private String separator;

    public SetWarp() {
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
            String line = bfr.readLine();
            Player p = (Player) sender;
            String warpName = args[0];
            for (int i = 1; i < args.length; i++) {
                warpName += " " + args[i];
            }
            warpName = warpName.toLowerCase();
            while (line != null) {
                String otherWarp = line.substring(0, line.indexOf(","));
                if (otherWarp.toLowerCase().equals(warpName)) {
                    p.sendMessage("Warp name: " + warpName + " is already taken");
                    return true;
                } else {
                    line = bfr.readLine();
                }
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + separator + "TeleportationCommands" + separator + "warps.dat", true));
            Location l = p.getLocation();
            bfw.write(warpName + ", " + l.getX() + " " + l.getY() + " " + l.getZ() + "\n");
            bfw.flush();
            bfw.close();
            p.sendMessage("Set warp " + warpName +  " at " + l.getX() + ", " + l.getY() + ", " + l.getZ());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
