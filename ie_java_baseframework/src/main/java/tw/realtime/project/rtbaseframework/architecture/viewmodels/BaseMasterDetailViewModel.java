package tw.realtime.project.rtbaseframework.architecture.viewmodels;

import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseMasterDetailViewModel<T, R> extends BaseObjectViewModel<T> {

    private MutableLiveData<R> detailLiveData;

    private MutableLiveData<AsyncApiException> detailErrorLiveData;


    protected BaseMasterDetailViewModel(@NonNull Application application) {
        super(application);
    }


    @MainThread
    public final LiveData<R> getDetailLiveData () {
        if (null == detailLiveData) {
            detailLiveData = new MutableLiveData<>();
        }
        return detailLiveData;
    }

    @MainThread
    protected final void setDetailLiveData (@Nullable R detail) {
        if (null != detailLiveData) {
            detailLiveData.setValue(detail);
        }
    }


    @MainThread
    public final LiveData<AsyncApiException> getDetailErrorLiveData () {
        if (null == detailErrorLiveData) {
            detailErrorLiveData = new MutableLiveData<>();
        }
        return detailErrorLiveData;
    }

    @MainThread
    protected final void setDetailErrorLiveData (@Nullable AsyncApiException exception) {
        if (null != detailErrorLiveData) {
            detailErrorLiveData.setValue(exception);
        }
    }

}
