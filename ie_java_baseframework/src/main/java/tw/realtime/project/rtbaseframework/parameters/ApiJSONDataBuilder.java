package tw.realtime.project.rtbaseframework.parameters;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.apis.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;
import tw.realtime.project.rtbaseframework.utils.JSONUtils;


/**
 * Created by vexonelite on 2017/10/3.
 */

public class ApiJSONDataBuilder implements ApiJSONObjectDelegate {

    private JSONObject mJSONObject;

    public ApiJSONDataBuilder() {
        mJSONObject = new JSONObject();
    }

    @NonNull
    @Override
    public JSONObject convertIntoJSON() {
        return mJSONObject;
    }

    @NonNull
    @Override
    public ApiDataDelegate setData(@NonNull String key, @NonNull String value) throws IeRuntimeException {
        if (!key.isEmpty()) {
            try {
                mJSONObject.put(key, value);
            }
            catch (Exception cause) {
                throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_WRAPPING_FAILURE);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public String retrieveData(@NonNull String key) {
        return JSONUtils.getStringFromJSON(mJSONObject, key, getClass().getSimpleName(), true);
    }

    @NonNull
    @Override
    public ApiDataDelegate setJSONArray(@NonNull String key, @NonNull JSONArray jsonArray) throws IeRuntimeException {
        if (!key.isEmpty()){
            try {
                mJSONObject.put(key, jsonArray);
            }
            catch (Exception cause) {
                throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_WRAPPING_FAILURE);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public ApiDataDelegate setJSONObject(@NonNull String key, @NonNull JSONObject jsonObject) throws IeRuntimeException {
        if (!key.isEmpty()) {
            try {
                mJSONObject.put(key, jsonObject);
            }
            catch (Exception cause) {
                throw new IeRuntimeException(cause, ErrorCodes.Base.JSON_WRAPPING_FAILURE);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public JSONArray retrieveJSONArray(@NonNull String key) throws IeRuntimeException {
        return JSONUtils.getJSONArrayFromJSON(mJSONObject, key, getClass().getSimpleName(), true);
    }

    @NonNull
    @Override
    public JSONObject retrieveJSONObject(@NonNull String key) throws IeRuntimeException {
        return JSONUtils.getJSONObjectFromJSON(mJSONObject, key, getClass().getSimpleName(), true);
    }
}
