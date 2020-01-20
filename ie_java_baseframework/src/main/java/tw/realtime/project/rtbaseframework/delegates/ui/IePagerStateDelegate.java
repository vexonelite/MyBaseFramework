package tw.realtime.project.rtbaseframework.delegates.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.realtime.project.rtbaseframework.enumerations.IePageScrollState;
import tw.realtime.project.rtbaseframework.models.VisiblePageState;
import tw.realtime.project.rtbaseframework.widgets.recyclerviews.IePagerSnapScrollListener;
import tw.realtime.project.rtbaseframework.widgets.recyclerviews.PagerSnapHelperVerbose;

/**
 * {@link RecyclerView} relevant
 * see {@link PagerSnapHelperVerbose}, {@link IePagerSnapScrollListener},
 */
public interface IePagerStateDelegate {
    void onPageScroll(@NonNull List<VisiblePageState> pagesState);

    void onScrollStateChanged(@NonNull IePageScrollState state);

    void onPageSelected(int index);
}
