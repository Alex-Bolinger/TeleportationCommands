package okey_boomer.teleportationcommands;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

public class TeleportHelper {
    public static Location deserializeLocation(String savedLocation) {
        savedLocation = savedLocation.substring(1, savedLocation.length() - 1);
        HashMap<String, Object> lMap = new HashMap<String, Object>();
        StringTokenizer tokenizer = new StringTokenizer(savedLocation, ", ");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            double doubleValue;
            try {
                doubleValue = Double.parseDouble(keyValue[1]);
                lMap.put(keyValue[0], doubleValue);
            }
            catch (NumberFormatException nfe) {
                lMap.put(keyValue[0],keyValue[1]);
            }
        }
        return Location.deserialize(lMap);
    }

    public static boolean teleport(Player player, Location location) {
        Vehicle vehicle = (Vehicle) player.getVehicle();
        if (vehicle != null) {
            vehicle.removePassenger(player);
            vehicle.teleport(location);
            player.teleport(location);
            vehicle.addPassenger(player);
            return true;
        } else {
            return player.teleport(location);
        }
    }
}
