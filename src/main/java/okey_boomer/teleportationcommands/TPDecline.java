package okey_boomer.teleportationcommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TPDecline implements CommandExecutor {
    private ComponentLogger LOGGER;

    public TPDecline(JavaPlugin plugin) {
        LOGGER = plugin.getComponentLogger();
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cPlease specify player whose request you are declining!");
            return true;
        }
        Player p = (Player) sender;
        Player otherPlayer = null;
        boolean found = false;
        ArrayList<Player> allPlayers = new ArrayList<>();
        for (World world : Bukkit.getServer().getWorlds()) {
            allPlayers.addAll(world.getPlayers());
        }
        for (Player player : allPlayers) {
            if (player.getName().equals(args[0])) {
                found = true;
                otherPlayer = player;
            }
        }
        File f = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "teleport" + File.separator + otherPlayer.getName() + ".json");
        TeleportRequests requests;
        Gson gson = new Gson();
        if (f.exists()) {
            try {
                StringBuilder in = new StringBuilder();
                BufferedReader bfr = new BufferedReader(new FileReader(f));
                String line = bfr.readLine();
                while (line != null) {
                    in.append(line);
                    line = bfr.readLine();
                }
                bfr.close();
                requests = gson.fromJson(in.toString(), TeleportRequests.class);
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage("§cError declining teleport request");
                return true;
            }
        } else {
            p.sendMessage("§cNo active teleportation requests found");
            return true;
        }

        if (requests.requestExists(p)) {
            requests.removeRequest(p);
            try {
                if (requests.isEmpty()) {
                    Files.delete(Paths.get(f.getAbsolutePath()));
                } else {
                    BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
                    bfw.write(gson.toJson(requests));
                    bfw.flush();
                    bfw.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            p.sendActionBar(Component.text("§cTeleport request from " + otherPlayer.getName() + " not found"));
            return true;
        }

        p.sendActionBar(Component.text("Declined " + args[0] + "'s teleport request"));
        otherPlayer.sendMessage("§c" + p.getName() + " declined your teleport request");
        return true;
    }
}
