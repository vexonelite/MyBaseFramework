package tw.realtime.project.rtbaseframework.api.commons;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;
import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * 利用 OkHttp 來呼叫 Rest Api。
 * 提供 Builder 來設定網址, AccessToken, DeviceToken。
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public abstract class BaseCacheApiCaller<T> extends BaseApiData implements AsyncApiCallback<T>{

    private ExecutorService mExecutorService;

    private Call mCurrentCall;

    private boolean doesNotNotifyCallback = false;

    private AsyncApiCallback<T> mCallback;
    private String mApiUrl;
    private String mAccessToken;
    private String mDeviceToken;
    private long mTimeGap;
    private boolean mCacheEnable;


    private BaseCacheApiCaller(Map<String, String> dataMap) {
        super(dataMap);
    }

    protected BaseCacheApiCaller(BaseBuilder<T> builder) {
        this(builder.getDataMap());
        mApiUrl = builder.bApiUrl;
        mAccessToken = builder.bAccessToken;
        mDeviceToken = builder.bDeviceToken;
        mCallback = builder.bCallback;
        mTimeGap = builder.bTimeGap;
        mCacheEnable = builder.bCacheEnable;
    }

    public static class BaseBuilder<T> extends ApiDataBuilder {

        private AsyncApiCallback<T> bCallback;
        private String bApiUrl;
        private String bAccessToken;
        private String bDeviceToken;
        private long bTimeGap = 0L;
        private boolean bCacheEnable = false;

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

        public BaseBuilder<T> setTimeGap (long timeGap) {
            if (timeGap > 0L) {
                bTimeGap = timeGap;
            }
            return this;
        }

        public BaseBuilder<T> setCacheEnableFlag (boolean flag) {
            bCacheEnable = flag;
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


    // 實作 of AsyncApiCallback<T>
    @Override
    public void onStart() {
        new Handler(Looper.getMainLooper()).post(new StartNotificationTask());
    }

    // 實作 of AsyncApiCallback<T>
    @Override
    public void onEnd() {
        new Handler(Looper.getMainLooper()).post(new EndNotificationTask());
    }

    // 實作 of AsyncApiCallback<T>
    @Override
    public void onTokenError() {
        new Handler(Looper.getMainLooper()).post(new TokenErrorTask());
    }

    // 實作 of AsyncApiCallback<T>
    @Override
    public void onError(AsyncApiException e) {
        new Handler(Looper.getMainLooper()).post(new ErrorNotificationTask(e));
    }

    // 實作 of AsyncApiCallback<T>
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




    /**
     * 檢查呼叫 Api 的參數是否合法
     * @throws IllegalArgumentException
     */
    protected void verifyEssentialParameters () throws IllegalArgumentException {

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
        Runnable task = mCacheEnable ? (new CallApiWithCacheTask()) : (new CallApiWithoutCacheTask());
        mExecutorService.submit(task);
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

    private class TerminationTask implements Runnable {
        @Override
        public void run() {
            terminateExecutorService();
        }
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


    private class CallApiWithoutCacheTask implements Runnable {
        @Override
        public void run() {

            try {
                T result = callApi();
                onEnd();
                onSuccess(result);
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

        protected T callApi() throws AsyncApiException {
            // 呼叫 Api，並取回 HTTP Response
            Response response = executeRequestCall();
            // 解析 HTTP Response
            String responseString = getOkHttpResponseString(response);
            return parseResponseString(responseString);
        }
    }

    private class CallApiWithCacheTask extends CallApiWithoutCacheTask {
        @Override
        public void run() {

            LogWrapper.showLog(Log.WARN, getLogTag(), "CallApiWithCacheTask - Phase1 - findDataFromDb with timeGap: " + mTimeGap);
            T cachedResult = null;
            //AsyncApiException cachedException = null;
            try {
                cachedResult = findDataFromDb(false, mTimeGap);
            }
            catch (AsyncApiException e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on findDataFromDb(false, " + mTimeGap +")", e);
            }

            if ( !doesNeedToCallApi(cachedResult) ) {
                LogWrapper.showLog(Log.WARN, getLogTag(), "CallApiWithCacheTask - Phase1 - found cache from Db!");
                onEnd();
                onSuccess(cachedResult);
                new Handler(Looper.getMainLooper()).post(new TerminationTask());
                return;
            }

            LogWrapper.showLog(Log.WARN, getLogTag(), "CallApiWithCacheTask - Phase2 - Execute Api!");
            T responseResult = null;
            AsyncApiException callApiException = null;
            try {
                responseResult = callApi();
            }
            catch (AsyncApiException e) {
                callApiException = e;
            }

            if (null != responseResult) {
                handleResponseResult(responseResult);
                return;
            }

            if (null != callApiException) {
                handleResponseException(callApiException);
            }

            new Handler(Looper.getMainLooper()).post(new TerminationTask());
        }

        private boolean doesNeedToCallApi (T cachedResult) {
            if (null != cachedResult) {
                if (cachedResult instanceof Collection) {
                    return ((Collection) cachedResult).isEmpty();
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }

        private void handleResponseResult (T responseResult) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "CallApiWithCacheTask - Phase2 - handleResponseResult");

            saveDataToDb(responseResult);

            try {
                T cachedResult = findDataFromDb(true, mTimeGap);
                onEnd();
                onSuccess(cachedResult);
            }
            catch (AsyncApiException e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on handleResponseResult", e);
                onEnd();
                //onError(e);
            }
        }

        private void handleResponseException (AsyncApiException callApiException) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "CallApiWithCacheTask - Phase2 - handleResponseException");

            if (ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN.equals(callApiException.getStatusCode())) {
                onEnd();
                onTokenError();
                return;
            }

            try {
                LogWrapper.showLog(Log.WARN, getLogTag(), "CallApiWithCacheTask - Phase2 - handleResponseException - try find cache without timeGap!");
                T cachedResult = findDataFromDb(true, 0L);
                onEnd();
                onError(callApiException);
                onSuccess(cachedResult);
            }
            catch (AsyncApiException e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on handleResponseException", e);
                onEnd();
                onError(callApiException);
            }
        }
    }

    /**
     * 開發過程中使用，測試 App 進入背景時用 (Activity 進入 onPause 狀態)
     */
    private void pauseTask () {
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "CallerTask - pauseTask: InterruptedException!");
        }
    }

    /**
     * 從 SQLite 中取出快取資料
     * @param regardlessOfTime 是否無視timeGap
     * @param timeGap           時間
     * @return
     * @throws AsyncApiException 取出過程中的 Exception 物件
     */
    protected T findDataFromDb (boolean regardlessOfTime, long timeGap) throws AsyncApiException {
        return null;
    }

    /**
     * 將指定的 data 寫入到 SQLite 中作為快取
     * @param data 指定的回傳物件
     * @throws AsyncApiException 解析過程中的 Exception 物件
     */
    protected void saveDataToDb (T data) throws AsyncApiException {

    }

    @Deprecated
    /**
     * Usage for development only
     */
    public void findDataFromDbInBackground(boolean regardlessOfTime, long timeGap) {
        if (null == mExecutorService) {
            mExecutorService = Executors.newFixedThreadPool(1);
        }
        mExecutorService.submit(new FindDbTask(regardlessOfTime, timeGap) );
    }

    @Deprecated
    /**
     * Usage for development only
     */
    private class FindDbTask implements Runnable {
        private boolean fRegardlessOfTime;
        private long fTimeGap;

        private FindDbTask (boolean regardlessOfTime, long timeGap) {
            fRegardlessOfTime = regardlessOfTime;
            fTimeGap = timeGap;
        }

        @Override
        public void run() {

            try {
                onSuccess(findDataFromDb(fRegardlessOfTime, fTimeGap));
            }
            catch (AsyncApiException e) {
                onError(e);
            }

            new Handler(Looper.getMainLooper()).post(new TerminationTask());
        }
    }

    @Deprecated
    /**
     * Usage for development only
     */
    public void saveDataToDbInBackground(T data) {
        if (null == mExecutorService) {
            mExecutorService = Executors.newFixedThreadPool(1);
        }
        mExecutorService.submit(new SaveDbTask(data) );
    }

    @Deprecated
    /**
     * Usage for development only
     */
    private class SaveDbTask implements Runnable {
        private T sData;

        private SaveDbTask (T data) {
            sData = data;
        }

        @Override
        public void run() {

            try {
                saveDataToDb(sData);
                onSuccess(sData);
            }
            catch (AsyncApiException e) {
                onError(e);
            }

            new Handler(Looper.getMainLooper()).post(new TerminationTask());
        }
    }
}
