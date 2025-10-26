package okey_boomer.teleportationcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;
import java.util.*;

public class SetHome implements CommandExecutor {
    private ComponentLogger LOGGER;

    public SetHome(JavaPlugin plugin)
    { 
        LOGGER = plugin.getComponentLogger();
    }

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
            for (String h1 : homes) {
                bfw.write(h1 + "\n");
                bfw.flush();
            }
            Player p = (Player) sender;
            Location l = p.getLocation();
            bfw.write(args[0].toLowerCase() + " " + l.serialize());
            bfw.flush();
            bfw.close();
            p.sendMessage("Successfully set home: " + args[0].toLowerCase() + " at " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ());
            LOGGER.info("Player: " + p.getName() + "set home at: " + l.serialize());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
