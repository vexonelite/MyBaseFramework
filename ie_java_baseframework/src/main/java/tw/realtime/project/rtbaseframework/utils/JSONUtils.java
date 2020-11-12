package tw.realtime.project.rtbaseframework.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.models.IePair;


/**
 * Created by vexonelite on 2017/6/26.
 * revision on 2018/11/19.
 */
public final class JSONUtils {

    public static boolean isJSONValid(@NonNull String json) {
        try {
            final Gson gson = new Gson();
            gson.fromJson(json, Object.class);
            return true;
        }
        catch(com.google.gson.JsonSyntaxException cause) {
            return false;
        }
    }

    /**
     * 將 JSON 字串轉換為 JSONObject
     * @param jsonString JSON 字串
     * @param logTag 印出 Log 時的 Class Tag
     * @return JSONObject
     * @throws IeRuntimeException 例外物件
     */
    @NonNull
    public static JSONObject jsonStringToJsonObject(
            @NonNull String jsonString, @NonNull String logTag) throws IeRuntimeException {
        try {
            return new JSONObject(jsonString);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on jsonStringToJsonObject");
            throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
        }
    }

    /**
     * 將 JSON 字串轉換為 JSONArray
     * @param jsonString JSON 字串
     * @param logTag 印出 Log 時的 Class Tag
     * @return JSONArray
     * @throws IeRuntimeException 例外物件
     */
    @NonNull
    public static JSONArray jsonStringToJsonArray(
            @NonNull String jsonString, @NonNull String logTag) throws IeRuntimeException {
        try {
            return new JSONArray(jsonString);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on jsonStringToJsonArray");
            throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
        }
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 value 字串
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 value 字串
     * @throws IeRuntimeException 例外物件
     */
    @NonNull
    public static String getStringFromJSON(
            @NonNull JSONObject jsonObject,
            @NonNull String key,
            @NonNull String logTag,
            boolean required
    ) throws IeRuntimeException {

        if (jsonObject.has(key)) {
            try {
                return jsonObject.getString(key);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getStringFromJSON");
                if (required) {
                    throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
                }
                else {
                    return "";
                }
            }
        }
        else {
            final String message = "getStringFromJSON: No key '" + key + "' in JSONObject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new IeRuntimeException(message, ErrorCodes.Base.NO_SPECIFIED_KEY_IN_JSON);
            }
            else {
                return "";
            }
        }
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 value 整數值
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 value 整數值
     * @throws IeRuntimeException 例外物件
     */
    public static int getIntegerFromJSON(
            @NonNull JSONObject jsonObject,
            @NonNull String key,
            @NonNull String logTag,
            boolean required
    ) throws IeRuntimeException {

        if (jsonObject.has(key)) {
            try {
                return jsonObject.getInt(key);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getIntegerFromJSON");
                if (required) {
                    throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
                }
                else {
                    return -1;
                }
            }
        }
        else {
            final String message = "getIntegerFromJSON: No key '" + key + "' in JSONObject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new IeRuntimeException(message, ErrorCodes.Base.NO_SPECIFIED_KEY_IN_JSON);
            }
            else {
                return -1;
            }
        }
    }

