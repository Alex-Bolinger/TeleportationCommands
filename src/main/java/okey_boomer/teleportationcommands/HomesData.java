package okey_boomer.teleportationcommands;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class HomesData {
    private HashMap<String, Location> homes;

    public HomesData() {
        homes = new HashMap<>();
    }

    public Set<Entry<String, Location>> getAllHomes() {
        return homes.entrySet();
    }

    public boolean addHome(String home, Location location) {
        if (getHome(home) != null) {
            return false;
        }
        homes.put(home, location);
        return true;
    }

    public Location getHome(String home) {
        return homes.get(home);
    }

    public void deleteHome(String home) {
        homes.remove(home);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("[");
        homes.entrySet().forEach(entry -> {
            out.append("{\"" + entry.getKey() + "\":{");
            entry.getValue().serialize().entrySet().forEach(set -> {
                out.append("\"" + set.getKey() + "\":");
                out.append("\"" + set.getValue() + "\",");
            });
            out.replace(out.length()-1, out.length(), "}}");
        });
        return out.append("]").toString();
    }

    public static class HomesDataSerializer implements JsonSerializer<HomesData> {

        @Override
        public JsonElement serialize(HomesData arg0, Type arg1, JsonSerializationContext arg2) {
            JsonObject object = new JsonObject();
            JsonArray arr = new JsonArray();
            arg0.homes.entrySet().forEach(entry -> {
                JsonObject home = new JsonObject();
                JsonObject location = new JsonObject();
                entry.getValue().serialize().entrySet().forEach(set -> {
                    location.addProperty(set.getKey(), set.getValue().toString());
                });
                home.add(entry.getKey(), location);
                arr.add(home);
            });
            object.add("homes", arr);
            return object;
        }
    }

    public static class HomesDataDeserializer implements JsonDeserializer<HomesData> {
        @Override
        public HomesData deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
                throws JsonParseException {
            HomesData homes = new HomesData();
            JsonArray homesList = arg0.getAsJsonObject().get("homes").getAsJsonArray();
            homesList.iterator().forEachRemaining(element -> {
                JsonObject object = element.getAsJsonObject();
                String home = object.entrySet().iterator().next().getKey();
                String location = object.entrySet()
                                        .iterator()
                                        .next()
                                        .getValue()
                                        .toString()
                                        .replaceAll(",", ", ")
                                        .replaceAll(":", "=")
                                        .replaceAll("\"", "");
                homes.addHome(home, TeleportHelper.deserializeLocation(location));
            });
            return homes;
        }
        
    }
}
