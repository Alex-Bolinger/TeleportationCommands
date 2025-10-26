package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;
import java.util.ArrayList;

public class Homes implements CommandExecutor {
    private ComponentLogger LOGGER;

    public Homes(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName());
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(home));
            ArrayList<String> homes = new ArrayList<>();
            String h = bfr.readLine();
            while (h != null) {
                homes.add(h.substring(0, h.indexOf(' ')));
                h = bfr.readLine();
            }
            bfr.close();
            if (homes.size() == 0) {
                sender.sendMessage("No homes found");
                return true;
            }
            sender.sendMessage("Homes\n_________________________");
            for (String h1 : homes) {
                if (!h1.equals("death {")) {
                    sender.sendMessage(h1);
                }
            }
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
