package okey_boomer.teleportationcommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;
import java.util.ArrayList;

public class TPAccept implements CommandExecutor {
    private ComponentLogger LOGGER;

    public TPAccept(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0] == null) {
            sender.sendMessage("Please specify player whose request you are accepting!");
            return true;
        }
        Player p = (Player) sender;
        Player otherPlayer = null;
        boolean found = false;
        ArrayList<Player> allPlayers = new ArrayList<>();
        for (World world : Bukkit.getServer().getWorlds()) {
            allPlayers.addAll(world.getPlayers());
        }
        for (Player player : allPlayers) {
            if (player.getName().equals(args[0])) {
                found = true;
                otherPlayer = player;
            }
        }
        if (!found) {
            sender.sendMessage("Other player not found");
            return true;
        }
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
            ArrayList<String> data = new ArrayList<>();
            String line = bfr.readLine();
            if (line == null) {
                bfr.close();
                return true;
            }
            while (line != null) {
                data.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            String out = "";
            found = false;
            for (String s : data) {
                if (s.substring(0, s.indexOf(",")).equals(args[0])
                        && s.substring(s.indexOf(",") + 2, s.lastIndexOf(",")).equals(p.getName())) {
                    s = s.substring(0, s.lastIndexOf(",")) + ", accepted";
                    found = true;
                }
                out += s + "\n";
            }
            if (!found) {
                sender.sendMessage("Teleport request not found");
                return true;
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
            bfw.write(out);
            bfw.flush();
            bfw.close();
            p.sendMessage("Accepted " + args[0] + "'s teleport request");
            TeleportHelper.teleport(otherPlayer, p.getLocation());
            otherPlayer.sendMessage(p.getName() + " accepted your teleport request");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
