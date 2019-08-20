package tw.com.goglobal.project.rxjava2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.BaseConstants;
import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;
import tw.realtime.project.rtbaseframework.utils.CryptUtils;
import tw.realtime.project.rtbaseframework.utils.JSONUtils;


/**
 * Created by vexonelite on 2017/10/3.
 * revision on 2018/11/19.
 */
public final class RxHttpOperators {

    private static String getLogTag() {
        return RxHttpOperators.class.getSimpleName();
    }

    public static class OkHttpResponseToJsonString implements Function<Response, String> {

        private boolean doesEnableAesEncoding = false;
        private String oAesKey = "";
        private String oAesIv = "";

        public OkHttpResponseToJsonString setEnableAesFlag(boolean flag) {
            doesEnableAesEncoding = flag;
            return this;
        }

        public OkHttpResponseToJsonString setAesKey(@NonNull String aesKey) {
            oAesKey = aesKey;
            return this;
        }

        public OkHttpResponseToJsonString setAesIv(@NonNull String aesIv) {
            oAesIv = aesIv;
            return this;
        }


        @Override
        public String apply(@NonNull Response response) throws Exception {
            final String result = okHttpResponseToJsonString(response);
            response.close();
            return result;
        }

        private String okHttpResponseToJsonString(@NonNull Response response) throws IeRuntimeException {
            LogWrapper.showLog(Log.INFO, getLogTag(), "okHttpResponseToJsonString - Tid: "
                    + Thread.currentThread().getId());

            if (!response.isSuccessful()) {
                final String message = "Something wrong - code: " + response.code() +
                        ", message: " + response.message();
                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.HTTP_RESPONSE_ERROR, "", "");
            }

            final ResponseBody responseBody = response.body();

