package tw.realtime.project.rtbaseframework.reactives;

import java.util.Map;

/**
 * Provide common interface to set and retrieve the key-value parameters via a HashMap.
 *
 * <p>
 * Created by vexonelite on 2017/10/3.
 */

public interface ApiHashMapDelegate extends ApiDataDelegate {
    /**
     * Put a key-value parameter into the HashMap. The returned ApiDataDelegate is handy for builder-style code.
     * @param key
     * @param value
     * @return
     */
    ApiHashMapDelegate setData(String key, String value);

    /**
     * Retrieve the key-value parameter via a given key
     * @param key
     * @return the value corresponding to the given key
     */
    String retrieveData(String key);

    /**
     * Get the HashMap that keeps all key-value parameters
     * @return
     */
    Map<String, String> getDataMap();
}
