package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONArray;
import org.json.JSONObject;

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
    ApiDataDelegate setJSONArray(String key, JSONArray jsonArray) throws AsyncApiException;
    /**
     * Put a key-JSONObject parameter into the JSONObject.
     * The returned ApiDataDelegate is handy for builder-style code.
     * @param key
     * @param jsonObject
     * @return
     */
    ApiDataDelegate setJSONObject(String key, JSONObject jsonObject) throws AsyncApiException;
    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the JSONArray corresponding to the given key
     */
    JSONArray retrieveJSONArray(String key) throws AsyncApiException;
    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the JSONObject corresponding to the given key
     */
    JSONObject retrieveJSONObject(String key) throws AsyncApiException;
}
