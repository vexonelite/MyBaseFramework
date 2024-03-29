package tw.com.goglobal.project.rxjava2;

import android.util.Log;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.ExceptionHelper;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResult;
import tw.realtime.project.rtbaseframework.delegates.apis.IeTaskDelegate;


public abstract class AbstractRxTask<T> implements IeTaskDelegate, RxDisposeDelegate {

    private Disposable disposable;

    public IeApiResult<T> callback;

    protected boolean isForHttp() { return false; }

    protected final String getLogTag() { return this.getClass().getSimpleName(); }

    protected final void setDisposable(@NonNull Disposable disposable) { this.disposable = disposable; }


    @Override
    public final void rxDisposeIfPossible() {
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
            rxDisposeIfPossible();
            notifyCallbackOnError(cause);
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableObserver - onComplete - Tid: " + Thread.currentThread().getId());
            rxDisposeIfPossible();
            notifyCallbackOnSuccess(cachedData);
        }
    }

    public final class ApiDisposableSingleObserver extends DisposableSingleObserver<T> {

        @Override
        public void onSuccess(@NonNull T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiDisposableSingleObserver - onSuccess - Tid: " + Thread.currentThread().getId());
            rxDisposeIfPossible();
            notifyCallbackOnSuccess(result);
        }

        @Override
        public void onError(@NonNull Throwable cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiDisposableSingleObserver - onError - Tid: " + Thread.currentThread().getId(), cause);
            rxDisposeIfPossible();
            notifyCallbackOnError(cause);
        }
    }

    public final class ApiCompletableObserver implements CompletableObserver {

        final T result;

        public ApiCompletableObserver(@NonNull T result) { this.result = result; }

        @Override
        public void onSubscribe(@NonNull Disposable disposable) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiCompletableObserver - onSubscribe - Tid: " + Thread.currentThread().getId());
            setDisposable(disposable);
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiCompletableObserver - onComplete - Tid: " + Thread.currentThread().getId());
            rxDisposeIfPossible();
            notifyCallbackOnSuccess(result);
        }

        @Override
        public void onError(@NonNull Throwable cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiCompletableObserver - onError - Tid: " + Thread.currentThread().getId(), cause);
            rxDisposeIfPossible();
            notifyCallbackOnError(cause);
        }
    }

    public final class ApiMaybeObserver implements MaybeObserver<T> {

        @Override
        public void onSubscribe(@NonNull Disposable disposable) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onSubscribe - Tid: " + Thread.currentThread().getId());
            setDisposable(disposable);
        }

        @Override
        public void onSuccess(@NonNull T result) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onSuccess - Tid: " + Thread.currentThread().getId());
            rxDisposeIfPossible();
            notifyCallbackOnSuccess(result);
        }

        @Override
        public void onError(@NonNull Throwable cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "ApiMaybeObserver - onError - Tid: " + Thread.currentThread().getId(), cause);
            rxDisposeIfPossible();
            notifyCallbackOnError(cause);
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "ApiMaybeObserver - onComplete - Tid: " + Thread.currentThread().getId());
            rxDisposeIfPossible();
            if (null != callback) {
                callback.onError(new IeRuntimeException(
                        "filter returns negative result", ErrorCodes.Base.RX_MAYBE_ON_COMPLETE));
            }
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

    ///

    public static <T extends AbstractRxTask<?>> void releaseTaskIfPossible(@Nullable T task) {
        if (null != task) {
            task.rxDisposeIfPossible();
        }
    }
}
