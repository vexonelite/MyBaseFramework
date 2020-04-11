package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.delegates.ui.IePagerStateDelegate;
import tw.realtime.project.rtbaseframework.enumerations.IePageScrollState;
import tw.realtime.project.rtbaseframework.models.VisiblePageState;


public final class IePagerSnapScrollListener extends RecyclerView.OnScrollListener {

    private final IePagerStateDelegate externalDelegate;

    private final List<VisiblePageState> pageStates = new ArrayList<>();

    private final IePageScrollState[] statesArray = new IePageScrollState[] {
            IePageScrollState.IDLE, IePageScrollState.DRAGGING, IePageScrollState.SETTLING
    };

    public IePagerSnapScrollListener(
            @NonNull RecyclerView recyclerView, @NonNull IePagerStateDelegate externalDelegate) {
        this.externalDelegate = externalDelegate;
        recyclerView.addOnScrollListener(this);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        final RecyclerView.Adapter recyclerViewAdapter = recyclerView.getAdapter();
        if (null == recyclerViewAdapter) { return; }
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) { return; }
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        final List<VisiblePageState> pageStatesPool = new ArrayList<>();
        for (int i = 0; i < recyclerViewAdapter.getItemCount(); i++) {
            pageStatesPool.add(new VisiblePageState(0, recyclerView, 0, 0, 0f));
        }

        final int firstPos = layoutManager.findFirstVisibleItemPosition();
        final int lastPos = layoutManager.findLastVisibleItemPosition();

        final int screenEndX = recyclerView.getContext().getResources().getDisplayMetrics().widthPixels;
        final int midScreen = (screenEndX / 2);

        for (int position = firstPos; position <= lastPos; position++) {
            final View view = layoutManager.findViewByPosition(position);
            if (null != view) {
                final float viewWidth = (float) view.getMeasuredWidth();
                final float viewStartX = view.getX();
                final float viewEndX = viewStartX + viewWidth;
                if (viewEndX >= 0 && viewStartX <= screenEndX) {
                    final float viewHalfWidth = viewWidth / 2f;

                    final VisiblePageState pageState = pageStatesPool.get(position - firstPos);
                    pageState.index = position;
                    pageState.view = view;
                    pageState.viewCenterX = (int) (viewStartX + viewWidth / 2f);
                    pageState.distanceToSettledPixels = (pageState.viewCenterX - midScreen);
                    pageState.distanceToSettled = (pageState.viewCenterX + viewHalfWidth) / (midScreen + viewHalfWidth);
                    pageStates.add(pageState);
                }
            }
        }
        externalDelegate.onPageScroll(pageStates);

        // Clear this in advance so as to avoid holding refs to views.
        pageStates.clear();
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        externalDelegate.onScrollStateChanged(statesArray[newState]);
    }
}
