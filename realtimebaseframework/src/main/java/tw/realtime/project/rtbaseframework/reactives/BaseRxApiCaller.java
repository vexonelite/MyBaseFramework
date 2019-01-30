package tw.realtime.project.rtbaseframework.reactives;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import io.reactivex.MaybeObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.interfaces.apis.RtAsyncApiCallback;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;

/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseRxApiCaller<T> {

    private Disposable mDisposable;

    private RtAsyncApiCallback<T> mCallback;


    protected BaseRxApiCaller(RtAsyncApiCallback<T> callback) {
        mCallback = callback;
    }

    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

    protected RtAsyncApiCallback<T> getCallback () {
        return mCallback;
    }

    protected void setDisposable (Disposable disposable) {
        mDisposable = disposable;
    }

    public void rxDisposableIfNeeded () {

        if (null != mDisposable) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "rxDisposableIfNeeded - dispose");
            }
            mDisposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "rxDisposableIfNeeded - reset");
        }
    }

    public class ApiDisposableObserver extends DisposableObserver<T> {

        private T mCachedObject;

        @Override
        public void onNext(@NonNull T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableObserver - onNext - Tid: " + Thread.currentThread().getId());
            mCachedObject = result;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiDisposableObserver - onError - Tid: " + Thread.currentThread().getId(), e);

            if (null != mCallback) {
                mCallback.onEnd();
                if (e instanceof AsyncApiException) {
                    AsyncApiException asyncApiException = (AsyncApiException) e;
                    if (ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN.equals(asyncApiException.getStatusCode())) {
                        mCallback.onTokenError();
                    }
                    else {
                        mCallback.onError(asyncApiException);
                    }
                }
                else {
                    mCallback.onError(new AsyncApiException(e, ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, "", ""));
                }
            }

            rxDisposableIfNeeded();
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableObserver - onComplete - Tid: " + Thread.currentThread().getId());

            if (null != mCallback) {
                mCallback.onEnd();
                mCallback.onSuccess(mCachedObject);
            }

            rxDisposableIfNeeded();
        }
    }

    public class ApiDisposableSingleObserver extends DisposableSingleObserver<T> {

        @Override
        public void onSuccess(@NonNull T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableSingleObserver - onSuccess - Tid: " + Thread.currentThread().getId());

            if (null != mCallback) {
                mCallback.onEnd();
                mCallback.onSuccess(result);
            }

            rxDisposableIfNeeded();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiDisposableSingleObserver - onError - Tid: " + Thread.currentThread().getId(), e);

            if (null != mCallback) {
                mCallback.onEnd();
                if (e instanceof AsyncApiException) {
                    AsyncApiException asyncApiException = (AsyncApiException) e;
                    if (ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN.equals(asyncApiException.getStatusCode())) {
                        mCallback.onTokenError();
                    }
                    else {
                        mCallback.onError(asyncApiException);
                    }
                }
                else {
                    mCallback.onError(new AsyncApiException(e, ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, "", ""));
                }
            }

            rxDisposableIfNeeded();
        }
    }

    public class ApiMaybeObserver implements MaybeObserver<T> {

        @Override
        public void onSubscribe (Disposable disposable) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onSubscribe - Tid: " + Thread.currentThread().getId());
            setDisposable(disposable);
        }

        @Override
        public void onSuccess(T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onSuccess - Tid: " + Thread.currentThread().getId());

            if (null != mCallback) {
                mCallback.onEnd();
                mCallback.onSuccess(result);
            }

            rxDisposableIfNeeded();
        }

        @Override
        public void onError(Throwable e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiMaybeObserver - onError - Tid: " + Thread.currentThread().getId(), e);

            if (null != mCallback) {
                mCallback.onEnd();
                if (e instanceof AsyncApiException) {
                    AsyncApiException asyncApiException = (AsyncApiException) e;
                    if (ApiConstants.ExceptionCode.SERVER_INVALID_ACCESS_TOKEN.equals(asyncApiException.getStatusCode())) {
                        mCallback.onTokenError();
                    }
                    else {
                        mCallback.onError(asyncApiException);
                    }
                }
                else {
                    mCallback.onError(new AsyncApiException(e, ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, "", ""));
                }
            }

            rxDisposableIfNeeded();
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onComplete - Tid: " + Thread.currentThread().getId());

            if (null != mCallback) {
                mCallback.onEnd();
                mCallback.onError(new AsyncApiException(
                        "filter returns negative result",
                        ApiConstants.ExceptionCode.RX_MAYBE_ON_COMPLETE,
                        "",
                        ""));
            }

            rxDisposableIfNeeded();
        }
    }

    public class DoOnSubscriber implements Consumer<Disposable> {
        @Override
        public void accept(@NonNull Disposable disposable) throws Exception {
            LogWrapper.showLog(Log.INFO, getLogTag(), "DoOnSubscriber - accept - Tid: " + Thread.currentThread().getId());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (null != mCallback) {
                        mCallback.onStart();
                    }
                }
            });
        }
    }

    public abstract void issueApiCall ();
}
