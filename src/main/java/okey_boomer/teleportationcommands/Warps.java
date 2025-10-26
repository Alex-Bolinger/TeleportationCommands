package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Warps implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat"));
            String line = bfr.readLine();
            String out = "";
            while (line != null) {
                out += line.substring(0, line.indexOf(",")) + "\n";
                line = bfr.readLine();
            }
            bfr.close();
            if (out.length() > 0) {
                out = out.substring(0, out.length() - 1);
            }
            sender.sendMessage("Warps List: \n" + out);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
