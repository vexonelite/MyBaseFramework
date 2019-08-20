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
     * Get the HashMap that keeps all key-value parameters
     * @return
     */
    Map<String, String> getDataMap();
}
