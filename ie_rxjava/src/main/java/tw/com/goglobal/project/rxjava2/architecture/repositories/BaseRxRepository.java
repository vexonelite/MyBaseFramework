package tw.com.goglobal.project.rxjava2.architecture.repositories;

import android.util.Log;

import androidx.annotation.Nullable;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import tw.com.goglobal.project.rxjava2.RxDisposeDelegate;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.apis.AppRepository;

/**
 * Created by vexonelite on 2018/08/08.
 * revision on 2018/11/28.
 */
public abstract class BaseRxRepository implements AppRepository, RxDisposeDelegate {

    private Disposable disposable;

    protected final String getLogTag() {
        return this.getClass().getSimpleName();
    }

    protected final void setDisposable(@Nullable Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void rxDisposeIfPossible() {
        localRxDisposeIfPossible();
    }

    private void localRxDisposeIfPossible() {
        if (null != disposable) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "localRxDisposeIfPossible - dispose");
            }
            disposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "localRxDisposeIfPossible - reset");
        }
    }

    /**
     * equivalent to implement io.reactivex.functions.Action
     */
    protected final void defaultSuccessActionCallback() {
        LogWrapper.showLog(Log.INFO, getLogTag(), "defaultSuccessActionCallback#run - Tid: (" + Thread.currentThread().getId() + ")");
        rxDisposeIfPossible();
    }

    /**
     * equivalent to implement io.reactivex.functions.Consumer<Throwable>
     */
    protected final void defaultErrorCallback(@NonNull Throwable throwable) throws Exception {
        LogWrapper.showLog(Log.ERROR, getLogTag(), "defaultErrorCallback#accept - Tid: (" + Thread.currentThread().getId() + ")", throwable);
        rxDisposeIfPossible();
    }

}
