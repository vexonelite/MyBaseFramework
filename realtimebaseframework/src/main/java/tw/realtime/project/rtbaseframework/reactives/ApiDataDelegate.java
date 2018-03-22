package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;

/**
 * Used to get a JSONObject instance or a result that is converted from HashMap.
 * The returned JSONObject carries the key-value parameters.
 * <p>
 * Created by vexonelite on 2017/10/3.
 */
public interface ApiDataDelegate {
    /**
     * Get the the key-value parameters that are kept in the JSONObject instance.
     */
    JSONObject convertIntoJSON();

    /**
     * Put a key-value parameter into the HashMap. The returned ApiDataDelegate is handy for builder-style code.
     * @param key
     * @param value
     * @return
     */
    ApiDataDelegate setData(String key, String value) throws AsyncApiException;

    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the value corresponding to the given key
     */
    String retrieveData(String key);
}
