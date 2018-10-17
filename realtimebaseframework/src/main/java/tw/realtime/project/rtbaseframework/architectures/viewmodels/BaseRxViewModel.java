package tw.realtime.project.rtbaseframework.architectures.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.disposables.Disposable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.apis.RxDisposeDelegate;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseRxViewModel extends AndroidViewModel implements RxDisposeDelegate {

    private Disposable mDisposable;

    protected BaseRxViewModel(@NonNull Application application) {
        super(application);
    }

    protected void setDisposable (Disposable disposable) {
        mDisposable = disposable;
    }

    private void localRxDisposeIfNeeded () {
        if (null != mDisposable) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "localDisposableIfNeeded - dispose");
            }
            mDisposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "localDisposableIfNeeded - reset");
        }
    }

    @Override
    public void rxDisposeIfNeeded () {
        localRxDisposeIfNeeded();
    }


    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

}
