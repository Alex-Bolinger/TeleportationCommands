package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class DeleteWarp implements CommandExecutor {
    private String separator;

    public DeleteWarp() {
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
            if (!found) {
                return false;
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + separator + "TeleportationCommands" + separator + "warps.dat"));
            bfw.write(out);
            bfw.flush();
            bfw.close();
            p.sendMessage("Successfully deleted " + warpName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
