package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import tw.realtime.project.rtbaseframework.delegates.ui.IePagerStateDelegate;


public class PagerSnapHelperVerbose extends PagerSnapHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    protected final RecyclerView recyclerView;
    private final IePagerStateDelegate externalDelegate;

    private int lastPage = RecyclerView.NO_POSITION;

    public PagerSnapHelperVerbose(
            @NonNull RecyclerView recyclerView, @NonNull IePagerStateDelegate externalDelegate) {
        this.recyclerView = recyclerView;
        this.externalDelegate = externalDelegate;
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public final void onGlobalLayout() {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            final int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (position != RecyclerView.NO_POSITION) {
                notifyNewPageIfNeeded(position);
            }
        }
        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public final View findSnapView(RecyclerView.LayoutManager layoutManager) {
        final View view = super.findSnapView(layoutManager);
        if (null != view) { notifyNewPageIfNeeded(recyclerView.getChildAdapterPosition(view)); }
        return view;
    }

    @Override
    public final int findTargetSnapPosition(
            RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        final int position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        final RecyclerView.Adapter recyclerViewAdapter  = recyclerView.getAdapter();
        if ( (null != recyclerViewAdapter) && (position < recyclerViewAdapter.getItemCount()) ) { // Making up for a "bug" in the original snap-helper.
            notifyNewPageIfNeeded(position);
        }
        return position;
    }

    private void notifyNewPageIfNeeded(final int page) {
        if (page != lastPage) {
            externalDelegate.onPageSelected(page);
            lastPage = page;
        }
    }
}
