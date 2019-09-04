package tw.com.goglobal.project.httpstuff.callables;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Response;
import tw.com.goglobal.project.httpstuff.OkHttpUtil;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeHttpException;


public final class SimpleHttpGetCallable implements Callable<Response> {

    public final String url;

    public SimpleHttpGetCallable(@NonNull String url) {
        this.url = url;
    }

    @Override
    public Response call() throws Exception {
        try {
            final Call requestCall = new OkHttpUtil()
                    .generateHttpGetRequestCall(url, true, false);
            return requestCall.execute();
        }
        catch (Exception cause) {
            if (cause instanceof IOException) {
                throw new IeHttpException(cause, ErrorCodes.HTTP.REQUEST_ERROR, "", "");
            }
            else {
                throw new IeHttpException(cause, ErrorCodes.HTTP.FAIL_TO_EXECUTE_API_REQUEST, "", "");
            }
        }
    }
}
