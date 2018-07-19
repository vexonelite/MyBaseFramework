package tw.realtime.project.rtbaseframework.interfaces;

import android.support.annotation.NonNull;

public interface HolderCellClickListener<T> {
    void onHolderCellClicked(@NonNull T item, @NonNull String action, int position);
}
