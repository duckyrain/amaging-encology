package cn.amaging.encology.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by DuQiyu on 2019/9/16 10:40.
 */
public class JsonUtil {

    private static final Gson gson = new Gson();

    private static final JsonParser jsonParser = new JsonParser();

    private JsonUtil() {
    }

    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

    public static String toJsonString(Object object, Type typeOfT) {
        return gson.toJson(object, typeOfT);
    }

    public static Object fromJsonString(String json) {
        return gson.fromJson(json, Object.class);
    }

    public static <T> T fromJsonString(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromMap(Map<String, Object> map, Class<T> type) {
        return gson.fromJson(gson.toJson(map), type);
    }

    public static Map toMap(Object object) {
        return gson.fromJson(gson.toJson(object), Map.class);
    }

    public static JsonObject toJsonObject(String jsonStringObject)
            throws JsonSyntaxException, IllegalStateException {
        return jsonParser.parse(jsonStringObject).getAsJsonObject();
    }

    public static JsonArray toJsonArray(String jsonStringArray)
            throws JsonSyntaxException, IllegalStateException {
        return jsonParser.parse(jsonStringArray).getAsJsonArray();
    }

    public static JsonElement toJsonElement(String jsonStringElement)
            throws JsonSyntaxException, IllegalStateException {
        return jsonParser.parse(jsonStringElement);
    }

    public static JsonElement jsonElementFrom(String jsonString) {
        return jsonParser.parse(jsonString);
    }

    public static boolean isJsonObject(String jsonStringObject) {
        try {
            return toJsonObject(jsonStringObject).isJsonObject();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static boolean isJsonArray(String jsonStringArray) {
        try {
            return toJsonArray(jsonStringArray).isJsonArray();
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
