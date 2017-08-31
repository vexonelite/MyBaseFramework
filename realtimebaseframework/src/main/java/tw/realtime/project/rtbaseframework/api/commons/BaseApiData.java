package tw.realtime.project.rtbaseframework.api.commons;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 基礎封裝物件，有一個 Map<String, String> 用來存放 (key, value) 資訊，
 * 並提供存取介面與轉換成 JSON 物件介面
 * Created by vexonelite on 2017/5/25.
 */
public class BaseApiData {

    private Map<String, String> mDataMap;

    /**
     * 取出 key 值所對應的字串
     * @param key 指定的 key 值
     * @return key 值所對應的字串
     */
    protected String retrieveData (String key) {
        String result = null;
        if ( (null != key) && (!key.isEmpty()) && (mDataMap.containsKey(key)) ) {
            result = mDataMap.get(key);
        }
        return result;
    }

    protected BaseApiData (Map<String, String> dataMap) {
        mDataMap = new HashMap<>(dataMap);
    }

    /**
     * 轉換 Map<String, String> 為JSON 物件
     * @return
     */
    public JSONObject convertJSON () {
        return new JSONObject(mDataMap);
    }
}
