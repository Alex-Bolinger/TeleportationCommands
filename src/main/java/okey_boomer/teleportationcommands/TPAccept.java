package okey_boomer.teleportationcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class TPAccept implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        World w = p.getWorld();
        Player otherPlayer = null;
        boolean found = false;
        for (Player player : w.getPlayers()) {
            if (player.getName().equals(args[0])) {
                found = true;
                otherPlayer = player;
            }
        }
        if (!found) {
            return false;
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
                return false;
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
            bfw.write(out);
            bfw.flush();
            bfw.close();
            p.sendMessage("Accepted " + args[0] + "'s teleport request");
            otherPlayer.teleport(p.getLocation());
            otherPlayer.sendMessage(p.getName() + " accepted your teleport request");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
