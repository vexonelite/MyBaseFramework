package tw.realtime.project.rtbaseframework.parameters;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;

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
    @NonNull
    JSONObject convertIntoJSON();

    /**
     * Put a key-value parameter into the HashMap. The returned ApiDataDelegate is handy for builder-style code.
     * @param key
     * @param value
     * @return
     */
    @NonNull
    ApiDataDelegate setData(@NonNull String key, @NonNull String value) throws IeRuntimeException;

    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the value corresponding to the given key
     */
    @NonNull
    String retrieveData(@NonNull String key);
}
