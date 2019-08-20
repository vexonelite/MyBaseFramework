package tw.realtime.project.rtbaseframework.reactives;

import java.io.IOException;

import io.reactivex.SingleEmitter;
import io.reactivex.annotations.NonNull;
import okhttp3.Response;
import tw.realtime.project.rtbaseframework.apis.ApiConstants;


/**
 * Created by vexonelite on 2018/08/08.
 */

public final class HttpGetRequestSingle extends BaseApiRequestSingle {

    /**
     * recommend to use BuildConfig.DEBUG_MODE
     */
    private final boolean httpLogEnableFlag;

    public HttpGetRequestSingle(@NonNull ApiParameterSetDelegate delegate) {
        super(delegate);
        httpLogEnableFlag = delegate.doesEnableHttpLog();
    }

    @Override
    public void subscribe(@NonNull SingleEmitter<Response> emitter) throws Exception {
        try {
            okhttp3.Call call = OkHttpUtils.generateHttpGetRequestCall(getApiUrl(), doesEnableHttpLog(), true);
            if (!emitter.isDisposed()) {
                emitter.onSuccess(call.execute());
            }
        }
        catch (Exception e) {
            if (e instanceof IOException) {
                //throw new AsyncApiException(ApiConstants.ExceptionCode.HTTP_REQUEST_ERROR, e);
                emitter.onError(new AsyncApiException(e, ApiConstants.ExceptionCode.HTTP_REQUEST_ERROR, "", ""));
            }
            else {
                //throw new AsyncApiException(ApiConstants.ExceptionCode.FAIL_TO_EXECUTE_API_REQUEST, e);
                emitter.onError(new AsyncApiException(e, ApiConstants.ExceptionCode.FAIL_TO_EXECUTE_API_REQUEST, "", ""));
            }
        }
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
}