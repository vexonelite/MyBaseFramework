package tw.realtime.project.rtbaseframework.widgets.comparators;

import java.util.Comparator;


public final class IeStringComparator implements Comparator<String> {
    @Override
    public int compare(String item1, String item2) {
        if ((null == item1) || (null == item2)) { return 0; }
        return item1.compareTo(item2);
    }
}