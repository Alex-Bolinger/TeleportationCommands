package okey_boomer.teleportationcommands;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public final class TeleportationCommands extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        String separator;
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
        Spawn s = new Spawn(this);
        Teleport t = new Teleport(this);
        TPAccept tpAccept = new TPAccept(this);
        TPDecline tpDecline = new TPDecline(this);
        SetWarp setWarp = new SetWarp();
        Warp warp = new Warp();
        DeleteWarp deleteWarp = new DeleteWarp();
        File activeTeleportations = new File("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat");
        if (!activeTeleportations.exists()) {
            try {
                activeTeleportations.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        File warps = new File("plugins" + separator + "TeleportationCommands" + separator + "warps.dat");
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
        this.getCommand("spawn").setExecutor(s);
        this.getCommand("tpa").setExecutor(t);
        this.getCommand("tpaccept").setExecutor(tpAccept);
        this.getCommand("tpdecline").setExecutor(tpDecline);
        this.getCommand("setWarp").setExecutor(setWarp);
        this.getCommand("warp").setExecutor(warp);
        this.getCommand("deleteWarp").setExecutor(deleteWarp);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        String separator;
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("plugins" + separator + "TeleportationCommands" + separator + "activeTeleportations.dat"));
            bfw.write("");
            bfw.flush();
            bfw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
