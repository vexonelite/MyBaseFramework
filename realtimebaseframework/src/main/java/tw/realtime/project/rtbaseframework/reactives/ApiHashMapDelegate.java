package tw.realtime.project.rtbaseframework.reactives;

import java.util.Map;

/**
 * Created by vexonelite on 2017/10/3.
 */

public interface ApiHashMapDelegate extends ApiDataDelegate {
    ApiHashMapDelegate setData(String key, String value);
    String retrieveData(String key);
    Map<String, String> getDataMap();
}
