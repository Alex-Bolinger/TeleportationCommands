package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Homes implements CommandExecutor {
    private ComponentLogger LOGGER;

    public Homes(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        int pageNum;
        if (args.length > 0) {
            try {
                pageNum = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                sender.sendMessage("§cFirst input must be blank or a number");
                return true;
            }
            if (pageNum < 1) {
                sender.sendMessage("§cPage number must be greater than or equal to 1");
                return true;
            }
        } else {
            pageNum = 1;
        }
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
                homes = new HomesData();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            homes = new HomesData();
        }

        ArrayList<String> homeList = new ArrayList<>();
        homes.getAllHomes().forEach(entry -> {
            if (!entry.getKey().equals("death")) {
                homeList.add(entry.getKey());
            }
        });
        Collections.sort(homeList);
        int count = homeList.size();
        int pages = (int)Math.ceil((double)count / 6.0);
        if (pageNum > pages) {
            p.sendMessage("§cInvalid page number");
            return true;
        }
        int last = Math.min(homeList.size(), 6 * pageNum);
        String out = "§nHomes (page " + pageNum + "/" + pages + ")§r";
        for (int i = 6 * (pageNum - 1); i < last; i++) {
            out += "\n" + homeList.get(i);
        }
        p.sendMessage(out);
        return true;
    }
}
