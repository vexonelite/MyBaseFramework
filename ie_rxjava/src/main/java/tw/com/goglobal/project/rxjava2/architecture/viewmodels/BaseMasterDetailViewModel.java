package tw.com.goglobal.project.rxjava2.architecture.viewmodels;

import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseMasterDetailViewModel<T, R> extends BaseObjectViewModel<T> {

    private final MutableLiveData<R> detailLiveData = new MutableLiveData<>();

    private final MutableLiveData<IeRuntimeException> detailErrorLiveData = new MutableLiveData<>();


    protected BaseMasterDetailViewModel(@NonNull Application application) {
        super(application);
    }


    @MainThread
    public final LiveData<R> getDetailLiveData () {
        return detailLiveData;
    }

    @MainThread
    protected final void setDetailLiveData(@Nullable R detail) {
        detailLiveData.setValue(detail);
    }


    @MainThread
    public final LiveData<IeRuntimeException> getDetailErrorLiveData() {
        return detailErrorLiveData;
    }

    @MainThread
    protected final void setDetailErrorLiveData(@Nullable IeRuntimeException exception) {
        detailErrorLiveData.setValue(exception);
    }

}
