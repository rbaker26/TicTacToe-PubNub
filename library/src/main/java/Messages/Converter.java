package Messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

final public class Converter {
    static private Gson gson;

    private Converter() {}

    static {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    /**
     * Converts something from JSON.
     * @param json The JSON object.
     * @param typeOfT The type to convert it to. Specify this by using T.class.
     * @param <T> Again, the type of convert it to.
     * @return The converted object.
     */
    public static <T> T fromJson(JsonElement json, Class<T> typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    /**
     * Converts something to JSON. Probably don't need this, considering that
     * Pubnub's functions will typically do it for you.
     * @param obj Object to convert.
     * @return The converted object.
     */
    public static JsonElement toJson(Object obj) {
        return gson.toJsonTree(obj);
    }


}
