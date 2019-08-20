package tw.realtime.project.rtbaseframework.parameters;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;

/**
 * Provide common interface to set and retrieve the key-value parameters via a JSONObject
 * <p>
 * Created by vexonelite on 2017/10/3.
 */
public interface ApiJSONObjectDelegate extends ApiDataDelegate {
    /**
     * Put a key-JSONArray parameter into the JSONObject.
     * The returned ApiDataDelegate is handy for builder-style code.
     * @param key
     * @param jsonArray
     * @return
     */
    @NonNull
    ApiDataDelegate setJSONArray(@NonNull String key, @NonNull JSONArray jsonArray) throws IeRuntimeException;
    /**
     * Put a key-JSONObject parameter into the JSONObject.
     * The returned ApiDataDelegate is handy for builder-style code.
     * @param key
     * @param jsonObject
     * @return
     */
    @NonNull
    ApiDataDelegate setJSONObject(@NonNull String key, @NonNull JSONObject jsonObject) throws IeRuntimeException;
    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the JSONArray corresponding to the given key
     */
    @NonNull
    JSONArray retrieveJSONArray(@NonNull String key) throws IeRuntimeException;
    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the JSONObject corresponding to the given key
     */
    @NonNull
    JSONObject retrieveJSONObject(@NonNull String key) throws IeRuntimeException;
}
