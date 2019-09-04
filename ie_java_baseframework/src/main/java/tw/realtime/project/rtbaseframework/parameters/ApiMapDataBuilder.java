package tw.realtime.project.rtbaseframework.parameters;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;

/**
 * Created by vexonelite on 2017/10/3.
 */

public class ApiMapDataBuilder implements ApiHashMapDelegate {

    private Map<String, String> bDataMap;

    public ApiMapDataBuilder() {
        bDataMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ApiHashMapDelegate setData(@NonNull String key, @NonNull String value) throws IeRuntimeException {
        if (!key.isEmpty()) {
            bDataMap.put(key, value);
        }
        return this;
    }

    @NonNull
    @Override
    public String retrieveData(@NonNull String key) {
        String result = "";
        if ((!key.isEmpty()) && (bDataMap.containsKey(key)) ) {
            result = result + bDataMap.get(key);
        }
        return result;
    }

    @NonNull
    @Override
    public JSONObject convertIntoJSON() {
        return new JSONObject(bDataMap);
    }

    @NonNull
    @Override
    public Map<String, String> getDataMap() {
        return new HashMap<>(bDataMap);
    }
}
