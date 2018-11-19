package tw.realtime.project.rtbaseframework.reactives;

import android.util.Log;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Response;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.api.commons.OkHttpUtils;


/**
 * Created by vexonelite on 2018/08/14.
 */

public final class HttpGetRequestCallable extends BaseApiRequestCallable {

    /**
     * recommend to use BuildConfig.DEBUG_MODE
     */
    private final boolean httpLogEnableFlag;

    public HttpGetRequestCallable(@NonNull ApiParameterSetDelegate delegate) {
        super(delegate);
        httpLogEnableFlag = delegate.doesEnableHttpLog();
    }


    @Override
    public boolean doesEnableAesEncoding () {
        return false;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return httpLogEnableFlag;
    }

    /**
     * @return the flag of enabling SQLite cache
     */
    @Override
    public boolean doesEnableCache() {
        return false;
    }

    /**
     * @return the cache expiry time
     */
    @Override
    public long getTimeGap() {
        return 0;
    }


    @Override
    public Response call() throws Exception {
        try {
            final okhttp3.Call call = OkHttpUtils.generateHttpGetRequestCall(getApiUrl(), doesEnableHttpLog(), true);
            return call.execute();
        }
        catch (Throwable e) {
            LogWrapper.showLog(Log.ERROR, "HttpGetRequestCallable", "Exception on call");
            if (e instanceof IOException) {
                throw new AsyncApiException(e, ApiConstants.ExceptionCode.HTTP_REQUEST_ERROR, "", "");
            }
            else {
                throw new AsyncApiException(e, ApiConstants.ExceptionCode.FAIL_TO_EXECUTE_API_REQUEST, "", "");
            }
        }
    }
}