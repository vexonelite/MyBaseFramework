package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.realtime.project.rtbaseframework.delegates.ui.IePagerStateDelegate;


public final class CardPeekPagerSnapHelperVerbose extends PagerSnapHelperVerbose {

    public CardPeekPagerSnapHelperVerbose(
            @NonNull RecyclerView recyclerView, @NonNull IePagerStateDelegate externalDelegate) {
        super(recyclerView, externalDelegate);
    }

    @Override
    public int[] calculateDistanceToFinalSnap(
            @NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        final int position = recyclerView.getChildAdapterPosition(targetView);
        // If first or last view, the default implementation works.
        if ((position == 0) || (position == layoutManager.getChildCount() - 1)) {
            super.calculateDistanceToFinalSnap(layoutManager, targetView);
        }
        // Force centering in the view without its decorations.
        final int targetCenter = targetView.getLeft() + targetView.getWidth() / 2;
        final int distance = targetCenter - recyclerView.getWidth() / 2;
        return new int[] {distance, 0 };
    }
}
