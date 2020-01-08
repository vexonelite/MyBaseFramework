package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;


public final class IeDiffListCallback<T> extends DiffUtil.Callback {

    private final byte[] lock = new byte[0];
    private final List<T> theCurrentList = new ArrayList<>();
    private final List<T> theNewList = new ArrayList<>();
    private final DiffUtil.ItemCallback<T> itemCallback;

    public IeDiffListCallback(@NonNull DiffUtil.ItemCallback<T> itemCallback) {
        this.itemCallback = itemCallback;
    }

    public void updateDataSet(@NonNull List<T> currentList, @NonNull List<T> newList) {
        synchronized (lock) {
            theCurrentList.clear();
            theCurrentList.addAll(currentList);
            theNewList.clear();
            theNewList.addAll(newList);
        }
    }

    @Override
    public int getOldListSize() { return theCurrentList.size(); }

    @Override
    public int getNewListSize() { return theNewList.size(); }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        final T oldItem = theCurrentList.get(oldItemPosition);
        final T newItem = theNewList.get(newItemPosition);
        return itemCallback.areItemsTheSame(oldItem, newItem);
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        final T oldItem = theCurrentList.get(oldItemPosition);
        final T newItem = theNewList.get(newItemPosition);
        return itemCallback.areContentsTheSame(oldItem, newItem);
    }
}
