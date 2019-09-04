package tw.com.goglobal.project.rxjava2.architecture.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.enumerations.TaskState;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseObjectViewModel<T> extends BaseRxViewModel {

    /** Used to decide if we have to show a progress dialog */
    private final MutableLiveData<TaskState> taskStateLiveData = new MutableLiveData<>();

    /** Used to handle any exception happened during an async task execution call */
    private final MutableLiveData<IeRuntimeException> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<T> dataLiveData = new MutableLiveData<>();


    protected BaseObjectViewModel(@NonNull Application application) {
        super(application);
    }


    @MainThread
    public final LiveData<T> getDataLiveData() {
        return dataLiveData;
    }

    @MainThread
    protected final void setDataLiveData(@Nullable T master) {
        dataLiveData.setValue(master);
    }


    @MainThread
    public final LiveData<IeRuntimeException> getErrorLiveData() {
        return errorLiveData;
    }

    @MainThread
    protected final void setErrorLiveData(@Nullable IeRuntimeException exception) {
        errorLiveData.setValue(exception);
    }


    @MainThread
    public final LiveData<TaskState> getTaskState() {
        return taskStateLiveData;
    }

    @MainThread
    protected final void setTaskState(@Nullable TaskState taskState) {
        taskStateLiveData.setValue(taskState);
    }

    public final class DoOnSubscriber implements Consumer<Disposable> {
        @Override
        public void accept(@io.reactivex.annotations.NonNull Disposable disposable) throws Exception {
            LogWrapper.showLog(Log.INFO, getLogTag(), "DoOnSubscriber - accept - Tid: " + Thread.currentThread().getId());
            new Handler(Looper.getMainLooper()).post(
                    () -> {setTaskState(TaskState.LOADING_BEGIN); } );
        }
    }

}
