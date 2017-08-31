package tw.realtime.project.rtbaseframework.api.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * 建立 BaseApiData 物件之 Builder 物件，提供設定 (key, value) 與 利用 key 存取 value 之介面。
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public class ApiDataBuilder {

    private Map<String, String> bDataMap;

    public ApiDataBuilder () {
        bDataMap = new HashMap<>();
    }

    public Map<String, String> getDataMap() {
        return bDataMap;
    }

    public void setData (String key, String value) {
        if ( (null != key) && (!key.isEmpty()) && (null != value) ){
            bDataMap.put(key, value);
        }
    }

    /**
     * 取出 key 值所對應的字串
     * @param key 指定的 key 值
     * @return key 值所對應的字串
     */
    public String retrieveData (String key) {
        String result = "";
        if ( (null != key) && (!key.isEmpty()) && (bDataMap.containsKey(key)) ) {
            result = result + bDataMap.get(key);
        }
        return result;
    }

}
