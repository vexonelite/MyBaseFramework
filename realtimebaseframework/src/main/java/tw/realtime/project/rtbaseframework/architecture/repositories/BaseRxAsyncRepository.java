package tw.realtime.project.rtbaseframework.architecture.repositories;

import android.util.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.interfaces.apis.AsyncApiCallback;

public abstract class BaseRxAsyncRepository<T> extends BaseRxRepository {

    private AsyncApiCallback<T> callback;


    protected BaseRxAsyncRepository(@Nullable AsyncApiCallback<T> callback) {
        this.callback = callback;
    }


    protected final void onTokenError () {
        if (null != callback) {
            callback.onEnd();
            callback.onTokenError();
        }
    }

    protected final void onErrorHappened (AsyncApiException e) {
        if (null != callback) {
            callback.onEnd();
            callback.onError(e);
        }
    }

    protected final void notifyCallbackOnSuccess (T result) {
        if (null != callback) {
            callback.onEnd();
            callback.onSuccess(result);
        }
    }

    protected final void defaultAsyncSuccessCallback (@NonNull T result) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultAsyncSuccessCallback#accept - Tid: (" + Thread.currentThread().getId() + ")");
        rxDisposeIfNeeded();
        notifyCallbackOnSuccess(result);
    }

    protected final void defaultAsyncErrorCallback (@NonNull Throwable throwable) throws Exception {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultAsyncErrorCallback#accept - Tid: (" + Thread.currentThread().getId() + ")");

        rxDisposeIfNeeded();

        if (throwable instanceof AsyncApiException) {
            final AsyncApiException asyncApiException = (AsyncApiException) throwable;
            onErrorHappened(asyncApiException);
        }
        else {
            onErrorHappened(new AsyncApiException(throwable, ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, "", ""));
        }
    }
}
