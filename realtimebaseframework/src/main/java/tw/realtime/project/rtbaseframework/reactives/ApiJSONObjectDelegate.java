package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;

/**
 * Created by vexonelite on 2017/10/3.
 */

public interface ApiJSONObjectDelegate extends ApiDataDelegate {
    ApiDataDelegate setJSONArray(String key, JSONArray jsonArray) throws AsyncApiException;
    ApiDataDelegate setJSONObject(String key, JSONObject jsonObject) throws AsyncApiException;
    JSONArray retrieveJSONArray(String key) throws AsyncApiException;
    JSONObject retrieveJSONObject(String key) throws AsyncApiException;
}
