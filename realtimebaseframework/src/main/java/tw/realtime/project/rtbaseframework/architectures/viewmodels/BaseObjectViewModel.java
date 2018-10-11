package tw.realtime.project.rtbaseframework.architectures.viewmodels;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.enumerations.LiveDataState;
import tw.realtime.project.rtbaseframework.enumerations.TaskState;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseObjectViewModel<T> extends BaseRxViewModel {

    /** Used to decide if we have to show a progress dialog */
    private MutableLiveData<TaskState> mTaskStateLiveData;

    /** Used to handle any exception happened during an async task execution call */
    private MutableLiveData<AsyncApiException> mErrorLiveData;

    private MutableLiveData<T> mDataLiveData;
    @Deprecated
    private MutableLiveData<LiveDataState> mStateLiveData;


    protected BaseObjectViewModel(@NonNull Application application) {
        super(application);
    }


    @MainThread
    public LiveData<T> getDataLiveData () {
        if (null == mDataLiveData) {
            mDataLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "getDataLiveData");
        }
        return mDataLiveData;
    }

    @MainThread
    @Deprecated
    public void resetDataLiveData () {
        if (null != mDataLiveData) {
            // this way poses problem for some case
            //mDataLiveData.setValue(null);
            mDataLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetDataLiveData");
        }
    }

    @MainThread
    protected void setDataLiveData (@Nullable T master) {
        if (null != mDataLiveData) {
            mDataLiveData.setValue(master);
            LogWrapper.showLog(Log.WARN, getLogTag(), "setDataLiveData");
        }
    }


    @MainThread
    @Deprecated
    public LiveData<LiveDataState> getStateLiveData () {
        if (null == mStateLiveData) {
            mStateLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "getStateLiveData");
        }
        return mStateLiveData;
    }

    @MainThread
    @Deprecated
    public void resetStateLiveData () {
        if (null != mStateLiveData) {
            // this way poses problem for some case
            //mStateLiveData.setValue(null);
            mStateLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetStateLiveData");
        }
    }

    @MainThread
    @Deprecated
    protected void setStateLiveData (@NonNull LiveDataState dataState) {
        if (null != mStateLiveData) {
            mStateLiveData.setValue(dataState);
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetListOfData");
        }
    }


    @MainThread
    public LiveData<AsyncApiException> getErrorLiveData () {
        if (null == mErrorLiveData) {
            mErrorLiveData = new MutableLiveData<>();
        }
        return mErrorLiveData;
    }

    @MainThread
    @Deprecated
    public void resetErrorLiveData () {
        if (null != mErrorLiveData) {
            // this way poses problem for some case
            //mErrorLiveData.setValue(null);
            mErrorLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetErrorLiveData");
        }
    }

    @MainThread
    protected void setErrorLiveData (@Nullable AsyncApiException exception) {
        if (null != mErrorLiveData) {
            mErrorLiveData.setValue(exception);
        }
    }


    @MainThread
    public LiveData<TaskState> getTaskState () {
        if (null == mTaskStateLiveData) {
            mTaskStateLiveData = new MutableLiveData<>();
        }
        return mTaskStateLiveData;
    }

    @MainThread
    @Deprecated
    public void resetTaskState () {
        if (null != mTaskStateLiveData) {
            // this way poses problem for some case
            //mTaskStateLiveData.setValue(null);
            mTaskStateLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetTaskState");
        }
    }

    @MainThread
    protected void setTaskState (@Nullable TaskState taskState) {
        if (null != mTaskStateLiveData) {
            mTaskStateLiveData.setValue(taskState);
        }
    }

}
