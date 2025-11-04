package okey_boomer.teleportationcommands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class DeathListener implements Listener {
    private ComponentLogger LOGGER;

    public DeathListener(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        File homes = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + pde.getEntity().getName());
        try {
            homes.createNewFile();
            BufferedReader bfr = new BufferedReader(new FileReader(homes));
            String allHomes = "death " + pde.getEntity().getLocation().serialize() + "\n";
            String line = bfr.readLine();
            while (line != null) {
                allHomes += !line.startsWith("death {") ? line + "\n" : "";
                line = bfr.readLine(); 
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter(homes));
            bfw.write(allHomes);
            bfw.flush();
            bfw.close();
            LOGGER.info("Player: " + pde.getEntity().getName() + " died at: " + pde.getEntity().getLocation());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
