package tw.realtime.project.rtbaseframework.adapters;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import java.util.ArrayList;
import java.util.List;

public class PagerDataWrapper<T, R> {
    private final byte[] lock = new byte[0];
    private List<T> itemSet = new ArrayList<>();
    private SparseArrayCompat<R> sparseArray = new SparseArrayCompat<>();


    public int getRealDataCount() {
        return itemSet.size();
    }

    /**
     * 加入新的資料 List 到 內部資料 Holder
     * @param givenSet   新的資料 List
     */
    public void appendItemSet(@NonNull List<T> givenSet) {
        if ( (null != givenSet) && (!givenSet.isEmpty()) ) {
            synchronized(lock) {
                itemSet.addAll(givenSet);
            }
        }
    }

    /**
     * 設定內部資料 Holder 的 Reference 到指定的資料 List
     * @param givenSet 指定的資料 List
     */
    public void setItemSet(@NonNull List<T> givenSet) {
        if ( (null != givenSet) && (!givenSet.isEmpty()) ) {
            synchronized(lock) {
                itemSet.clear();
                itemSet.addAll(givenSet);
            }
        }
    }

    /**
     * 取得 position 所對應的資料物件
     * @param position
     * @return position 所對應的資料物件；若 position不合法，會回傳 Null
     */
//    fun getItemElementAtPosition(position: Int): T? =
//        if (position >= 0 && position < itemSet.size) {
//            itemSet[position]
//        } else {
//            null
//        }
    public T getItemElementAtPosition(int position) {
        return itemSet.get(position);
    }

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
    public R getArrayItemAtPosition(int position) {
        return sparseArray.get(position);
    }

}
