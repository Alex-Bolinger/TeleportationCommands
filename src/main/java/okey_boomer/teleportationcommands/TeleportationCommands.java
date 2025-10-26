package okey_boomer.teleportationcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class TeleportationCommands extends JavaPlugin {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Player p = (Player) sender;
        World w = p.getWorld();
        if (command.getName().equals("warp") || command.getName().equals("deleteWarp")) {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat"));
                String line = bfr.readLine();
                while (line != null) {
                    String item = line.substring(0, line.indexOf(','));
                    if (args.length != 0) {
                        if (item.startsWith(args[0])) {
                            list.add(line.substring(0, line.indexOf(',')));
                        }
                    } else {
                        list.add(line.substring(0, line.indexOf(',')));
                    }
                    line = bfr.readLine();
                }
                bfr.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else if (command.getName().equals("tpa")) {
            for (Player player : w.getPlayers()) {
                if (!p.equals(player)) {
                    if (args.length != 0) {
                        if (player.getName().startsWith(args[0])) {
                            list.add(player.getName());
                        }
                    } else {
                        list.add(player.getName());
                    }
                }
            }
        } else if (command.getName().equals("home") || command.getName().equals("deleteHome")) {
            File home = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + sender.getName());
            if (home.exists()) {
                try {
                    BufferedReader bfr = new BufferedReader(new FileReader(home));
                    String line = bfr.readLine();
                    while (line != null) {
                        String item = line.substring(0, line.indexOf(' '));
                        if (args.length != 0) {
                            if (item.startsWith(args[0])) {
                                list.add(line.substring(0, line.indexOf(' ')));
                            }
                        } else {
                            list.add(line.substring(0, line.indexOf(' ')));
                        }
                        line = bfr.readLine();
                    }
                    bfr.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } else if (command.getName().equals("tpaccept") || command.getName().equals("tpdecline")) {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
                String line = bfr.readLine();
                while (line != null) {
                    String[] parts = line.split(", ");
                    if (parts[1].equals(p.getName()) && parts[2].equals("false")) {
                        if (args.length != 0) {
                            if (parts[0].startsWith(args[0])) {
                                list.add(parts[0]);
                            }
                        } else {
                            list.add(parts[0]);
                        }
                    }
                }
                bfr.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        Spawn s = new Spawn(this);
        Teleport t = new Teleport(this);
        TPAccept tpAccept = new TPAccept(this);
        TPDecline tpDecline = new TPDecline(this);
        SetWarp setWarp = new SetWarp(this);
        Warp warp = new Warp(this);
        DeleteWarp deleteWarp = new DeleteWarp(this);
        Warps warpsCommand = new Warps(this);
        Home home = new Home(this);
        SetHome setHome = new SetHome(this);
        DeleteHome deleteHome = new DeleteHome(this);
        Homes homes = new Homes(this);
        File activeTeleportations = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat");
        if (!activeTeleportations.exists()) {
            try {
                activeTeleportations.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        File warps = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "warps.dat");
        if (!warps.exists()) {
            try {
                warps.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("permissions.yml"));
            if (bfr.readLine() == null) {
                bfr.close();
                BufferedWriter bfw = new BufferedWriter(new FileWriter("permissions.yml"));
                bfw.write("permissions:\n" +
                        "    warpOp:\n" +
                        "        description: allows user to set warps\n" +
                        "        default: op\n");
                bfw.flush();
                bfw.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        File homesDir = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes");
        if (!homesDir.exists()) {
            homesDir.mkdir();
        }
        this.getCommand("spawn").setExecutor(s);
        this.getCommand("tpa").setExecutor(t);
        this.getCommand("tpaccept").setExecutor(tpAccept);
        this.getCommand("tpdecline").setExecutor(tpDecline);
        this.getCommand("setWarp").setExecutor(setWarp);
        this.getCommand("warp").setExecutor(warp);
        this.getCommand("deleteWarp").setExecutor(deleteWarp);
        this.getCommand("warps").setExecutor(warpsCommand);
        this.getCommand("home").setExecutor(home);
        this.getCommand("setHome").setExecutor(setHome);
        this.getCommand("deleteHome").setExecutor(deleteHome);
        this.getCommand("homes").setExecutor(homes);

        this.getCommand("spawn").setTabCompleter(this);
        this.getCommand("tpa").setTabCompleter(this);
        this.getCommand("tpaccept").setTabCompleter(this);
        this.getCommand("tpdecline").setTabCompleter(this);
        this.getCommand("setWarp").setTabCompleter(this);
        this.getCommand("warp").setTabCompleter(this);
        this.getCommand("deleteWarp").setTabCompleter(this);
        this.getCommand("warps").setTabCompleter(this);
        this.getCommand("home").setTabCompleter(this);
        this.getCommand("setHome").setTabCompleter(this);
        this.getCommand("deleteHome").setTabCompleter(this);
        this.getCommand("homes").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + File.separator + "TeleportationCommands" + File.separator + "activeTeleportations.dat"));
            bfw.write("");
            bfw.flush();
            bfw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
