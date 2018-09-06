package tw.realtime.project.rtbaseframework.adapters.recyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecyclerDataWrapper<T> {

    protected final byte[] lock = new byte[0];

    private final List<T> itemSet = new ArrayList<>();

    public final int getRealDataCount() {
        return itemSet.size();
    }

    /**
     * 加入新的資料 List 到 內部資料 Holder
     * @param givenSet   新的資料 List
     */
    public final void appendItemSet(@NonNull List<T> givenSet) {
        //if ( (null != givenSet) && (!givenSet.isEmpty()) ) {
        if (!givenSet.isEmpty()) {
            synchronized(lock) {
                itemSet.addAll(givenSet);
            }
        }
    }

    /**
     * 設定內部資料 Holder 的 Reference 到指定的資料 List
     * @param givenSet 指定的資料 List
     */
    public final void setItemSet(@NonNull List<T> givenSet) {
        //if ( (null != givenSet) && (!givenSet.isEmpty()) ) {
        if (!givenSet.isEmpty()) {
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
    @Nullable
    public final T getItemElementAtPosition(int position) {
        if ( (position >= 0) && (position < itemSet.size()) ) {
            return itemSet.get(position);
        }
        else {
            return null;
        }
    }

}
