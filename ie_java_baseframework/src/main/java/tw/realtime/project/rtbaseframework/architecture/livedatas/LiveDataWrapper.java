package tw.realtime.project.rtbaseframework.architecture.livedatas;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public final class LiveDataWrapper<T> {

//    private final byte[] lock = new byte[0];
    private final MutableLiveData<T> mutableLiveData = new MutableLiveData<>();

    @MainThread
    public LiveData<T> retrieveLiveData() { return mutableLiveData; }

    @MainThread
    @Nullable
    public T retrieveItemFromLiveData() { return mutableLiveData.getValue(); }

    @MainThread
    public void setItemToLiveData(@Nullable T item) { mutableLiveData.setValue(item); }

    @MainThread
    public void postItemToLiveData(@Nullable T item) { mutableLiveData.postValue(item); }
//        synchronized (lock) {
//            mutableLiveData.postValue(item);
//        }
//    }
}
