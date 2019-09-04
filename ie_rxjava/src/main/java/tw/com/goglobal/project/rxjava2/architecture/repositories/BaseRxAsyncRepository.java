package tw.com.goglobal.project.rxjava2.architecture.repositories;

import android.util.Log;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeHttpException;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.delegates.apis.RtAsyncApiCallback;

/**
 * Created by vexonelite on 2018/08/08.
 * revision on 2018/11/28.
 */
public abstract class BaseRxAsyncRepository<T> extends BaseRxRepository {

    private RtAsyncApiCallback<T> callback;

    public final void setAsyncApiCallback(@Nullable RtAsyncApiCallback<T> callback) {
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

            if (cause instanceof IeHttpException) {
                final IeHttpException httpException = (IeHttpException) cause;
                if (ErrorCodes.HTTP.SERVER_INVALID_ACCESS_TOKEN.equals(httpException.getStatusCode())) {
                    callback.onTokenError();
                }
                else {
                    callback.onError(httpException);
                }
            }
            else if (cause instanceof IOException) {
                callback.onError(new IeHttpException(cause, ErrorCodes.HTTP.REQUEST_ERROR, "", ""));
            }
            else {
                callback.onError(new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR));
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
        rxDisposeIfPossible();
        notifyCallbackOnSuccess(result);
    }

    protected final void defaultAsyncErrorCallback(@NonNull Throwable cause) throws Exception {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultAsyncErrorCallback#accept - Tid: (" + Thread.currentThread().getId() + ")");

        rxDisposeIfPossible();

        if (cause instanceof IeRuntimeException) {
            final IeRuntimeException asyncApiException = (IeRuntimeException) cause;
            notifyCallbackOnError(asyncApiException);
        }
        else {
            notifyCallbackOnError(new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR));
        }
    }
}
