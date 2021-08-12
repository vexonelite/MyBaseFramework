package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.HolderCellClickDelegate;
import tw.realtime.project.rtbaseframework.delegates.ui.view.IdentifierCellTypeDelegate;
import tw.realtime.project.rtbaseframework.widgets.CommonItemWrapper;


public abstract class IeMultipleSelectionListAdapter<T extends IdentifierCellTypeDelegate>
        extends ListAdapter<T, RecyclerView.ViewHolder> {

    protected final AdapterMultiSelectionHelper<T> multiSelectionHelper = new AdapterMultiSelectionHelper<>();

    private AdapterSelection adapterSelection = AdapterSelection.SINGLE;

    public HolderCellClickDelegate<T> holderCellClickCallback;

    public IeMultipleSelectionListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) { super(diffCallback); }

    public class ItemClicker extends CommonItemWrapper<T> implements View.OnClickListener {

        public ItemClicker(@NonNull final T delegate, @NonNull final String action, final int position) {
            super(delegate, action, position);
        }

        @Override public void onClick(View view) {
            if (null != holderCellClickCallback) {
                holderCellClickCallback.onHolderCellClicked(getDataObject(), getAction(), getPosition());
            }
        }
    }

    public final void setAdapterSelection(@NonNull final AdapterSelection selection) {
        this.adapterSelection = selection;
        clearSelectionMap();
    }

    public final void clearSelectionMap() {
        try {
            multiSelectionHelper.clearSelectionMap();
            LogWrapper.showLog(Log.INFO, "IeMultipleSelectionListAdapter", "clearSelectionMap - [done]");
        }
        catch (InterruptedException cause) {
            LogWrapper.showLog(Log.ERROR, "IeMultipleSelectionListAdapter", "error on clearSelectionMap: [" + cause.getLocalizedMessage() + "]");
        }
    }

    @NonNull
    public final AdapterSelection getAdapterSelection() { return adapterSelection; }

    @NonNull
    public final List<String> getSelectedItemIdList() {
        return multiSelectionHelper.getSelectedItemIdList();
    }

    @NonNull
    public final List<T> getSelectedItemList() {
        return multiSelectionHelper.getSelectedItemList();
    }

    public final void updateListViaSelection(@NonNull final T delegate, final int selectedIndex) {
        //LogWrapper.showLog(Log.INFO, "IeMultipleSelectionListAdapter", "updateListViaSelection - BackupMode: " + backupMode);
        if (adapterSelection == AdapterSelection.MULTIPLE) {
            try {
                final int removedIndex = multiSelectionHelper.updateSelectionMapForMultipleSelection(delegate, selectedIndex);
                if (removedIndex > -1) {
                    notifyItemChanged(removedIndex);
                }
            }
            catch (InterruptedException cause) {
                LogWrapper.showLog(Log.ERROR, "IeMultipleSelectionListAdapter", "InterruptedException on updateListViaSelection[Delete]");
            }
        }
        else {
            try {
                final int previousSelectedIndex = multiSelectionHelper.updateSelectionMapForSingleSelection(delegate, selectedIndex);
                if (previousSelectedIndex > -1) {
                    notifyItemChanged(previousSelectedIndex);
                }
                notifyItemChanged(selectedIndex);
            }
            catch (InterruptedException cause) {
                LogWrapper.showLog(Log.ERROR, "IeMultipleSelectionListAdapter", "InterruptedException on updateListViaSelection[Non-Delete]");
            }
        }
    }

    @Nullable
    public final T getItemAtPosition(final int position) {
        try { return getItem(position); }
        catch (Exception cause) { return null; }
    }

    @Override
    public final int getItemViewType(final int position) {
        final T delegate = getItem(position);
        return delegate.theCellType();
    }

}
