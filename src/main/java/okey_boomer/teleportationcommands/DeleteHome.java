package okey_boomer.teleportationcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DeleteHome implements CommandExecutor {
    private String separator;

    public DeleteHome() {
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
            home.delete();
            sender.sendMessage("Successfully deleted home");
        } else {
            sender.sendMessage("No home set");
        }
        return true;
    }
}
