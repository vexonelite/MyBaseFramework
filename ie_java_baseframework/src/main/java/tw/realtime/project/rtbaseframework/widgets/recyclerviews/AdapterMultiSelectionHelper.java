package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.ui.view.IdentifierDelegate;


public final class AdapterMultiSelectionHelper<T extends IdentifierDelegate> {

    private final Semaphore mutex = new Semaphore(1);
    private final Map<String, T> selectionMap = new HashMap<>();
    private final Map<String, Integer> selectionIndexMap = new HashMap<>();


    private String getLogTag() { return this.getClass().getSimpleName(); }

    public void clearSelectionMap() throws InterruptedException {
        this.mutex.acquire();
        this.selectionMap.clear();
        this.mutex.release();
    }

    @NonNull
    public List<String> getSelectedBackupIdList() {
        final Set<String> keySet = selectionMap.keySet();
        return new ArrayList<>(keySet);
    }

    @NonNull
    public List<T> getSelectedBackupList() {
        final Set<String> keySet = selectionMap.keySet();
        final List<T> resultList = new ArrayList<>();
        for (final String key : keySet) {
            resultList.add(selectionMap.get(key));
        }
        return resultList;
    }

    public boolean doesBelongToSelectionMap(@NonNull final T delegate) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "doesBelongToSelectionMap - current: " + delegate);
//            final Set<String> keySet = selectionMap.keySet();
//            for (final String key : keySet) {
//                LogWrapper.showLog(Log.INFO, getLogTag(), "doesBelongToSelectionMap - key: " + key);
//            }
        return selectionMap.containsKey(delegate.theIdentifier());
    }

    public int updateSelectionMapForMultipleSelection(@NonNull final T delegate, final int selectedIndex) throws InterruptedException {
        final boolean hasExisted = selectionMap.containsKey(delegate.theIdentifier());
        if (hasExisted) {
            this.mutex.acquire();
            final Integer removedIndex = selectionIndexMap.remove(delegate.theIdentifier());
            final T removedItem = selectionMap.remove(delegate.theIdentifier());
            this.mutex.release();
            if ( (null != removedIndex) && (null != removedItem) ) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "updateSelectionMapForMultipleSelection - removedIndex -> " + removedIndex);
                return removedIndex;
            }
            else { return -1; }
        }
        else {
            this.mutex.acquire();
            selectionMap.put(delegate.theIdentifier(), delegate);
            selectionIndexMap.put(delegate.theIdentifier(), selectedIndex);
            this.mutex.release();
            LogWrapper.showLog(Log.INFO, getLogTag(), "updateSelectionMapForMultipleSelection - addedIndex -> " + selectedIndex);
            return selectedIndex;
        }
    }

    public int updateSelectionMapForSingleSelection(@NonNull final T delegate, final int selectedIndex) throws InterruptedException {

        final int previousSelectedIndex = getCurrentSelectedIndexSingleSelection();
        //LogWrapper.showLog(Log.INFO, getLogTag(), "updateSelectionMapForSingleSelection - previousSelectedIndex: " + previousSelectedIndex);
        if (previousSelectedIndex == selectedIndex) {
            return -1;
        }
        this.mutex.acquire();
        selectionMap.clear();
        selectionIndexMap.clear();
        //LogWrapper.showLog(Log.INFO, getLogTag(), "updateSelectionMapForSingleSelection - clear -> " + selectionMap.size());
        selectionMap.put(delegate.theIdentifier(), delegate);
        selectionIndexMap.put(delegate.theIdentifier(), selectedIndex);
        //LogWrapper.showLog(Log.INFO, getLogTag(), "updateSelectionMapForSingleSelection - put -> " + selectionMap.size());
        this.mutex.release();

        if (previousSelectedIndex >= 0) { return previousSelectedIndex; }
        else { return -1; }
    }

    public int getCurrentSelectedIndexSingleSelection() {
        final List<String> list = getSelectedBackupIdList();
        //LogWrapper.showLog(Log.INFO, getLogTag(), "getCurrentSelectedIndexSingleSelection - list.size: " + list.size());
        if (list.size() == 1) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "getCurrentSelectedIndexSingleSelection - key: " + list.get(0));
            final Integer result = selectionIndexMap.get(list.get(0));
            return (null != result) ? result : -1;
        }
        else { return -1; }
    }
}