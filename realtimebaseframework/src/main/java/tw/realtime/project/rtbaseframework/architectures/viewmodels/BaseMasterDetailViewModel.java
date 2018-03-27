package tw.realtime.project.rtbaseframework.architectures.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.enumerations.LiveDataState;


/**
 * Created by vexonelite on 2018/03/22.
 */

public abstract class BaseMasterDetailViewModel<T, R> extends BaseObjectViewModel<T> {

    private MutableLiveData<R> mDetailLiveData;
    private MutableLiveData<LiveDataState> mDetailStateLiveData;
    private MutableLiveData<AsyncApiException> mDetailErrorLiveData;


    protected BaseMasterDetailViewModel(@NonNull Application application) {
        super(application);
    }


    @MainThread
    public LiveData<R> getDetailLiveData () {
        if (null == mDetailLiveData) {
            mDetailLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "getDetailLiveData");
        }
        return mDetailLiveData;
    }

    @MainThread
    public void resetDetailLiveData () {
        if (null != mDetailLiveData) {
            // this way poses problem for some case
            //mDetailLiveData.setValue(null);
            mDetailLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetDetailLiveData");
        }
    }

    @MainThread
    protected void setDetailLiveData (@NonNull R detail) {
        if (null != mDetailLiveData) {
            mDetailLiveData.setValue(detail);
            LogWrapper.showLog(Log.WARN, getLogTag(), "setDetailLiveData");
        }
    }



    @MainThread
    public LiveData<LiveDataState> getDetailStateLiveData () {
        if (null == mDetailStateLiveData) {
            mDetailStateLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "getDetailStateLiveData");
        }
        return mDetailStateLiveData;
    }

    @MainThread
    public void resetDetailStateLiveData () {
        if (null != mDetailStateLiveData) {
            // this way poses problem for some case
            //mDetailStateLiveData.setValue(null);
            mDetailStateLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetDetailStateLiveData");
        }
    }

    @MainThread
    protected void setDetailStateLiveData (@NonNull LiveDataState dataState) {
        if (null != mDetailStateLiveData) {
            mDetailStateLiveData.setValue(dataState);
            LogWrapper.showLog(Log.WARN, getLogTag(), "setDetailStateLiveData");
        }
    }


    @MainThread
    public LiveData<AsyncApiException> getDetailErrorLiveData () {
        if (null == mDetailErrorLiveData) {
            mDetailErrorLiveData = new MutableLiveData<>();
        }
        return mDetailErrorLiveData;
    }

    @MainThread
    public void resetDetailErrorLiveData () {
        if (null != mDetailErrorLiveData) {
            // this way poses problem for some case
            //mDetailErrorLiveData.setValue(null);
            mDetailErrorLiveData = new MutableLiveData<>();
            LogWrapper.showLog(Log.WARN, getLogTag(), "resetDetailErrorLiveData");
        }
    }

    @MainThread
    protected void setDetailErrorLiveData (@NonNull AsyncApiException exception) {
        if (null != mDetailErrorLiveData) {
            mDetailErrorLiveData.setValue(exception);
        }
    }

}
