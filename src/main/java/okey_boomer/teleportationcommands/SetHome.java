package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class SetHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName());
        try {
            home.createNewFile();
            BufferedReader bfr = new BufferedReader(new FileReader(home));
            ArrayList<String> homes = new ArrayList<>();
            String h = bfr.readLine();
            while (h != null) {
                if (h.substring(0,h.indexOf(" ")).equals(args[0].toLowerCase())) {
                    sender.sendMessage("Home: " + args[0] + " already exists");
                    bfr.close();
                    return true;
                }
                homes.add(h);
                h = bfr.readLine();
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName()));
            Player p = (Player) sender;
            Location l = p.getLocation();
            for (String h1 : homes) {
                bfw.write(h1 + "\n");
                bfw.flush();
            }
            bfw.write(args[0].toLowerCase() + " " + l.getX() + " " + l.getY() + " " + l.getZ());
            bfw.flush();
            bfw.close();
            p.sendMessage("Successfully set home: " + args[0].toLowerCase() + " at " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
