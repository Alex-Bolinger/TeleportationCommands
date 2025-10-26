package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Home implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0] == null) {
            sender.sendMessage("Please specify a home to teleport to!");
            return true;
        }
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName());
        if (home.exists()) {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(home));
                String coord = bfr.readLine();
                boolean found = false;
                while (coord != null && !found) {
                    if (coord.substring(0, coord.indexOf(' ')).equals(args[0].toLowerCase())) {
                        coord = coord.substring(coord.indexOf(' ')+1);
                        found = true;
                    } else {
                        coord = bfr.readLine();
                    }
                }
                bfr.close();
                if (coord == null) {
                    sender.sendMessage("Home: " + args[0].toLowerCase() + " not found");
                    return true;
                }
                double x = Double.parseDouble(coord.substring(0, coord.indexOf(" ")));
                double y = Double.parseDouble(coord.substring(coord.indexOf(" ") + 1, coord.lastIndexOf(" ")));
                double z = Double.parseDouble(coord.substring(coord.lastIndexOf(" ") + 1));
                Player p = (Player) sender;
                p.teleport(new Location(p.getWorld(), x, y, z));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            sender.sendMessage("No home set");
        }
        return true;
    }
}
