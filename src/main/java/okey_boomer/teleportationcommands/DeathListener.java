package okey_boomer.teleportationcommands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class DeathListener implements Listener {
    private ComponentLogger LOGGER;

    public DeathListener(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        String homeName = "death";
        Player p = pde.getPlayer();
        Location l = p.getLocation();
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + p.getName() + ".json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(HomesData.class, new HomesData.HomesDataSerializer());
        gsonBuilder.registerTypeAdapter(HomesData.class, new HomesData.HomesDataDeserializer());
        Gson gson = gsonBuilder.create();
        try {
            HomesData homes;
            if (home.exists()) {
                StringBuilder in = new StringBuilder();
                BufferedReader bfr = new BufferedReader(new FileReader(home));
                String line = bfr.readLine();
                while (line != null) {
                    in.append(line);
                    line = bfr.readLine();
                }
                homes = gson.fromJson(in.toString(), HomesData.class);
                bfr.close();
            } else {
                home.createNewFile();
                homes = new HomesData();
            }
            if (homes.getHome(homeName) != null) {
                homes.deleteHome(homeName);
            }
            homes.addHome(homeName, l);
            BufferedWriter bfw = new BufferedWriter(new FileWriter(home));
            bfw.write(gson.toJson(homes));
            bfw.flush();
            bfw.close();
            LOGGER.info("Player: " + p.getName() + " set death location at: " + l.serialize());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
