package tw.realtime.project.rtbaseframework.architecture.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.disposables.Disposable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.apis.RxDisposeDelegate;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseRxViewModel extends AndroidViewModel implements RxDisposeDelegate {

    private Disposable disposable;

    protected BaseRxViewModel(@NonNull Application application) {
        super(application);
    }

    protected final void setDisposable (@Nullable Disposable disposable) {
        this.disposable = disposable;
    }

    private void localRxDisposeIfNeeded () {
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
    public void rxDisposeIfNeeded () {
        localRxDisposeIfNeeded();
    }


    protected final String getLogTag () {
        return this.getClass().getSimpleName();
    }

}
