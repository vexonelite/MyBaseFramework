package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vexonelite on 2017/10/3.
 */

public class ApiMapDataBuilder implements ApiHashMapDelegate {

    private Map<String, String> bDataMap;

    public ApiMapDataBuilder() {
        bDataMap = new HashMap<>();
    }

    @Override
    public ApiHashMapDelegate setData (String key, String value) {
        if ( (null != key) && (!key.isEmpty()) && (null != value) ){
            bDataMap.put(key, value);
        }
        return this;
    }

    @Override
    public String retrieveData (String key) {
        String result = "";
        if ( (null != key) && (!key.isEmpty()) && (bDataMap.containsKey(key)) ) {
            result = result + bDataMap.get(key);
        }
        return result;
    }

    @Override
    public JSONObject convertIntoJSON() {
        return new JSONObject(bDataMap);
    }

    @Override
    public Map<String, String> getDataMap() {
        return new HashMap<>(bDataMap);
    }
}
