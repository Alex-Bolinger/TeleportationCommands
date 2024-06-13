package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class DeleteHome implements CommandExecutor {
    private String separator;

    public DeleteHome() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File home = new File("plugins" + separator + "TeleportationCommands" + separator + "homes" + separator + sender.getName());
        if (home.exists()) {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(home));
                ArrayList<String> homes = new ArrayList<>();
                String h = bfr.readLine();
                boolean found = false;
                while (h != null) {
                    if (!h.substring(0, h.indexOf(' ')).equals(args[0].toLowerCase())) {
                        homes.add(h);
                    } else {
                        found = true;
                    }
                    h = bfr.readLine();
                }
                bfr.close();
                if (found) {
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(home));
                    String out = "";
                    for (String h1 : homes) {
                        out += h1 + "\n";
                    }
                    bwr.write(out);
                    bwr.flush();
                    bwr.close();
                } else {
                    sender.sendMessage("Home: " + args[0].toLowerCase() + " not found.");
                    return true;
                }
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            sender.sendMessage("Successfully deleted home");
        } else {
            sender.sendMessage("No home set");
        }
        return true;
    }
}
