package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by vexonelite on 2017/10/3.
 */

public interface ApiDataDelegate {
    ApiDataDelegate setData(String key, String value);
    String retrieveData(String key);
    JSONObject convertIntoJSON();
    Map<String, String> getDataMap();
}
