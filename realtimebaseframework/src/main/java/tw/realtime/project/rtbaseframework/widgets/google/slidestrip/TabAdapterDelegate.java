package tw.realtime.project.rtbaseframework.widgets.google.slidestrip;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import tw.realtime.project.rtbaseframework.interfaces.ui.view.TabItemDelegate;

public interface TabAdapterDelegate<T extends TabItemDelegate> {
    @NonNull
    View getTabView(@NonNull ViewGroup parent, int position, @NonNull T tabItem);

    /**
     * Determine if the tabView is selected by checking if (position == currentPage)
     *
     * @param tabView
     * @param position The index of tabView
     * @param currentPage The position of selected tabView
     */
    void onDemandToUpdateTabView(@NonNull View tabView, int position, int currentPage);
}
