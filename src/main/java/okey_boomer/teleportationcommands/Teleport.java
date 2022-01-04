package okey_boomer.teleportationcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Teleport implements CommandExecutor {
    private final Plugin plugin;

    private final Counter counter;

    private Timer timer;

    private String separator;

    private boolean ready;

    private Player otherPlayer;

    public Teleport(Plugin plugin) {
        this.plugin = plugin;
        counter = new Counter();
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
        ready = false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            World w = p.getWorld();

            boolean found = false;
            for (Player player : w.getPlayers()) {
                if (player.getName().equals(args[0])) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat"));
                String line = bfr.readLine();
                while (line != null) {
                    if (line.substring(0, line.indexOf(",")).equals(p.getName())
                            && line.substring(line.indexOf(",") + 2, line.lastIndexOf(",")).equals(args[0])) {
                        return false;
                    } else {
                        line = bfr.readLine();
                    }
                }
                BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat", true));
                bfw.write(p.getName() + ", " + args[0] + ", false");
                bfw.flush();
                bfw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            timer = new Timer(100, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (counter.getCounter() <= 600) {
                            counter.increment();
                            BufferedReader bfr = new BufferedReader(new FileReader("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat"));
                            ArrayList<String> data = new ArrayList<>();
                            String line = bfr.readLine();
                            while (line != null) {
                                data.add(line);
                                line = bfr.readLine();
                            }
                            bfr.close();
                            boolean removed = false;
                            boolean accepted = false;
                            for (int i = 0; i < data.size(); i++) {
                                if (data.get(i).substring(0, data.get(i).indexOf(",")).equals(p.getName())) {
                                    String other = data.get(i).substring(data.get(i).indexOf(",") + 2, data.get(i).lastIndexOf(","));
                                    if (other.equals(args[0])) {
                                        if (data.get(i).substring(data.get(i).lastIndexOf(",") + 2).equals("accepted")) {
                                            removed = true;
                                            accepted = true;
                                            data.remove(i);
                                            break;
                                        } else if (data.get(i).substring(data.get(i).lastIndexOf(",") + 2).equals("declined")) {
                                            removed = true;
                                            data.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                            if (removed) {
                                BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat"));
                                String out = "";
                                for (String s : data) {
                                    out += s + "\n";
                                }
                                bfw.write(out);
                                bfw.flush();
                                bfw.close();
                                List<Player> players = w.getPlayers();
                                for (Player player : players) {
                                    if (player.getName().equals(args[0])) {
                                        if (accepted) {
                                            otherPlayer = player;
                                            ready = true;
                                            p.sendMessage("Teleported Successfully!");
                                        } else {
                                            p.sendMessage("Teleport Request was Declined!");
                                        }
                                        break;
                                    }
                                }
                                stopTimer();
                            }
                        } else {
                            stopTimer();
                            try {
                                BufferedReader bfr = new BufferedReader(new FileReader("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat"));
                                ArrayList<String> data = new ArrayList<>();
                                String line = bfr.readLine();
                                while (line != null) {
                                    data.add(line);
                                    line = bfr.readLine();
                                }
                                bfr.close();
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).substring(0, data.get(i).indexOf(",")).equals(p.getName())) {
                                        String other = data.get(i).substring(data.get(i).indexOf(",") + 2, data.get(i).lastIndexOf(","));
                                        if (other.equals(args[0])) {
                                            if (data.get(i).substring(data.get(i).lastIndexOf(",") + 2).equals("accepted")) {
                                                data.remove(i);
                                                break;
                                            }
                                        }
                                    }
                                }
                                BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat"));
                                String out = "";
                                for (String s : data) {
                                    out += s + "\n";
                                }
                                bfw.write(out);
                                bfw.flush();
                                bfw.close();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            p.sendMessage("Teleport Request Timed Out");
                        }
                    } catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            });
            timer.start();
            while (!ready) {

            }
            if (ready) {
                p.teleport(p.getLocation());
            }
            return true;
        }
        return false;
    }

    public void stopTimer() {
        timer.stop();
    }

    private class Counter {
        private int counter;

        public Counter() {
            counter = 0;
        }

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }
}