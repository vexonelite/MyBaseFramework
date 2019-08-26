package tw.realtime.project.rtbaseframework.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import tw.realtime.project.rtbaseframework.LogWrapper;

import tw.realtime.project.rtbaseframework.apis.BaseConstants;
import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;


/**
 * Created by vexonelite on 2017/6/26.
 * revision on 2018/11/19.
 */
public final class JSONUtils {

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
            throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
        catch (JSONException e) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on jsonStringToJsonArray");
            throw new IeRuntimeException(e, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
                    throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, "", "");
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
                    throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, "", "");
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
                    throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, "", "");
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
                    throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, "", "");
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
                    throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
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
                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, "", "");
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
            throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
        }
    }

}

