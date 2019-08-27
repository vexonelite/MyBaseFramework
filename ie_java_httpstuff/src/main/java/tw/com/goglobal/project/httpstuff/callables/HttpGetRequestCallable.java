package tw.com.goglobal.project.httpstuff.callables;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Response;
import tw.com.goglobal.project.httpstuff.OkHttpUtil;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.IeHttpException;
import tw.realtime.project.rtbaseframework.parameters.ApiParameterSetDelegate;


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
    public boolean doesEnableAesEncoding() {
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
            final okhttp3.Call call = new OkHttpUtil()
                    .generateHttpGetRequestCall(getApiUrl(), doesEnableHttpLog(), true);
            return call.execute();
        }
        catch (Throwable cause) {
            LogWrapper.showLog(Log.ERROR, "HttpGetRequestCallable", "Exception on call");
            if (cause instanceof IOException) {
                throw new IeHttpException(cause, ErrorCodes.HTTP.REQUEST_ERROR, "", "");
            }
            else {
                throw new IeHttpException(cause, ErrorCodes.HTTP.FAIL_TO_EXECUTE_API_REQUEST, "", "");
            }
        }
    }
}