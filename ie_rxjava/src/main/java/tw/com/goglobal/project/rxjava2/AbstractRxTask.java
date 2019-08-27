package tw.com.goglobal.project.rxjava2;

import android.util.Log;

import io.reactivex.MaybeObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.ExceptionHelper;
import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResult;


public abstract class AbstractRxTask<T> implements RxDisposeDelegate {

    private Disposable disposable;

    public IeApiResult<T> callback;

    protected boolean isForHttp() {
        return false;
    }

    protected final String getLogTag() {
        return this.getClass().getSimpleName();
    }

    protected final void setDisposable(@NonNull Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public final void rxDisposeIfNeeded() {
        if (null != disposable) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "rxDisposableIfNeeded - dispose");
            }
            disposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "rxDisposableIfNeeded - reset");
        }
    }

    public final class ApiDisposableObserver extends DisposableObserver<T> {

        private T cachedData;

        @Override
        public void onNext(@NonNull T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableObserver - onNext - Tid: " + Thread.currentThread().getId());
            cachedData = result;
        }

        @Override
        public void onError(@NonNull Throwable cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiDisposableObserver - onError - Tid: " + Thread.currentThread().getId(), cause);

            rxDisposeIfNeeded();
            notifyCallbackOnError(cause);
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableObserver - onComplete - Tid: " + Thread.currentThread().getId());
            rxDisposeIfNeeded();
            notifyCallbackOnSuccess(cachedData);
        }
    }

    public final class ApiDisposableSingleObserver extends DisposableSingleObserver<T> {

        @Override
        public void onSuccess(@NonNull T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableSingleObserver - onSuccess - Tid: " + Thread.currentThread().getId());
            rxDisposeIfNeeded();
            notifyCallbackOnSuccess(result);
        }

        @Override
        public void onError(@NonNull Throwable cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiDisposableSingleObserver - onError - Tid: " + Thread.currentThread().getId(), cause);
            rxDisposeIfNeeded();
            notifyCallbackOnError(cause);
        }
    }

    protected final void notifyCallbackOnSuccess(@NonNull T result) {
        if (null != callback) {
            callback.onSuccess(result);
        }
    }

    protected final void notifyCallbackOnError(@NonNull Throwable cause) {
        if (null != callback) {
            final IeRuntimeException exception = isForHttp()
                    ? ExceptionHelper.toIeHttpException(cause)
                    : ExceptionHelper.toIeRuntimeException(cause);
            callback.onError(exception);
        }
    }

    public final class ApiMaybeObserver implements MaybeObserver<T> {

        @Override
        public void onSubscribe(Disposable disposable) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onSubscribe - Tid: " + Thread.currentThread().getId());
            setDisposable(disposable);
        }

        @Override
        public void onSuccess(T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onSuccess - Tid: " + Thread.currentThread().getId());
            rxDisposeIfNeeded();
            notifyCallbackOnSuccess(result);
        }

        @Override
        public void onError(Throwable cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiMaybeObserver - onError - Tid: " + Thread.currentThread().getId(), cause);
            rxDisposeIfNeeded();
            notifyCallbackOnError(cause);
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onComplete - Tid: " + Thread.currentThread().getId());
            rxDisposeIfNeeded();
            if (null != callback) {
                callback.onError(new IeRuntimeException(
                        "filter returns negative result", ErrorCodes.Base.RX_MAYBE_ON_COMPLETE));
            }
        }
    }

    public abstract void runTask();
}
