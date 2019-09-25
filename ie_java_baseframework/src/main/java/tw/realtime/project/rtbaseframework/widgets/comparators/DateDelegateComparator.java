package tw.realtime.project.rtbaseframework.widgets.comparators;

import java.util.Comparator;
import java.util.Date;

import tw.realtime.project.rtbaseframework.delegates.ui.view.DateDelegate;


public final class DateDelegateComparator<T extends DateDelegate> implements Comparator<T> {

    private final boolean isAscending;

    public DateDelegateComparator(boolean isAscending) { this.isAscending = isAscending; }

    @Override
    public int compare(T item1, T item2) {
        if ( (null == item1) || (null == item2) ) {
            return 0;
        }

        final Date date1 = item1.theDate();
        final Date date2 = item2.theDate();
        int result = date1.compareTo(date2);
        if (result > 0) { // date1 is after date2
            return isAscending ? 1 : -1;
            //for order by ascending
            // return 1;
            // for order by descending
            //return -1;
        } else if (result < 0) { // date1 is before date2
            return isAscending ? -1 : 1;
            //for order by ascending
            //return -1;
            // for order by descending
            //return 1;
        } else { // date1 is equal date2
            return 0;
        }
    }
}
