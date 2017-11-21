package tw.realtime.project.rtbaseframework.api.commons;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;
import tw.realtime.project.rtbaseframework.LogWrapper;


@Deprecated
/**
 * 利用 OkHttp 來呼叫 Rest Api。
 * 提供 Builder 來設定網址, AccessToken, DeviceToken。
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public abstract class BaseMultipartApiCaller<T> extends BaseApiData implements AsyncApiCallback<T>{

    private ExecutorService mExecutorService;

    private Call mCurrentCall;

    private boolean doesNotNotifyCallback = false;

    private AsyncApiCallback<T> mCallback;
    private String mApiUrl;
    private String mAccessToken;
    private String mDeviceToken;


    private BaseMultipartApiCaller(Map<String, String> dataMap) {
        super(dataMap);
    }

    protected BaseMultipartApiCaller(BaseBuilder<T> builder) {
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

        if (null == mExecutorService) {
            mExecutorService = Executors.newFixedThreadPool(1);
        }
        onStart();
        mExecutorService.submit(new CallerTask());
    }

    private void terminateExecutorService () {
        if (null == mExecutorService) {
            return;
        }

        try {
            LogWrapper.showLog(Log.INFO, getLogTag(), "terminateExecutorService: attempt to shutdown executor");
            mExecutorService.shutdown();
            mExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on terminateExecutorService", e);
            System.err.println("tasks interrupted");
        }
        finally {
            if (!mExecutorService.isTerminated()) {
                LogWrapper.showLog(Log.WARN, getLogTag(), "terminateExecutorService: cancel non-finished tasks");
            }
            mExecutorService.shutdownNow();
            mExecutorService = null;
            LogWrapper.showLog(Log.INFO, getLogTag(), "terminateExecutorService: shutdownNow finished");
        }
    }

    private class CallerTask implements Runnable {
        @Override
        public void run() {

            try {
                retrieveDataFromApi();
            }
            catch (AsyncApiException e) {
                onEnd();
                if (ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN.equals(e.getStatusCode())) {
                    onTokenError();
                }
                else {
                    onError(e);
                }
            }

            new Handler(Looper.getMainLooper()).post(new TerminationTask());
        }

        private void retrieveDataFromApi () throws AsyncApiException {
            // 呼叫 Api，並取回 HTTP Response
            Response response = executeRequestCall();
            // 解析 HTTP Response
            String responseString = getOkHttpResponseString(response);
            T finalResult = parseResponseString(responseString);
            onEnd();
            onSuccess(finalResult);
        }
    }

    private class TerminationTask implements Runnable {
        @Override
        public void run() {
            terminateExecutorService();
        }
    }

    /**
     * 終止目前背景工作!
     * <p>
     * 需處理若進入頁面，但 App 就進入背景了，依目前的設定 任何結果不會回到頁面上!
     * 是否要重新呼叫 Api?
     */
    public void cancelCurrentTask () {
        doesNotNotifyCallback = true;
        cancelRequestCall();
        terminateExecutorService();
    }

    /**
     * 利用 OkHTTP，發送 POST Api Request
     * @return OkHTTP response
     * @throws AsyncApiException
     */
    private Response executeRequestCall () throws AsyncApiException {
        try {
            cancelRequestCall();
            String accessToken = ((null != mAccessToken) && (!mAccessToken.isEmpty())) ? mAccessToken : "";
            String deviceToken = ((null != mDeviceToken) && (!mDeviceToken.isEmpty())) ? mDeviceToken : "";
            mCurrentCall = getOkHttpRequestCall(convertJSON(), mApiUrl, accessToken, deviceToken);
            return mCurrentCall.execute();
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "executeRequestCall: HTTP_REQUEST_ERROR");
            throw new AsyncApiException(ApiConstants.ExceptionCode.HTTP_REQUEST_ERROR, e);
        }
    }

    /**
     * 取消目前的 Api 呼叫
     */
    private void cancelRequestCall () {
        try {
            if (null != mCurrentCall) {
                mCurrentCall.cancel();
                mCurrentCall = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "cancelRequestCall");
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "cancelRequestCall: ", e);
        }
    }

    /**
     * 為 Api 產生 OkHttp Reqest Call
     * @param apiUrl Api 之 Url
     * @param accessToken
     * @param deviceToken
     * @return
     */
    protected abstract Call getOkHttpRequestCall (JSONObject dataObject,
                                                  String apiUrl,
                                                  String accessToken,
                                                  String deviceToken);

    /**
     * 判定 Api Response 是否為 200 OK，若是就取得 Response String
     * 反之，丟出 Exception 物件
     * @param response Api Response
     * @return 200 OK 之下的 Response String
     * @throws AsyncApiException 解析過程中的 Exception 物件
     */
    private String getOkHttpResponseString (Response response) throws AsyncApiException {
        if (null == response) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getOkHttpResponseString: HTTP_RESPONSE_ERROR");
            throw new AsyncApiException(ApiConstants.ExceptionCode.HTTP_RESPONSE_ERROR,
                    "Http response is null!");
        }

        if (response.isSuccessful()) {
            try {
                return response.body().string();
            }
            catch (Exception e) {
                response.close();
                LogWrapper.showLog(Log.ERROR, getLogTag(), "getOkHttpResponseString: HTTP_RESPONSE_PARSING_ERROR");
                throw new AsyncApiException(ApiConstants.ExceptionCode.HTTP_RESPONSE_PARSING_ERROR, e);
            }
        }
        else {
            response.close();
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getOkHttpResponseString: HTTP_RESPONSE_ERROR");
            throw new AsyncApiException(ApiConstants.ExceptionCode.HTTP_RESPONSE_ERROR,
                    "Http response is not successful!");
        }
    }

    /**
     * 解析 Http Response 字串，並回傳出指定的回傳物件
     * @param responseString Http Response 字串
     * @return 指定的回傳物件
     * @throws AsyncApiException 解析過程中的 Exception 物件
     */
    protected abstract T parseResponseString (String responseString) throws AsyncApiException;

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
            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
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
            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
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
            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
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
            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
                mCallback.onSuccess(mResult);
            }
        }
    }



}
