package okey_boomer.teleportationcommands;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;

public class TeleportRequests {
    private HashMap<Integer,String> otherPlayers;

    public TeleportRequests() {
        otherPlayers = new HashMap<>();
    }

    public int addPlayer(Player otherPlayer) {
        Random r = new Random();
        int id = r.nextInt(0, Integer.MAX_VALUE);
        while (otherPlayers.containsKey(id)) {
            id = r.nextInt(0, Integer.MAX_VALUE);
        }
        otherPlayers.put(id, otherPlayer.getName());
        return id;
    }

    public String getRequest(int id) {
        return otherPlayers.get(id);
    }

    public boolean requestExists(Player otherPlayer) {
        return otherPlayers.values().contains(otherPlayer.getName());
    }

    public void removeRequest(Player otherPlayer) {
        otherPlayers.entrySet().removeIf(entry -> {
            return entry.getValue().equals(otherPlayer.getName());
        });
    }

    public void removeRequest(int id) {
        otherPlayers.remove(id);
    }

    public boolean isEmpty() {
        return otherPlayers.size() == 0;
    }
}
