package tw.realtime.project.rtbaseframework.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class IeSparseArrayHolder<T> {

    private final SparseArrayCompat<SparseArrayCompat<T>> holderArray;
    private final SparseArrayCompat<List<T>> listArray;

    public IeSparseArrayHolder(
            @NonNull SparseArrayCompat<SparseArrayCompat<T>> holderArray,
            @NonNull SparseArrayCompat<List<T>> listArray) {
        this.holderArray = holderArray;
        this.listArray = listArray;
    }

    @NonNull
    private String getLogTag() { return this.getClass().getSimpleName(); }

    @Nullable
    public T getObjectBy(final int type, final int identifier) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "getObjectBy - type: " + type + ", identifier: " + identifier);
        final SparseArrayCompat<T> typeArray = holderArray.get(type);
        if (null == typeArray) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getObjectBy - get no Array for type: " + type);
            return null;
        }
        final T object = typeArray.get(identifier);
        if (null == object) { LogWrapper.showLog(Log.ERROR, getLogTag(), "getObjectBy - get no object for identifier: " + identifier); }
        return object;
    }

    @Nullable
    public List<T> getObjectListBy(final int type) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "getObjectListBy - type: " + type);
        final List<T> objectList = listArray.get(type);
        if (null == objectList) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getObjectListBy - get no object list for type: " + type);
            return null;
        }
        return objectList;
    }
}
