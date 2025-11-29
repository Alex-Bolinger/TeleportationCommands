package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class TeleportationCommands extends JavaPlugin {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Player p = (Player) sender;
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
            ArrayList<Player> allPlayers = new ArrayList<>();
            for (World world : getServer().getWorlds()) {
                allPlayers.addAll(world.getPlayers());
            }
            for (Player player : allPlayers) {
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

            homes.getAllHomes().forEach(entry -> {
                if (!entry.getKey().equals("death") && entry.getKey().startsWith(args[0])) {
                    list.add(entry.getKey());
                }
            });
            Collections.sort(list);
        } else if (command.getName().equals("tpaccept") || command.getName().equals("tpdecline")) {
            ArrayList<Player> allPlayers = new ArrayList<>();
            for (World world : getServer().getWorlds()) {
                allPlayers.addAll(world.getPlayers());
            }

            for (Player player : allPlayers) {
                if (player.getName().startsWith(args[0])) {
                    try {
                        File f = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "teleport" + File.separator + player.getName() + ".json");
                        TeleportRequests requests;
                        Gson gson = new Gson();
                        if (f.exists()) {
                            StringBuilder in = new StringBuilder();
                            BufferedReader bfr = new BufferedReader(new FileReader(f));
                            String line = bfr.readLine();
                            while (line != null) {
                                in.append(line);
                                line = bfr.readLine();
                            }
                            bfr.close();
                            requests = gson.fromJson(in.toString(), TeleportRequests.class);
                            if (requests.requestExists(p)) {
                                list.add(player.getName());
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
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
        Back back = new Back(this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
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
        } else {
            File[] homeFiles = homesDir.listFiles();
            for (File f : homeFiles) {
                if (f.isFile() && !f.getAbsolutePath().contains(".")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(HomesData.class, new HomesData.HomesDataSerializer());
                    gsonBuilder.registerTypeAdapter(HomesData.class, new HomesData.HomesDataDeserializer());
                    Gson gson = gsonBuilder.create();
                    String name = f.getName();
                    HomesData homesData = new HomesData();
                    try {
                        File newHome = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "homes" + File.separator + name + ".json");
                        BufferedReader bfr = new BufferedReader(new FileReader(f));
                        String line = bfr.readLine();
                        while (line != null) {
                            String homeName = line.substring(0, line.indexOf("{") - 1);
                            Location l = TeleportHelper.deserializeLocation(line.substring(line.indexOf("{")));
                            homesData.addHome(homeName, l);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        BufferedWriter bfw = new BufferedWriter(new FileWriter(newHome));
                        bfw.write(gson.toJson(homesData));
                        bfw.flush();
                        bfw.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
        File tpDir = new File("plugins" + File.separator + "TeleportationCommands" + File.separator + "teleport");
        if (!tpDir.exists()) {
            tpDir.mkdir();
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
        this.getCommand("back").setExecutor(back);

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
        this.getCommand("back").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            Files.walk(Paths.get("plugins" + File.separator + "TeleportationCommands" + File.separator + "teleport"))
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException ioe) {
                        this.getComponentLogger().error("Error deleting: " + path);
                    }
                });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
