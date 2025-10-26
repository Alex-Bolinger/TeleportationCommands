package okey_boomer.teleportationcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class Teleport implements CommandExecutor {
    private final Plugin plugin;
    private Player otherPlayer;

    public Teleport(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            World w = p.getWorld();
            otherPlayer = null;
            boolean found = false;
            for (Player player : w.getPlayers()) {
                if (player.getName().equals(args[0])) {
                    otherPlayer = player;
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
                String line = bfr.readLine();
                while (line != null) {
                    if (line.substring(0, line.indexOf(",")).equals(p.getName())
                            && line.substring(line.indexOf(",") + 2, line.lastIndexOf(",")).equals(args[0])
                            && line.substring(line.lastIndexOf(",") + 2).equals("false")) {
                                bfr.close();
                        return false;
                    } else {
                        line = bfr.readLine();
                    }
                }
                bfr.close();
                BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat", true));
                bfw.write(p.getName() + ", " + args[0] + ", false");
                bfw.flush();
                bfw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            p.sendMessage("Sent " + args[0] + " a teleport request");
            otherPlayer.sendMessage(p.getName() + " sent you a teleport request");
            p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
                        ArrayList<String> data = new ArrayList<>();
                        String line = bfr.readLine();
                        while (line != null) {
                            data.add(line);
                            if (line.substring(0, line.indexOf(",")).equals(p.getName())
                                    && line.substring(line.indexOf(",") + 2, line.lastIndexOf(",")).equals(args[0])) {
                                bfr.close();
                                data.remove(data.size() - 1);
                                BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
                                String out = "";
                                for (String s : data) {
                                    out += s + "\n";
                                }
                                bfw.write(out);
                                bfw.flush();
                                bfw.close();
                                if (line.substring(line.lastIndexOf(",") + 2).equals("false")) {
                                    p.sendMessage("Teleport request to " + args[0] + " timed out");
                                    otherPlayer.sendMessage("Teleport request from " + args[0] + " timed out");
                                }
                                break;
                            }
                        }
                        bfr.close();
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