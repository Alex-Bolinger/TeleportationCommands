package okey_boomer.teleportationcommands;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.bukkit.Location;

public class LocationHelper {
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
}
