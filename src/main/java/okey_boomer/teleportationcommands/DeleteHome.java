package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;
import java.util.ArrayList;

public class DeleteHome implements CommandExecutor {
    private ComponentLogger LOGGER;

    public DeleteHome(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName());
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
            sender.sendMessage("Successfully deleted home: " + args[0]);
            LOGGER.info("Player: " + ((Player)sender).getName() + "deleted their home: " + args[0]);
        } else {
            sender.sendMessage("No home set");
        }
        return true;
    }
}
