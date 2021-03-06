package com.silverpine.uu.core;

import android.util.Base64;
import android.util.JsonReader;

import com.silverpine.uu.logging.UULog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * UUJson
 *
 * Useful Utilities - A set of extension methods for the JSONObject class.
 *
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class UUJson
{
    public static String safeGetString(final JSONObject json, final Object key)
    {
        return safeGetString(json, key, null);
    }

    public static boolean hasNonNullValueForKey(final JSONObject json, final Object key)
    {
        return (json != null && key != null && json.has(key.toString()) && !json.isNull(key.toString()));
    }

    public static String safeGetString(final JSONObject json, final Object key, final String defaultValue)
    {
        String  val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optString(key.toString(), defaultValue);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetString", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static long safeGetLong(final JSONObject json, final Object key)
    {
        return safeGetLong(json, key, 0);
    }

    //noinspection ConstantConditions
    public static long safeGetLong(final JSONObject json, final Object key, final long defaultValue)
    {
        long val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optLong(key.toString(), defaultValue);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetLong", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static int safeGetInt(final JSONObject json, final Object key)
    {
        return safeGetInt(json, key, 0);
    }

    public static int safeGetInt(final JSONObject json, final Object key, final int defaultValue)
    {
        int val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optInt(key.toString(), defaultValue);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetInt", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static boolean safeGetBool(final JSONObject json, final Object key)
    {
        return safeGetBool(json, key, false);
    }

    public static boolean safeGetBool(final JSONObject json, final Object key, final boolean defaultValue)
    {
        boolean val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optBoolean(key.toString(), defaultValue);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetBool", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static float safeGetFloat(final JSONObject json, final Object key)
    {
        return safeGetFloat(json, key, 0.0f);
    }

    public static float safeGetFloat(final JSONObject json, final Object key, final float defaultValue)
    {
        float val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = (float)json.optDouble(key.toString(), defaultValue);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetFloat", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static double safeGetDouble(final JSONObject json, final Object key)
    {
        return safeGetDouble(json, key, 0);
    }

    public static double safeGetDouble(final JSONObject json, final Object key, final double defaultValue)
    {
        double val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optDouble(key.toString(), defaultValue);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetDouble", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    @Nullable
    public static Long safeGetLongObject(final JSONObject json, final Object key)
    {
        return safeGetLong(json, key, null);
    }

    @Nullable
    public static Long safeGetLong(final JSONObject json, final Object key, @Nullable final Long defaultValue)
    {
        Long val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optLong(key.toString());
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetLongObject", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    @Nullable
    public static Integer safeGetIntObject(final JSONObject json, final Object key)
    {
        return safeGetIntObject(json, key, null);
    }

    @Nullable
    public static Integer safeGetIntObject(final JSONObject json, final Object key, @Nullable final Integer defaultValue)
    {
        Integer val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optInt(key.toString());
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetIntObject", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    @Nullable
    public static Boolean safeGetBoolObject(final JSONObject json, final Object key)
    {
        return safeGetBoolObject(json, key, null);
    }

    @Nullable
    public static Boolean safeGetBoolObject(final JSONObject json, final Object key, @Nullable final Boolean defaultValue)
    {
        Boolean val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optBoolean(key.toString());
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetBoolObject", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    @Nullable
    public static Float safeGetFloatObject(final JSONObject json, final Object key)
    {
        return safeGetFloatObject(json, key, null);
    }

    @Nullable
    public static Float safeGetFloatObject(final JSONObject json, final Object key, @Nullable final Float defaultValue)
    {
        Float val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = (float)json.optDouble(key.toString());
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetFloatObject", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    @Nullable
    public static Double safeGetDoubleObject(final JSONObject json, final Object key)
    {
        return safeGetDoubleObject(json, key, null);
    }

    @Nullable
    public static Double safeGetDoubleObject(final JSONObject json, final Object key, @Nullable final Double defaultValue)
    {
        Double val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.optDouble(key.toString());
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetDoubleObject", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static byte[] safeGetDataFromBase64String(final JSONObject json, final Object key)
    {
        return safeGetDataFromBase64String(json, key, null);
    }

    public static byte[] safeGetDataFromBase64String(final JSONObject json, final Object key, final byte[] defaultValue)
    {
        byte[] val = defaultValue;

        try
        {
            String base64String = safeGetString(json, key);
            if (base64String != null)
            {
                val = Base64.decode(base64String, Base64.NO_WRAP);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetDataFromBase64String", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static long safeGetDate(
            final JSONObject json,
            final Object key,
            final String[] formatters,
            final TimeZone timeZone)
    {
        return safeGetDate(json, key, 0, formatters, timeZone);
    }

    public static long safeGetDate(
            final JSONObject json,
            final Object key,
            final long defaultValue,
            final String[] formatters,
            final TimeZone timeZone)
    {
        long result = defaultValue;
        String str = safeGetString(json, key, null);

        if (str != null)
        {
            Date dt = UUDate.parseDate(str, timeZone, formatters);

            if (dt != null)
            {
                result = dt.getTime();
            }
        }

        return result;
    }

    @Nullable
    public static <T extends Enum<T>> T safeGetEnum(@NonNull final Class<T> type, final JSONObject json, final Object key)
    {
        return UUEnum.fromString(type, safeGetString(json, key));
    }

    @NonNull
    public static <T extends Enum<T>> T safeGetEnum(@NonNull final Class<T> type, final JSONObject json, final Object key, @NonNull final T defaultValue)
    {
        return UUEnum.fromString(type, safeGetString(json, key), defaultValue);
    }

    public static Object safeGet(final JSONObject json, final Object key)
    {
        return safeGet(json, key, null);
    }

    public static Object safeGet(final JSONObject json, final Object key, final Object defaultValue)
    {
        Object val = defaultValue;

        try
        {
            if (hasNonNullValueForKey(json, key))
            {
                val = json.opt(key.toString());
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGet", "key: " + key, ex);
            val = defaultValue;
        }

        return val;
    }

    public static JSONObject safeGetJsonObject(final JSONObject json, final Object key)
    {
        return safeGetObject(JSONObject.class, json, key);
    }

    public static JSONArray safeGetJsonArray(final JSONObject json, final Object key)
    {
        return safeGetObject(JSONArray.class, json, key);
    }

    public static Object safeGetJsonObject(final JSONArray json, final int index)
    {
        Object obj = null;

        try
        {
            if (json != null && json.length() > index)
            {
                obj = json.get(index);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safeGetJsonObject", ex);
            obj = null;
        }

        return obj;
    }

    public static <T extends Object> T safeGetObject(final Class<T> type, final JSONObject json, final Object key)
    {
        return safeGetObject(type, json, key, null);
    }

    public static <T extends Object> T safeGetObject(final Class<T> type, final JSONObject json, final Object key, final T defaultValue)
    {
        T val = defaultValue;

        try
        {
            Object obj = safeGet(json, key);
            if (obj != null && type != null)
            {
                if (obj.getClass().isAssignableFrom(type))
                {
                    val = UUObject.safeCast(type, obj);
                }
            }
        }
        catch (Exception ex)
        {
            val = defaultValue;
        }

        return val;
    }


    public static void safePut(final JSONObject json, final Object key, final Object val)
    {
        try
        {
            if (json != null && key != null)
            {
                if (val != null)
                {
                    if (val instanceof Enum)
                    {
                        json.put(key.toString(), ((Enum) val).name());
                    }
                    else
                    {
                        json.put(key.toString(), val);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safePut", "key: " + key, ex);
        }
    }

    public static void safePutArray(final JSONObject json, final Object key, final List val)
    {
        try
        {
            if (json != null && key != null)
            {
                if (val != null)
                {
                    JSONArray arr = new JSONArray();
                    for (Object o : val)
                    {
                        arr.put(o);
                    }

                    json.put(key.toString(), arr);
                }
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "safePutArray", "key: " + key, ex);
        }
    }

    public static JSONObject toJson(final Map<String, Object> map)
    {
        try
        {
            return new JSONObject(map);
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "toJson", ex);
            return null;
        }
    }

    @Nullable
    public static JSONObject toJsonFromStringMap(@Nullable final Map<String, String> map)
    {
        try
        {
            if (map != null)
            {
                return new JSONObject(map);
            }
        }
        catch (Exception ignored)
        {
        }

        return null;
    }

    @Nullable
    public static JSONObject toJsonFromObjectMap(@Nullable final Map<Object, Object> map)
    {
        try
        {
            if (map != null)
            {
                return new JSONObject(map);
            }
        }
        catch (Exception ignored)
        {
        }

        return null;
    }

    @Nullable
    public static JSONArray toJsonFromArray(@NonNull final ArrayList<?> array)
    {
        try
        {
            JSONArray arr = new JSONArray();

            for (Object o : array)
            {
                arr.put(o);
            }

            return arr;
        }
        catch (Exception ignored)
        {
        }

        return null;
    }


    @Nullable
    public static HashMap<Object,Object> jsonStringToHashMap(@Nullable final String jsonString)
    {
        JSONObject obj = toJsonObject(jsonString);
        if (obj == null)
        {
            return null;
        }

        HashMap<Object,Object> map = new HashMap<>();

        Iterator<String> keys = obj.keys();
        while (keys.hasNext())
        {
            String key = keys.next();
            Object val = obj.opt(key);
            map.put(key, val);
        }

        return map;
    }

    public static void fillFromJsonString(@NonNull final HashMap<Object, Object> map, @Nullable final String jsonString)
    {
        map.clear();
        HashMap<Object, Object> tmp = jsonStringToHashMap(jsonString);
        if (tmp != null)
        {
            map.putAll(tmp);
        }
    }

    public static Object toJson(final String jsonString)
    {
        Object obj = toJsonObject(jsonString);
        if (obj == null)
        {
            obj = toJsonArray(jsonString);
        }

        return obj;
    }

    public static JSONObject toJsonObject(final String jsonString)
    {
        try
        {
            return new JSONObject(jsonString);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static JSONArray toJsonArray(final String jsonString)
    {
        try
        {
            return new JSONArray(jsonString);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static JSONArray toJsonArray(final Iterable<?> list)
    {
        try
        {
            JSONArray arr = new JSONArray();

            for (Object o : list)
            {
                arr.put(o);
            }

            return arr;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static String toJsonString(final Map<String, Object> map)
    {
        String jsonStr = null;

        try
        {
            JSONObject obj = toJson(map);
            if (obj != null)
            {
                jsonStr = obj.toString();
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "toJsonString", ex);
            jsonStr = null;
        }

        return jsonStr;
    }

    public static String toJsonString(final JSONArray jsonArray)
    {
        String jsonStr = null;

        try
        {
            if (jsonArray != null)
            {
                jsonStr = jsonArray.toString();
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "toJsonString", ex);
            jsonStr = null;
        }

        return jsonStr;
    }

    public static String toJsonString(final JSONObject jsonObject)
    {
        String jsonStr = null;

        try
        {
            if (jsonObject != null)
            {
                jsonStr = jsonObject.toString();
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "toJsonString", ex);
            jsonStr = null;
        }

        return jsonStr;
    }

    @NonNull
    public static String toJsonStringFromStringMap(@Nullable final Map<String, String> map)
    {
        String jsonStr = "";

        try
        {
            JSONObject obj = toJsonFromStringMap(map);
            if (obj != null)
            {
                jsonStr = obj.toString();
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "toJsonStringFromStringMap", ex);
            jsonStr = "";
        }

        return jsonStr;
    }

    @NonNull
    public static String toJsonStringFromObjectMap(@Nullable final Map<Object, Object> map)
    {
        String jsonStr = "";

        try
        {
            JSONObject obj = toJsonFromObjectMap(map);
            if (obj != null)
            {
                jsonStr = obj.toString();
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUJson.class, "toJsonStringFromObjectMap", ex);
            jsonStr = "";
        }

        return jsonStr;
    }

    public static ArrayList<JSONObject> flattenJsonArray(final JSONArray json)
    {
        ArrayList<JSONObject> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    Object obj = json.get(i);
                    if (obj instanceof JSONObject)
                    {
                        list.add((JSONObject)obj);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<String> safeGetJsonStrings(final JSONArray json)
    {
        ArrayList<String> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    String obj = json.getString(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Long> safeGetJsonLongs(final JSONArray json)
    {
        ArrayList<Long> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    long obj = json.getLong(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Integer> safeGetJsonIntegers(final JSONArray json)
    {
        ArrayList<Integer> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    int obj = json.getInt(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Short> safeGetJsonShorts(final JSONArray json)
    {
        ArrayList<Short> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    short obj = (short)json.getInt(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Byte> safeGetJsonBytes(final JSONArray json)
    {
        ArrayList<Byte> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    byte obj = (byte)json.getInt(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Double> safeGetJsonDoubles(final JSONArray json)
    {
        ArrayList<Double> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    double obj = json.getDouble(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Float> safeGetJsonFloats(final JSONArray json)
    {
        ArrayList<Float> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    float obj = (float)json.getDouble(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    @NonNull
    public static ArrayList<Boolean> safeGetJsonBooleans(final JSONArray json)
    {
        ArrayList<Boolean> list = new ArrayList<>();

        try
        {
            if (json != null)
            {
                int count = json.length();
                for (int i = 0; i < count; i++)
                {
                    boolean obj = json.getBoolean(i);
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    public static ArrayList<String> safeGetArrayOfStrings(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonStrings(array);
    }

    public static ArrayList<Long> safeGetArrayOfLongs(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonLongs(array);
    }

    public static ArrayList<Integer> safeGetArrayOfIntegers(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonIntegers(array);
    }

    public static ArrayList<Short> safeGetArrayOfShorts(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonShorts(array);
    }

    public static ArrayList<Byte> safeGetArrayOfBytes(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonBytes(array);
    }

    public static ArrayList<Double> safeGetArrayOfDoubles(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonDoubles(array);
    }

    public static ArrayList<Float> safeGetArrayOfFloats(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonFloats(array);
    }

    public static ArrayList<Boolean> safeGetArrayOfBooleans(final JSONObject json, final Object key)
    {
        JSONArray array = safeGetJsonArray(json, key);
        return safeGetJsonBooleans(array);
    }

    public static byte[] safeSerializeJson(final JSONObject jsonObj, final String contentEncoding)
    {
        try
        {
            String str = jsonObj.toString();
            return str.getBytes(contentEncoding);
        }
        catch (Exception ex)
        {
            UULog.debug(UUJson.class, "safeSerializeJson", ex);
            return null;
        }
    }

    public static byte[] safeSerializeJson(final JSONArray jsonArray, final String contentEncoding)
    {
        try
        {
            String str = jsonArray.toString();
            return str.getBytes(contentEncoding);
        }
        catch (Exception ex)
        {
            UULog.debug(UUJson.class, "safeSerializeJson", ex);
            return null;
        }
    }

    public static byte[] objectToJson(final Object obj, final String contentEncoding)
    {
        byte[] json = null;

        if (obj != null)
        {
            if (obj instanceof JSONObject)
            {
                json = safeSerializeJson((JSONObject)obj, contentEncoding);
            }
            else if (obj instanceof ArrayList)
            {
                JSONArray jsonArray = new JSONArray();

                ArrayList<?> list = (ArrayList<?>)obj;
                for (Object listNode : list)
                {
                    JSONObject jsonObj = null;

                    if (listNode instanceof JSONObject)
                    {
                        jsonObj = (JSONObject)listNode;
                    }

                    if (jsonObj != null)
                    {
                        jsonArray.put(jsonObj);
                    }
                }

                json = safeSerializeJson(jsonArray, contentEncoding);
            }
            else if (obj instanceof JSONArray)
            {
                json = safeSerializeJson((JSONArray)obj, contentEncoding);
            }
        }

        return json;
    }

    public static void closeJsonReader(final JsonReader reader)
    {
        try
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        catch (Exception ex)
        {
            UULog.debug(UUJson.class, "closeJsonReader", ex);
        }
    }
}