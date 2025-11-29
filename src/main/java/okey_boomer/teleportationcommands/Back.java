package okey_boomer.teleportationcommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class Back implements CommandExecutor{
    private ComponentLogger LOGGER;

    public Back(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        String homeName = "death";
        Player p = (Player) sender;
        File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + p.getName() + ".json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(HomesData.class, new HomesData.HomesDataSerializer());
        gsonBuilder.registerTypeAdapter(HomesData.class, new HomesData.HomesDataDeserializer());
        Gson gson = gsonBuilder.create();
        HomesData homes;
        try {
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
                sender.sendMessage("§cNo homes set");
                return true;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            homes = new HomesData();
        }
        if (homes.getHome(homeName) == null) {
            p.sendMessage("§cDeath location not found");
            return true;
        }
        Location l = homes.getHome(homeName);
        TeleportHelper.teleport(p, l);
        p.sendActionBar(Component.text("Teleported to last death location"));
        
        return true;
    }
}
