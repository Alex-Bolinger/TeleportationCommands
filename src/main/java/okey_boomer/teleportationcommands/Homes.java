package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class Homes implements CommandExecutor {
    private String separator;

    public Homes() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        File home = new File("plugins" + separator + "TeleportationCommands" + separator + "homes" + separator + sender.getName());
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(home));
            ArrayList<String> homes = new ArrayList<>();
            String h = bfr.readLine();
            while (h != null) {
                homes.add(h.substring(0, h.indexOf(' ')));
                h = bfr.readLine();
            }
            if (homes.size() == 0) {
                sender.sendMessage("No homes found");
                return true;
            }
            sender.sendMessage("Homes\n_________________________");
            for (String h1 : homes) {
                sender.sendMessage(h1);
            }
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
