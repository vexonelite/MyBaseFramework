package tw.realtime.project.rtbaseframework.api.commons;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * 利用 OkHttp 來呼叫 Rest Api。
 * 提供 Builder 來設定網址, AccessToken, DeviceToken。
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public abstract class BaseApiCaller<T> extends BaseApiData implements okhttp3.Callback, AsyncApiCallback<T> {

    private okhttp3.Call mCurrentCall;

    private AsyncApiCallback<T> mCallback;
    private String mApiUrl;
    private String mAccessToken;
    private String mDeviceToken;

    private BaseApiCaller(Map<String, String> dataMap) {
        super(dataMap);
    }

    protected BaseApiCaller(BaseBuilder<T> builder) {
        this(builder.getDataMap());
        mApiUrl = builder.bApiUrl;
        mAccessToken = builder.bAccessToken;
        mDeviceToken = builder.bDeviceToken;
        mCallback = builder.bCallback;
    }

    public static class BaseBuilder<T> extends ApiDataBuilder {

        private AsyncApiCallback<T> bCallback;
        private String bApiUrl;
        private String bAccessToken;
        private String bDeviceToken;

        public BaseBuilder<T> setAsyncApiCallback (AsyncApiCallback<T> callback) {
            bCallback = callback;
            return this;
        }

        public BaseBuilder<T> setApiUrl (String apiUrl) {
            bApiUrl = apiUrl;
            return this;
        }

        public BaseBuilder<T> setAccessToken (String accessToken) {
            bAccessToken = accessToken;
            return this;
        }

        public BaseBuilder<T> setDeviceToken (String deviceToken) {
            bDeviceToken = deviceToken;
            return this;
        }
    }


    /**
     * 取得 Log 使用時的 Tag 字串
     * @return  Tag 字串
     */
    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

    /**
     * 檢查呼叫 Api 的參數是否合法
     * @throws IllegalArgumentException
     */
    protected void verifyEssentialParameters () throws IllegalArgumentException {
//        if ( (null == mHeaderKey) || (mHeaderKey.isEmpty()) ) {
//            throw new IllegalArgumentException("verifyEssentialParameters - Header Key is invalid!");
//        }
    }

    /**
     * 發送 Api
     */
    public void issueApiRequest ()  {
        verifyEssentialParameters();
        onStart();
        cancelRequestCall();
        String accessToken = ((null != mAccessToken) && (!mAccessToken.isEmpty())) ? mAccessToken : "";
        String deviceToken = ((null != mDeviceToken) && (!mDeviceToken.isEmpty())) ? mDeviceToken : "";
        mCurrentCall = getOkHttpRequestCall(convertJSON(), mApiUrl, accessToken, deviceToken);
        mCurrentCall.enqueue(this);
    }

    /**
     * 為 Api 產生 OkHttp Reqest Call
     * @param apiUrl Api 之 Url
     * @param accessToken
     * @param deviceToken
     * @return
     */
    protected abstract okhttp3.Call getOkHttpRequestCall (JSONObject dataObject,
                                                          String apiUrl,
                                                          String accessToken,
                                                          String deviceToken);

    /**
     * 取消目前的 Api 呼叫
     */
    public void cancelRequestCall () {
        try {
            if (null != mCurrentCall) {
                mCurrentCall.cancel();
                mCurrentCall = null;
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "cancelRequestCall: ", e);
        }
    }


    @Override
    public void onFailure(Call call, IOException e) {
        LogWrapper.showLog(Log.ERROR, getLogTag(), "onFailure", e);
        onEnd();
        onError(new AsyncApiException(ApiConstants.ExceptionCode.HTTP_REQUEST_ERROR, e));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onEnd();

        try {
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                LogWrapper.showLog(Log.INFO, getLogTag(), "onResponse - responseString: ");
                LogWrapper.showLongLog(Log.INFO, getLogTag(), responseString);
                parseResponseString(responseString);
            }
            else {
                onError(
                        new AsyncApiException(ApiConstants.ExceptionCode.HTTP_RESPONSE_ERROR,
                                "Http response is not successful!")
                );
            }
            response.close();
        }
        catch (Exception e) {
            onError(new AsyncApiException(ApiConstants.ExceptionCode.HTTP_RESPONSE_PARSING_ERROR, e));
        }
    }

    /**
     * 解析 Http Response 字串
     * @param responseString Http Response 字串
     */
    protected abstract void parseResponseString (String responseString);


    @Override
    public void onStart() {
        new Handler(Looper.getMainLooper()).post(new StartNotificationTask());
    }

    @Override
    public void onEnd() {
        new Handler(Looper.getMainLooper()).post(new EndNotificationTask());
    }

    @Override
    public void onTokenError() {
        new Handler(Looper.getMainLooper()).post(new TokenErrorTask());
    }

    @Override
    public void onError(AsyncApiException e) {
        new Handler(Looper.getMainLooper()).post(new ErrorNotificationTask(e));
    }

    @Override
    public void onSuccess(T t) {
        new Handler(Looper.getMainLooper()).post(new SuccessNotificationTask(t));
    }

    private class StartNotificationTask implements Runnable {
        @Override
        public void run() {
            if (null != mCallback) {
                mCallback.onStart();
            }
        }
    }

    private class EndNotificationTask implements Runnable {
        @Override
        public void run() {
            if (null != mCallback) {
                mCallback.onEnd();
            }
        }
    }

    private class TokenErrorTask implements Runnable {
        @Override
        public void run() {
            if (null != mCallback) {
                mCallback.onTokenError();
            }
        }
    }

    private class ErrorNotificationTask implements Runnable {

        private AsyncApiException mException;

        private ErrorNotificationTask (AsyncApiException e) {
            mException = e;
        }

        @Override
        public void run() {
            if (null != mCallback) {
                mCallback.onError(mException);
            }
        }
    }

    private class SuccessNotificationTask implements Runnable {

        private T mResult;

        private SuccessNotificationTask (T t) {
            mResult = t;
        }

        @Override
        public void run() {
            if (null != mCallback) {
                mCallback.onSuccess(mResult);
            }
        }
    }

}
