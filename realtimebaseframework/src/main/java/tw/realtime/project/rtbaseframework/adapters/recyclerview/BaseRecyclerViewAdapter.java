package tw.realtime.project.rtbaseframework.adapters.recyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private final byte[] lock = new byte[0];
    private final List<V> dataSetKeeper = new ArrayList<V>();

    @NonNull
    public abstract K onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull K holder, int position);

    @Override
    public int getItemCount() {
        return getRealDataCount();
    }


    public final boolean isEmpty () {
        return (getRealDataCount() == 0);
    }

    /**
     * 回傳實際 Data Holder 的 資料數量
     * @return
     */
    public final int getRealDataCount () {
        return dataSetKeeper.size();
    }

    /**
     * 取得 position 在 Data Holder 中對應的資料
     * @param position
     * @return 對應的資料
     */
    @Nullable
    public final V getObjectAtPosition (int position) {
        if ((dataSetKeeper.isEmpty()) || (position < 0) || (position >= dataSetKeeper.size()) ) {
            return null;
        }
        return dataSetKeeper.get(position);
    }

    /**
     * 將資料 List 加至目前 Data Holder 的尾端
     * @param dataSet 資料 List
     * @param defaultNotify 是否執行 notifyItemRangeInserted()
     */
    public final void appendNewDataSet(@NonNull List<V> dataSet, boolean defaultNotify) {
        if (dataSet.isEmpty()) {
            return;
        }
        final int size = dataSet.size();
        //final int start = (mData.isEmpty()) ? 0 : (mData.size() - 1);
        final int start = dataSetKeeper.size();
        synchronized (lock) {
            dataSetKeeper.addAll(dataSet);
        }
        if (defaultNotify) {
            notifyItemRangeInserted(start, size);
        }
    }

    /**
     * 將資料 List 加至目前 Data Holder 的前端
     * @param dataSet 資料 List
     * @param defaultNotify 是否執行 notifyItemRangeInserted()
     */
    public final void appendNewDataSetToTheTop (@NonNull List<V> dataSet, boolean defaultNotify) {
        if (dataSet.isEmpty()) {
            return;
        }
        final int size = dataSet.size();
        //final int start = (mData.isEmpty()) ? 0 : (mData.size() - 1);
        final int start = 0;
        synchronized (lock) {
            dataSetKeeper.addAll(start, dataSet);
        }
        if (defaultNotify) {
            notifyItemRangeInserted(start, (size - 1));
        }
    }

    /**
     * 清除目前 Data Holder 的資料
     * @param defaultNotify 是否執行 notifyItemRangeRemoved()
     */
    public final void removeAllExistingData(boolean defaultNotify) {
        if (dataSetKeeper.isEmpty()) {
            return;
        }
        final int size = dataSetKeeper.size();
        synchronized (lock) {
            dataSetKeeper.clear();
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
    public final void appendNewDataToTheEnd(@NonNull V entity, boolean defaultNotify) {
        final int position = dataSetKeeper.size();
        synchronized (lock) {
            dataSetKeeper.add(entity);
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
    final public void addNewDataAtPosition(int position, @NonNull V entity, boolean defaultNotify) {
        synchronized (lock) {
            dataSetKeeper.add(position, entity);
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
    public final void removeSpecifiedData (@NonNull V entity, boolean defaultNotify) {
        if (dataSetKeeper.isEmpty()) {
            return;
        }
        final int position = dataSetKeeper.indexOf(entity);
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
    public final void removeDataAtPosition(final int position, boolean defaultNotify) {
        if ( (position < 0) || (position >= dataSetKeeper.size()) ) {
            return;
        }
        synchronized (lock) {
            dataSetKeeper.remove(position);
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
    public final void swapDataFromAtoB (int fromPosition, int toPosition, boolean defaultNotify) {
        if ( (dataSetKeeper.isEmpty()) || (fromPosition == toPosition)
                || (fromPosition < 0) || (fromPosition >= dataSetKeeper.size())
                || (toPosition < 0) || (toPosition >= dataSetKeeper.size()) ) {
            return;
        }
        synchronized (lock) {
            Collections.swap(dataSetKeeper, fromPosition, toPosition);
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
    public final int getIndexOfObject(@NonNull V entity) {
        if (dataSetKeeper.isEmpty()) {
            return -1;
        }
        return dataSetKeeper.indexOf(entity);
    }
}
