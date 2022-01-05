package okey_boomer.teleportationcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SetHome implements CommandExecutor {
    private String separator;

    public SetHome() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            separator = "\\";
        } else {
            separator = "/";
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File home = new File("plugins" + separator + "TeleportationCommands" + separator + "homes" + separator + sender.getName());
        if (home.exists()) {
            sender.sendMessage("Home already set");
            return true;
        }
        try {
            home.createNewFile();
            BufferedWriter bfw = new BufferedWriter(new FileWriter(home));
            Player p = (Player) sender;
            Location l = p.getLocation();
            bfw.write(l.getX() + " " + l.getY() + " " + l.getZ());
            bfw.flush();
            bfw.close();
            p.sendMessage("Successfully set home at " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
}
