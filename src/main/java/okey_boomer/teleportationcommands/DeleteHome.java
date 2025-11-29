package okey_boomer.teleportationcommands;

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

import java.io.*;

public class DeleteHome implements CommandExecutor {
    private ComponentLogger LOGGER;

    public DeleteHome(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String homeName;
        if (args.length == 0) {
            homeName = "home";
        } else {
            homeName = args[0];
        }
        Player p = (Player) sender;
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
                p.sendMessage("§cNo homes set!");
                return true;
            }
            if (homes.getHome(homeName) == null) {
                p.sendMessage("§cHome: " + homeName + " not found");
                return true;
            }
            homes.deleteHome(homeName);
            BufferedWriter bfw = new BufferedWriter(new FileWriter(home));
            bfw.write(gson.toJson(homes));
            bfw.flush();
            bfw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        sender.sendActionBar(Component.text("Successfully deleted home: " + homeName));
        LOGGER.info("Player: " + p.getName() + " deleted home: " + homeName);
        return true;
    }
}
