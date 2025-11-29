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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Teleport implements CommandExecutor {
    private final JavaPlugin plugin;
    private Player otherPlayer;

    public Teleport(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Please specify player you would like to teleport to!");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.getName().equals(args[0])) {
                p.sendActionBar(Component.text("bruh"));
                return true;
            }
            otherPlayer = null;
            boolean found = false;
            ArrayList<Player> allPlayers = new ArrayList<>();
            for (World world : Bukkit.getServer().getWorlds()) {
                allPlayers.addAll(world.getPlayers());
            }
            for (Player player : allPlayers) {
                if (player.getName().equals(args[0])) {
                    otherPlayer = player;
                    found = true;
                }
            }
            if (!found) {
                p.sendMessage("§cOther player not found");
                return true;
            }
            File f = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "teleport" + File.separator + p.getName() + ".json");
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
                    p.sendMessage("§cError sending teleport request");
                    return true;
                }
            } else {
                requests = new TeleportRequests();
                try {
                    f.createNewFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            final Integer id = requests.addPlayer(otherPlayer);
            try {
                BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
                bfw.write(gson.toJson(requests));
                bfw.flush();
                bfw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            p.sendActionBar(Component.text("Sent " + args[0] + " a teleport request"));
            otherPlayer.sendMessage(p.getName() + " sent you a teleport request");
            p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        if (f.exists()) {
                            StringBuilder in = new StringBuilder();
                            BufferedReader bfr = new BufferedReader(new FileReader(f));
                            String line = bfr.readLine();
                            while (line != null) {
                                in.append(line);
                                line = bfr.readLine();
                            }
                            bfr.close();
                            TeleportRequests tr = gson.fromJson(in.toString(), TeleportRequests.class);
                            if (tr.getRequest(id) != null) {
                                p.sendActionBar(Component.text("Teleport request to " + otherPlayer.getName() + " timed out"));
                                otherPlayer.sendActionBar(Component.text("Teleport request from " + otherPlayer.getName() + " timed out"));
                                tr.removeRequest(id);
                                if (!tr.isEmpty()) {
                                    BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
                                    bfw.write(gson.toJson(tr));
                                    bfw.flush();
                                    bfw.close();
                                } else {
                                    Files.delete(Paths.get(f.getAbsolutePath()));
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }, 1200);
            return true;
        }
        return false;
    }

}