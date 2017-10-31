package tw.realtime.project.rtbaseframework.reactives;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.utils.JSONUtils;

/**
 * Created by vexonelite on 2017/10/3.
 */

public class ApiJSONDataBuilder implements ApiJSONObjectDelegate {

    private JSONObject mJSONObject;

    public ApiJSONDataBuilder() {
        mJSONObject = new JSONObject();
    }

    @Override
    public JSONObject convertIntoJSON() {
        return mJSONObject;
    }

    @Override
    public ApiDataDelegate setJSONArray(String key, JSONArray jsonArray) throws AsyncApiException {
        if ( (null != key) && (!key.isEmpty()) && (null != jsonArray) ){
            try {
                mJSONObject.put(key, jsonArray);
            }
            catch (Exception e) {
                throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_WRAPPING_FAILURE, e);
            }
        }
        return this;
    }

    @Override
    public ApiDataDelegate setJSONObject(String key, JSONObject jsonObject) throws AsyncApiException {
        if ( (null != key) && (!key.isEmpty()) && (null != jsonObject) ){
            try {
                mJSONObject.put(key, jsonObject);
            }
            catch (Exception e) {
                throw new AsyncApiException(ApiConstants.ExceptionCode.JSON_WRAPPING_FAILURE, e);
            }
        }
        return this;
    }

    @Override
    public JSONArray retrieveJSONArray(String key) throws AsyncApiException {
        return JSONUtils.getJSONArrayFromJSON(mJSONObject, key, getClass().getSimpleName(), true);
    }

    @Override
    public JSONObject retrieveJSONObject(String key) throws AsyncApiException {
        return JSONUtils.getJSONObjectFromJSON(mJSONObject, key, getClass().getSimpleName(), true);
    }
}
