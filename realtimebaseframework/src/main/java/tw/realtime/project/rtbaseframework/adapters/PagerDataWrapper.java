package tw.realtime.project.rtbaseframework.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;

import tw.realtime.project.rtbaseframework.adapters.recyclerview.RecyclerDataWrapper;

public final class PagerDataWrapper<T, R> extends RecyclerDataWrapper<T> {

    private final SparseArrayCompat<R> sparseArray = new SparseArrayCompat<>();

    public void putArrayItemAtPosition (@NonNull R arrayItem, int position) {
        synchronized(lock) {
            sparseArray.put(position, arrayItem);
        }
    }

    public void removeArrayItemAtPosition (int position) {
        synchronized(lock) {
            sparseArray.remove(position);
        }
    }

    /**
     * 取得 position 所對應的 Array Item
     * @param position
     * @return position Array Item；若 position不合法，會回傳 Null
     */
//    fun getArrayItemAtPosition(position: Int): R? =
//        try {
//            //showLog(Log.INFO, getLogTag(), "getArrayItemAtPosition")
//            sparseArray.get(position)
//        }
//        catch (e: Exception) {
//            //showLog(Log.ERROR, getLogTag(), "Exception on getArrayItemAtPosition!", e)
//            null
//        }
    @Nullable
    public R getArrayItemAtPosition(int position) {
        return sparseArray.get(position);
    }

}
