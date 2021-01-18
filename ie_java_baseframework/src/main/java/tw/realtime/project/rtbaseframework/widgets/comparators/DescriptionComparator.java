package tw.realtime.project.rtbaseframework.widgets.comparators;

import java.util.Comparator;

import tw.realtime.project.rtbaseframework.delegates.ui.view.DescriptionDelegate;


public final class DescriptionComparator<T extends DescriptionDelegate> implements Comparator<T> {
    @Override
    public int compare(T item1, T item2) {
        if ((null == item1) || (null == item2)) { return 0; }
        return item1.theDescription().compareTo(item2.theDescription());
    }
}