package tw.realtime.project.rtbaseframework.architecture.repositories;

import android.util.Log;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.interfaces.apis.AsyncApiCallback;

/**
 * Created by vexonelite on 2018/08/08.
 * revision on 2018/11/28.
 */
public abstract class BaseRxAsyncRepository<T> extends BaseRxRepository {

    private AsyncApiCallback<T> callback;

    public final void setAsyncApiCallback(@Nullable AsyncApiCallback<T> callback) {
        this.callback = callback;
    }


    protected final void onTokenError() {
        if (null != callback) {
            callback.onEnd();
            callback.onTokenError();
        }
    }

    protected final void notifyCallbackOnError(@NonNull Throwable cause) {
        if (null != callback) {
            callback.onEnd();

            if (cause instanceof AsyncApiException) {
                final AsyncApiException asyncApiException = (AsyncApiException) cause;
                if (ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN.equals(asyncApiException.getStatusCode())) {
                    callback.onTokenError();
                }
                else {
                    callback.onError(asyncApiException);
                }
            }
            else if (cause instanceof IOException) {
                callback.onError(new AsyncApiException(cause, ApiConstants.ExceptionCode.HTTP_REQUEST_ERROR, "", ""));
            }
            else {
                callback.onError(new AsyncApiException(cause, ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, "", ""));
            }
        }
    }

    protected final void notifyCallbackOnSuccess(@NonNull T result) {
        if (null != callback) {
            callback.onEnd();
            callback.onSuccess(result);
        }
    }

    protected final void defaultAsyncSuccessCallback(@NonNull T result) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultAsyncSuccessCallback#accept - Tid: (" + Thread.currentThread().getId() + ")");
        rxDisposeIfNeeded();
        notifyCallbackOnSuccess(result);
    }

    protected final void defaultAsyncErrorCallback(@NonNull Throwable cause) throws Exception {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultAsyncErrorCallback#accept - Tid: (" + Thread.currentThread().getId() + ")");

        rxDisposeIfNeeded();

        if (cause instanceof AsyncApiException) {
            final AsyncApiException asyncApiException = (AsyncApiException) cause;
            notifyCallbackOnError(asyncApiException);
        }
        else {
            notifyCallbackOnError(new AsyncApiException(cause, ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, "", ""));
        }
    }
}
