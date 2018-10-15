package tw.realtime.project.rtbaseframework.widgets.google.slidestrip;

import androidx.annotation.NonNull;
import android.view.View;

import tw.realtime.project.rtbaseframework.interfaces.ui.tab.TabItemDelegate;
import tw.realtime.project.rtbaseframework.interfaces.ui.tab.TabViewDelegate;

public interface TabAdapterDelegate<T extends TabItemDelegate> extends TabViewDelegate<T> {
    /**
     * Determine if the tabView is selected by checking if (position == currentPage)
     *
     * @param tabView
     * @param position The index of tabView
     * @param currentPage The position of selected tabView
     */
    void onDemandToUpdateTabView(@NonNull View tabView, int position, int currentPage);
}