            if (null == responseBody) {
                throw new IeRuntimeException("ResponseBody is null", BaseConstants.ExceptionCode.HTTP_RESPONSE_ERROR, "", "");

            }
            try {
                final String rawResponseString = responseBody.string();
                if (doesEnableAesEncoding) {
                    final CryptUtils cryptUtils = new CryptUtils();
                    final String jsonString = cryptUtils.decrypt(
                            rawResponseString,
                            oAesKey,
                            oAesIv); //decrypt
                    LogWrapper.showLongLog(Log.INFO, getLogTag(), "okHttpResponseToJsonString - Tid: ("
                            + Thread.currentThread().getId() + "), jsonString: " + jsonString);
                    return jsonString;

                }
                else {
                    LogWrapper.showLongLog(Log.INFO, getLogTag(), "okHttpResponseToJsonString - Tid: (" +
                            Thread.currentThread().getId() + "), rawResponseString: " + rawResponseString);
                    return rawResponseString;
                }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on okHttpResponseToJsonString");
                throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.HTTP_RESPONSE_PARSING_ERROR, "", "");
            }
            finally {
                responseBody.close();
            }
        }
    }

    public static class VerifyResponse implements Function<retrofit2.Response<ResponseBody>, ResponseBody> {

        private final int successCode;

        public VerifyResponse(int successCode) {
            this.successCode = successCode;
        }

        @Override
        public ResponseBody apply(@NonNull retrofit2.Response<ResponseBody> response) throws Exception {
            final int httpCode = response.code();
            if (successCode == httpCode) {
                return response.body();
            }
            else {
                final String message = "Something wrong - code: " + response.code() + ", message: " + response.message();
                LogWrapper.showLongLog(Log.ERROR, getLogTag(), "VerifyResponse - Tid: (" +
                        Thread.currentThread().getId() + "), message: " + message);
                final String statusCode = "" + response.code();
                final ResponseBody errorBody = response.errorBody();
                String jsonResponse = "";

                try {
                    final String errorResponseString = errorBody.string();
                    LogWrapper.showLongLog(Log.INFO, getLogTag(), "VerifyResponse - Tid: (" +
                            Thread.currentThread().getId() + "), errorResponseString: " + errorResponseString);
                    jsonResponse = jsonResponse + errorResponseString;
                }
                catch (Exception e) {
                    LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on VerifyResponse");
                }
                finally {
                    if (null != errorBody) {
                        errorBody.close();
                    }
                }

                throw new IeRuntimeException(message, BaseConstants.ExceptionCode.WRONG_STATUS_CODE, statusCode, jsonResponse);
            }
        }
    }

    public static class ResponseBodyToString implements Function<ResponseBody, String> {
        @Override
        public String apply(@NonNull ResponseBody responseBody) throws Exception {
            try {
                final String rawResponseString = responseBody.string();
                LogWrapper.showLongLog(Log.INFO, getLogTag(), "ResponseBodyToString - Tid: (" +
                        Thread.currentThread().getId() + "), rawResponseString: " + rawResponseString);
                return rawResponseString;
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on ResponseBodyToString");
                throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.HTTP_RESPONSE_PARSING_ERROR, "", "");
            }
            finally {
                responseBody.close();
            }
        }
    }

    /**
     * This depends on the need of App, I decide to comment it, and make it serve as a template.
     public static class DoesJsonObjectHasValidCode implements Function<JSONObject, JSONObject> {
    @Override
    public JSONObject apply(@NonNull JSONObject jsonObject) throws Exception {
    final String code = JSONUtils.getStringFromJSON(
    jsonObject, LccConstants.UsageKey.CODE, getLogTag(), true);
    if (LccConstants.StatusCode.INVALID_ACCESS_TOKEN.equals(code)) {
    throw new AsyncApiException(ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN,
    "Status Code = 004; " + jsonObject.toString());
    }
    else if (LccConstants.StatusCode.SUCCESSFUL_OPERATION.equals(code)) {
    return jsonObject;
    }
    else {
    throw new AsyncApiException(code, "Status Code != 200!", jsonObject.toString());
    }
    }
    }
     */

    public static class JsonStringToJsonObject implements Function<String, JSONObject> {
        @Override
        public JSONObject apply(@NonNull String jsonString) throws Exception {
            return JSONUtils.jsonStringToJsonObject(jsonString, getLogTag());
        }
    }

    public static class JsonStringToJsonArray implements Function<String, JSONArray> {
        @Override
        public JSONArray apply(@NonNull String jsonString) throws Exception {
            return JSONUtils.jsonStringToJsonArray(jsonString, getLogTag());
        }
    }

    public static class JsonObjectToJsonString implements Function<JSONObject, String> {
        @Override
        public String apply(@NonNull JSONObject jsonObject) throws Exception {
            try {
                LogWrapper.showLog(Log.INFO, getLogTag(), "JsonObjectToJsonString - Tid: " + Thread.currentThread().getId());
                return jsonObject.toString();
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on JsonObjectToJsonString - Tid: " +
                        + Thread.currentThread().getId());
                throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
            }
        }
    }

    public static class JsonArrayToJsonString implements Function<JSONArray, String> {
        @Override
        public String apply(@NonNull JSONArray jsonArray) throws Exception {
            try {
                LogWrapper.showLog(Log.INFO, getLogTag(), "JsonArrayToJsonString - Tid: " + Thread.currentThread().getId());
                return jsonArray.toString();
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on JsonArrayToJsonString - Tid: " +
                        + Thread.currentThread().getId());
                throw new IeRuntimeException(cause, BaseConstants.ExceptionCode.JSON_PARSING_FAILURE, "", "");
            }
        }
    }

    public static class GetDataObjectFromJsonObject implements Function<JSONObject, JSONObject> {

        private String mJsonKey;

        public GetDataObjectFromJsonObject(@NonNull String jsonKey) {
            mJsonKey = jsonKey;
        }

        @Override
        public JSONObject apply(@NonNull JSONObject validObject) throws Exception {
            LogWrapper.showLog(Log.INFO, getLogTag(), "GetDataObjectFromJsonObject - Tid: " + Thread.currentThread().getId());
            return JSONUtils.getJSONObjectFromJSON(validObject, mJsonKey, getLogTag(), true);
        }
    }

    public static class GetDataArrayFromJsonObject implements Function<JSONObject, JSONArray> {

        private String mJsonKey;

        public GetDataArrayFromJsonObject(@NonNull String jsonKey) {
            mJsonKey = jsonKey;
        }

        @Override
        public JSONArray apply(@NonNull JSONObject validObject) throws Exception {
            LogWrapper.showLog(Log.INFO, getLogTag(), "GetDataArrayFromJsonObject - Tid: " + Thread.currentThread().getId());
            return JSONUtils.getJSONArrayFromJSON(validObject, mJsonKey, getLogTag(), true);
        }
    }
}
