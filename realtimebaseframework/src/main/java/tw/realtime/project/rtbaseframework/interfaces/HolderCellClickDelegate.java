package tw.realtime.project.rtbaseframework.interfaces;

import androidx.annotation.NonNull;

public interface HolderCellClickDelegate<T> {
    void onHolderCellClicked(@NonNull T item, @NonNull String action, int position);
}