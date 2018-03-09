package tw.realtime.project.rtbaseframework.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by per-erik on 14/11/14.
 */
public abstract class BaseRecyclerViewAdapter<V, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

    public static final int TYPE_ITEM = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_LOADER = 3;

    private final byte[] mLock = new byte[0];
    private List<V> mData = new ArrayList<V>();

    @NonNull
    public abstract K onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType);


    public abstract void onBindViewHolder(@NonNull K holder, int position);

    @Override
    public int getItemCount() {
        return getRealDataCount();
    }


    public boolean isEmpty () {
        return (getRealDataCount() == 0);
    }

    /**
     * 回傳實際 Data Holder 的 資料數量
     * @return
     */
    public int getRealDataCount () {
        if ( (null == mData) || (mData.isEmpty()) ) {
            return 0;
        }
        return mData.size();
    }

    /**
     * 取得 position 在 Data Holder 中對應的資料
     * @param position
     * @return 對應的資料
     */
    public V getObjectAtPosition (int position) {
        if ((null == mData) || (mData.isEmpty()) || (position < 0) || (position >= mData.size()) ) {
            return null;
        }
        return mData.get(position);
    }

    /**
     * 將資料 List 加至目前 Data Holder 的尾端
     * @param data 資料 List
     * @param defaultNotify 是否執行 notifyItemRangeInserted()
     */
    public void appendNewDataSet(final List<V> data, boolean defaultNotify) {
        if ( (null == mData) || (null == data) ) {
            return;
        }
        final int size = data.size();
        //final int start = (mData.isEmpty()) ? 0 : (mData.size() - 1);
        final int start = mData.size();
        synchronized (mLock) {
            mData.addAll(data);
        }
        if (defaultNotify) {
            notifyItemRangeInserted(start, size);
        }
    }

    /**
     * 將資料 List 加至目前 Data Holder 的前端
     * @param data 資料 List
     * @param defaultNotify 是否執行 notifyItemRangeInserted()
     */
    public void appendNewDataSetToTheTop (final List<V> data, boolean defaultNotify) {
        if ( (null == mData) || (null == data) ) {
            return;
        }
        final int size = data.size();
        //final int start = (mData.isEmpty()) ? 0 : (mData.size() - 1);
        final int start = 0;
        synchronized (mLock) {
            mData.addAll(start, data);
        }
        if (defaultNotify) {
            notifyItemRangeInserted(start, (size - 1));
        }
    }

    /**
     * 清除目前 Data Holder 的資料
     * @param defaultNotify 是否執行 notifyItemRangeRemoved()
     */
    public void removeAllExistingData(boolean defaultNotify) {
        if ( (null == mData) || (mData.isEmpty()) ) {
            return;
        }
        final int size = mData.size();
        synchronized (mLock) {
            mData.clear();
        }
        if (defaultNotify) {
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * 將資料物件加至目前 Data Holder 的尾端
     * @param entity 資料物件
     * @param defaultNotify 是否執行 notifyItemInserted()
     */
    public void appendNewDataToTheEnd(V entity, boolean defaultNotify) {
        if ( (null == mData) || (null == entity) ) {
            return;
        }
        final int position = mData.size();
        synchronized (mLock) {
            mData.add(entity);
        }
        if (defaultNotify) {
            notifyItemInserted(position);
        }
    }

    /**
     * 將資料物件加至 Data Holder 中的 position 位置
     * @param position
     * @param entity 資料物件
     * @param defaultNotify 是否執行 notifyItemInserted()
     */
    public void addNewDataAtPosition(int position, V entity, boolean defaultNotify) {
        if ( (null == mData) || (null == entity) ) {
            return;
        }
        synchronized (mLock) {
            mData.add(position, entity);
        }
        if (defaultNotify) {
            notifyItemInserted(position);
        }
    }

    /**
     * 從Data Holder 中移除指定的資料物件，若它存在於 Data Holder中
     * @param entity 指定的資料物件
     * @param defaultNotify 是否執行 notifyItemRemoved()
     */
    public void removeSpecifiedData (V entity, boolean defaultNotify) {
        if ( (null == mData) || (mData.isEmpty()) || (null == entity) ) {
            return;
        }
        final int position = mData.indexOf(entity);
        if (position < 0) {
            return;
        }
        removeDataAtPosition(position, defaultNotify);
    }

    /**
     * 將 Data Holder 中 position 位置所對應的資料物件移除
     * @param position 指定的位置
     * @param defaultNotify 是否執行 notifyItemRemoved()
     */
    public void removeDataAtPosition(final int position, boolean defaultNotify) {
        if ( (null == mData) || (position < 0) || (position >= mData.size()) ) {
            return;
        }
        synchronized (mLock) {
            mData.remove(position);
        }
        if (defaultNotify) {
            notifyItemRemoved(position);
        }
    }

    /**
     * 對調 fromPosition 與 toPosition 在Data Holder 中所對應的資料物件
     * @param fromPosition
     * @param toPosition
     * @param defaultNotify 是否執行 notifyItemMoved
     */
    public void swapDataFromAtoB (int fromPosition, int toPosition, boolean defaultNotify) {
        if ( (null == mData) || (mData.isEmpty()) || (fromPosition == toPosition)
                || (fromPosition < 0) || (fromPosition >= mData.size())
                || (toPosition < 0) || (toPosition >= mData.size()) ) {
            return;
        }
        synchronized (mLock) {
            Collections.swap(mData, fromPosition, toPosition);
        }
        if (defaultNotify) {
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    /**
     * 取得資料物件在 Data Holder 中所對應的位置
     * @param entity 資料物件
     * @return
     */
    public int getIndexOfObject(V entity) {
        if ( (null == mData) || (mData.isEmpty()) || (null == entity) ) {
            return -1;
        }
        return mData.indexOf(entity);
    }
}
