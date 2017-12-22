package tw.realtime.project.rtbaseframework.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;


/**
 * Created by vexonelite on 2017/6/26.
 */

public class JSONUtils {

    /**
     * 將 JSON 字串轉換為 JSONObject
     * @param jsonString JSON 字串
     * @param logTag 印出 Log 時的 Class Tag
     * @return JSONObject
     * @throws AsyncApiException 例外物件
     */
    public static JSONObject jsonStringToJsonObject (String jsonString, String logTag) throws AsyncApiException {
        try {
            return new JSONObject(jsonString);
        }
        catch (JSONException e) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on jsonStringToJsonObject");
            throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
        }
    }

    /**
     * 將 JSON 字串轉換為 JSONArray
     * @param jsonString JSON 字串
     * @param logTag 印出 Log 時的 Class Tag
     * @return JSONArray
     * @throws AsyncApiException 例外物件
     */
    public static JSONArray jsonStringToJsonArray (String jsonString, String logTag) throws AsyncApiException {
        try {
            return new JSONArray(jsonString);
        }
        catch (JSONException e) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on jsonStringToJsonArray");
            throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
        }
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 value 字串
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 value 字串
     * @throws AsyncApiException 例外物件
     */
    public static String getStringFromJSON (JSONObject jsonObject,
                                            String key,
                                            String logTag,
                                            boolean required) throws AsyncApiException {
        String returnValue = null;
        if (jsonObject.has(key)) {
            String value = null;
            try {
                value = jsonObject.getString(key);
            }
            catch (JSONException e) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getStringFromJSON");
                throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
            }

            if (null != value) {
                returnValue = value;
            }
            else {
                String message = "getStringFromJSON: jsonObject.getString(" + key + ") is either null or empty!";
                LogWrapper.showLog(Log.WARN, logTag, message);
                if (required) {
                    throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, message);
                }
            }
        } else {
            String message = "getStringFromJSON: No key '" + key + "' in JSONobject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new AsyncApiException(ApiConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, message);
            }
        }

        return returnValue;
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 value 整數值
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 value 整數值
     * @throws AsyncApiException 例外物件
     */
    public static int getIntegerFromJSON (JSONObject jsonObject,
                                          String key,
                                          String logTag,
                                          boolean required) throws AsyncApiException {
        int returnValue = -1;
        if (jsonObject.has(key)) {
            try {
                returnValue = jsonObject.getInt(key);
            }
            catch (JSONException e) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getIntegerFromJSON");
                throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
            }
        }
        else {
            String message = "getIntegerFromJSON: No key '" + key + "' in JSONobject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new AsyncApiException(ApiConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, message);
            }
        }

        return returnValue;
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 JSON 物件
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 JSON 物件
     * @throws AsyncApiException 例外物件
     */
    public static JSONObject getJSONObjectFromJSON (JSONObject jsonObject,
                                                    String key,
                                                    String logTag,
                                                    boolean required) throws AsyncApiException {
        JSONObject returnObject = null;
        if (jsonObject.has(key)) {
            JSONObject innerObject = null;
            try {
                innerObject = jsonObject.getJSONObject(key);
            }
            catch (JSONException e) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getJSONObjectFromJSON");
                throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
            }

            if (null != innerObject) {
                returnObject = innerObject;
            }
            else {
                String message = "getJSONObjectFromJSON - jsonObject.getJSONObject(" + key + ") is either null or empty!";
                LogWrapper.showLog(Log.WARN, logTag, message);
                if (required) {
                    throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, message);
                }
            }
        } else {
            String message = "getJSONObjectFromJSON - No key " + key + " in JSONobject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new AsyncApiException(ApiConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, message);
            }
        }

        return returnObject;
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 JSONArray
     * @param jsonObject Input JSON 物件
     * @param key 指定的key 值
     * @param logTag 印出 Log 時的 Class Tag
     * @param required
     * @return key 值對應的 JSONArray
     * @throws AsyncApiException 例外物件
     */
    public static JSONArray getJSONArrayFromJSON (JSONObject jsonObject,
                                                  String key,
                                                  String logTag,
                                                  boolean required) throws AsyncApiException {
        JSONArray returnArray = null;
        if (jsonObject.has(key)) {
            JSONArray innerArray = null;
            try {
                innerArray = jsonObject.getJSONArray(key);
            }
            catch (JSONException e) {
                LogWrapper.showLog(Log.ERROR, logTag, "Exception on getJSONArrayFromJSON");
                throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
            }

            if (null != innerArray) {
                returnArray = innerArray;
            }
            else {
                String message = "getJSONArrayFromJSON - jsonObject.getJSONArray(" + key + ") is either null or empty!";
                LogWrapper.showLog(Log.WARN, logTag, message);
                if (required) {
                    throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, message);
                }
            }
        } else {
            String message = "getJSONArrayFromJSON - No key " + key + " in JSONobject!";
            LogWrapper.showLog(Log.WARN, logTag, message);
            if (required) {
                throw new AsyncApiException(ApiConstants.ExceptionCode.NO_SPECIFIED_KEY_IN_JSON, message);
            }
        }

        return returnArray;
    }

    /**
     * 從 JSON 物件中依 key 值取得對應的 JSON 物件
     * @param jsonArray Input JSONArray
     * @param index 指定的 index
     * @param logTag 印出 Log 時的 Class Tag
     * @return index 值對應的 JSON 物件
     * @throws AsyncApiException 例外物件
     */
    public static JSONObject getJSONObjectFromJSONArray (JSONArray jsonArray,
                                                         int index,
                                                         String logTag) throws AsyncApiException {
        try {
            return jsonArray.getJSONObject(index);
        }
        catch (JSONException e) {
            LogWrapper.showLog(Log.ERROR, logTag, "Exception on getJSONObjectFromJSONArray - " +
                    "cannot getJSONObject at index: " + index);
            throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_PARSING_FAILURE, e);
        }
    }
}
