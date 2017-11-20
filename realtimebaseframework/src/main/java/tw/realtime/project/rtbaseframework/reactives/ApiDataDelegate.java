package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONObject;

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
}
