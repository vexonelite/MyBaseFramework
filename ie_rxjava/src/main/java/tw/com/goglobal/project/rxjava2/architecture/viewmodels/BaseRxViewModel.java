package tw.com.goglobal.project.rxjava2.architecture.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.disposables.Disposable;
import tw.com.goglobal.project.rxjava2.RxDisposeDelegate;
import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseRxViewModel extends AndroidViewModel implements RxDisposeDelegate {

    private Disposable disposable;

    protected BaseRxViewModel(@NonNull Application application) {
        super(application);
    }

    protected final void setDisposable(@Nullable Disposable disposable) {
        this.disposable = disposable;
    }

    private void localRxDisposePossible() {
        if (null != disposable) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "localDisposableIfNeeded - dispose");
            }
            disposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "localDisposableIfNeeded - reset");
        }
    }

    @Override
    public void rxDisposeIfPossible() {
        localRxDisposePossible();
    }


    protected final String getLogTag() {
        return this.getClass().getSimpleName();
    }

}
