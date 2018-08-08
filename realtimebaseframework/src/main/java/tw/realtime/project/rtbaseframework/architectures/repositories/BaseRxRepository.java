package tw.realtime.project.rtbaseframework.architectures.repositories;

import android.util.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.apis.AppRxRepository;

public abstract class BaseRxRepository implements AppRxRepository {

    private Disposable mDisposable;

    protected final String getLogTag () {
        return this.getClass().getSimpleName();
    }

    protected final void setDisposable (Disposable disposable) {
        mDisposable = disposable;
    }

    @Override
    public void rxDisposeIfNeeded () {
        localRxDisposeIfNeeded();
    }

    private void localRxDisposeIfNeeded () {
        if (null != mDisposable) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "localRxDisposableIfNeeded - dispose");
            }
            mDisposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "localRxDisposableIfNeeded - reset");
        }
    }

    /**
     * equivalent to implement io.reactivex.functions.Action
     */
    protected final void defaultSuccessActionCallback () {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultSuccessActionCallback#run - Tid: (" + Thread.currentThread().getId() + ")");
        rxDisposeIfNeeded();
    }

    /**
     * equivalent to implement io.reactivex.functions.Consumer<Throwable>
     */
    protected final void defaultErrorCallback (@NonNull Throwable throwable) throws Exception {
        LogWrapper.showLog(Log.ERROR, getLogTag(), "defaultErrorCallback#accept - Tid: (" + Thread.currentThread().getId() + ")", throwable);
        rxDisposeIfNeeded();
    }

}