    public static long getLongFromJSON(
            @NonNull JSONObject jsonObject,
            @NonNull String key,
            @NonNull String logTag,
            boolean required
    ) throws IeRuntimeException {

        if (jsonObject.has(key)) {
            try {
                return jsonObject.getLong(key);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getLongFromJSON");
                if (required) {
                    throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
                }
                else {
                    return -1L;
                }
            }
        }
        else {
            final String message = "getLongFromJSON: No key '" + key + "' in JSONObject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new IeRuntimeException(message, ErrorCodes.Base.NO_SPECIFIED_KEY_IN_JSON);
            }
            else {
                return -1L;
            }
        }
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 JSON 物件
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 JSON 物件
     * @throws IeRuntimeException 例外物件
     */
    @NonNull
    public static JSONObject getJSONObjectFromJSON(
            @NonNull JSONObject jsonObject,
            @NonNull String key,
            @NonNull String logTag,
            boolean required
    ) throws IeRuntimeException {

        if (jsonObject.has(key)) {
            try {
                return jsonObject.getJSONObject(key);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getJSONObjectFromJSON");
                if (required) {
                    throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
                }
                else {
                    return new JSONObject();
                }
            }
        }
        else {
            final String message = "getJSONObjectFromJSON - No key " + key + " in JSONobject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new IeRuntimeException(message, ErrorCodes.Base.NO_SPECIFIED_KEY_IN_JSON);
            }
            else {
                return new JSONObject();
            }
        }
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 JSONArray
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 JSONArray
     * @throws IeRuntimeException 例外物件
     */
    @NonNull
    public static JSONArray getJSONArrayFromJSON(
            @NonNull JSONObject jsonObject,
            @NonNull String key,
            @NonNull String logTag,
            boolean required
    ) throws IeRuntimeException {

        if (jsonObject.has(key)) {
            try {
                return jsonObject.getJSONArray(key);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getJSONArrayFromJSON");
                if (required) {
                    throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
                }
                else {
                    return new JSONArray();
                }
            }
        }
        else {
            final String message = "getJSONArrayFromJSON - No key " + key + " in JSONobject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new IeRuntimeException(message, ErrorCodes.Base.NO_SPECIFIED_KEY_IN_JSON);
            }
            else {
                return new JSONArray();
            }
        }
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 JSON 物件
     * @param jsonArray Input JSONArray
     * @param index 指定的 index
     * @param logTag 印出 Log 時的 Class Tag
     * @return index 值對應的 JSON 物件
     * @throws IeRuntimeException 例外物件
     */
    @NonNull
    public static JSONObject getJSONObjectFromJSONArray(
            @NonNull JSONArray jsonArray, int index, String logTag) throws IeRuntimeException {
        try {
            return jsonArray.getJSONObject(index);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on getJSONObjectFromJSONArray - " +
                    "cannot getJSONObject at index: " + index);
            throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_PARSING_FAILURE);
        }
    }

    // [start] added in 2020/11/11

    @Nullable
    public static String[] dismantleJsonString(
            @Nullable final String json, @Nullable final String startChar, @Nullable final String endChar) {
        if (null == json) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - json is null!!");
            return null;
        }
        if (null == startChar) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - startChar is null!!");
            return null;
        }
        if (null == endChar) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - endChar is null!!");
            return null;
        }
        final int firstIndex = json.indexOf(startChar);
        final int lastIndex = json.lastIndexOf(endChar);
        LogWrapper.showLog(Log.INFO, "JSONUtils", "dismantleJsonString - firstIndex: " + firstIndex + ", lastIndex: " + lastIndex);
        if (lastIndex <= firstIndex) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - error: lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            //throw new IllegalArgumentException("lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            return null;
        }
        final String innerJson = json.substring(firstIndex + 1, lastIndex);
        LogWrapper.showLog(Log.INFO, "JSONUtils", "dismantleJsonString - innerJson: " + innerJson);
        final String[] keyValueArray = innerJson.split(",");
        for(final String keyValue : keyValueArray) {
            LogWrapper.showLog(Log.INFO, "JSONUtils", "dismantleJsonString - keyValue: [" + keyValue + "]");
        }
        return keyValueArray;
    }

    @Nullable
    public static String getInnerJsonString(
            @Nullable final String json, @Nullable final String startChar, @Nullable final String endChar) {
        if (null == json) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "getInnerJsonString - json is null!!");
            return null;
        }
        if (null == startChar) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "getInnerJsonString - startChar is null!!");
            return null;
        }
        if (null == endChar) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "getInnerJsonString - endChar is null!!");
            return null;
        }
        final int firstIndex = json.indexOf(startChar);
        final int lastIndex = json.lastIndexOf(endChar);
        LogWrapper.showLog(Log.INFO, "JSONUtils", "getInnerJsonString - firstIndex: " + firstIndex + ", lastIndex: " + lastIndex);
        if (lastIndex <= firstIndex) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "getInnerJsonString - error: lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            //throw new IllegalArgumentException("lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            return null;
        }
        final String innerJson = json.substring(firstIndex + 1, lastIndex);
        LogWrapper.showLog(Log.INFO, "JSONUtils", "getInnerJsonString - innerJson: " + innerJson);
        return innerJson;
    }

    @Nullable
    public static String[] getJsonArrayString(
            @Nullable final String json, @Nullable final String startChar, @Nullable final String endChar) {
        if (null == json) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - json is null!!");
            return null;
        }
        if (null == startChar) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - startChar is null!!");
            return null;
        }
        if (null == endChar) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - endChar is null!!");
            return null;
        }
        final int firstIndex = json.indexOf(startChar);
        final int lastIndex = json.lastIndexOf(endChar);
        LogWrapper.showLog(Log.INFO, "JSONUtils", "dismantleJsonString - firstIndex: " + firstIndex + ", lastIndex: " + lastIndex);
        if (lastIndex <= firstIndex) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "dismantleJsonString - error: lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            //throw new IllegalArgumentException("lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            return null;
        }
        final String innerJson = json.substring(firstIndex + 1, lastIndex);
        LogWrapper.showLog(Log.INFO, "JSONUtils", "dismantleJsonString - innerJson: " + innerJson);
        final String[] keyValueArray = innerJson.split(",");
        for(final String keyValue : keyValueArray) {
            LogWrapper.showLog(Log.INFO, "JSONUtils", "dismantleJsonString - keyValue: [" + keyValue + "]");
        }
        return keyValueArray;
    }

    @Nullable
    public static IePair<String, String> parseKeyValueString(
            @Nullable final String expectedKey,
            @Nullable final String keyValueString,
            final boolean isNumberFormat) {
        if (null == keyValueString) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "parseKeyValueString - keyValueString is null!!");
            return null;
        }
        if (null == expectedKey) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "parseKeyValueString - expectedKey is null!!");
            return null;
        }

        //LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - keyValueString: " + keyValueString);
        final String[] keyValueArray = keyValueString.split(":");
        if (keyValueArray.length != 2) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "parseKeyValueString - keyValueArray.length != 2 for [ESSID]!");
            return null;
        }

        final String keyString = keyValueArray[0];
        //LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - [" + expectedKey + "] - keyString: " + keyString);
        final int firstIndex2 = keyString.indexOf("\"");
        final int lastIndex2 = keyString.lastIndexOf("\"");
        //LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - [" + expectedKey + "] firstIndex2: " + firstIndex2 + ", lastIndex2: " + lastIndex2);
        if (lastIndex2 <= firstIndex2) {
            LogWrapper.showLog(Log.ERROR, "JSONUtils", "parseKeyValueString - error: lastIndex2(" + lastIndex2 + ") <= firstIndex2(" + firstIndex2 + ")");
            //throw new IllegalArgumentException("lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
            return null;
        }

        final String decodedKey = keyString.substring(firstIndex2 + 1, lastIndex2);

        final String valueString = keyValueArray[1];
        //LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - [" + expectedKey + "] - valueString: " + valueString);

        if (isNumberFormat) {
            LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - [" + expectedKey + "] decodedKey: [" + decodedKey + "], decodedValue: [" + valueString + "]");
            return new IePair<>(decodedKey, valueString);
        }
        else { // String format
            final int firstIndex1 = valueString.indexOf("\"");
            final int lastIndex1 = valueString.lastIndexOf("\"");
            //LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - [" + expectedKey + "] firstIndex1: " + firstIndex1 + ", lastIndex1: " + lastIndex1);
            if (lastIndex1 <= firstIndex1) {
                LogWrapper.showLog(Log.ERROR, "JSONUtils", "parseKeyValueString - error: lastIndex1(" + lastIndex1 + ") <= firstIndex1(" + firstIndex1 + ")");
                //throw new IllegalArgumentException("lastIndex(" + lastIndex + ") <= firstIndex(" + firstIndex + ")");
                return null;
            }

            final String decodedValue = valueString.substring(firstIndex1 + 1, lastIndex1);
            LogWrapper.showLog(Log.INFO, "JSONUtils", "parseKeyValueString - [" + expectedKey + "] decodedKey: [" + decodedKey + "], decodedValue: [" + decodedValue + "]");
            return new IePair<>(decodedKey, decodedValue);
        }
    }

    // [end] added in 2020/11/11
}

