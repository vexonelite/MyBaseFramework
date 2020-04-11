package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tw.realtime.project.rtbaseframework.enumerations.ScaleDirection;


/**
 * @see <a href="https://stackoverflow.com/questions/32823713/how-to-scale-up-recycler-view-center-item-while-scrolling-in-android/46924548">Reference 1</a>
 * @see <a href="https://stackoverflow.com/questions/41307578/recycler-view-resizing-item-view-while-scrolling-for-carousel-like-effect?rq=1">Reference 2</a>
 */
public final class HorizontalCenterZoomLinearLayoutManager extends LinearLayoutManager {

    private final float shrinkDistance;
    private final float shrinkAmount;
    private final ScaleDirection scaleDirection;

    public HorizontalCenterZoomLinearLayoutManager(
            Context context, boolean reverseLayout) {
        super(context, LinearLayoutManager.HORIZONTAL, reverseLayout);
        this.shrinkDistance = 0.9f;
        this.shrinkAmount = 0.15f;
        this.scaleDirection = ScaleDirection.BOTH;
    }

    public HorizontalCenterZoomLinearLayoutManager(
            @NonNull Context context,
            boolean reverseLayout,
            final float shrinkDistance,
            final float shrinkAmount,
            final ScaleDirection scaleDirection) {
        super(context, LinearLayoutManager.HORIZONTAL, reverseLayout);
        this.shrinkDistance = shrinkDistance;
        this.shrinkAmount = shrinkAmount;
        this.scaleDirection = scaleDirection;
    }


    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() == HORIZONTAL) {
            final int resultFromSuper = scrollHorizontallyBy(dx, recycler, state);
            scaleChildren();
            return resultFromSuper;
        }
        else { return 0; }
    }

    private void scaleChildren() {
        final float midpoint = ((float) getWidth()) / 2f;
        final float d1 = shrinkDistance * midpoint;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (null != child) {
                final float distanceToCenter = Math.abs(midpoint -
                        ( (float)(getDecoratedRight(child) + getDecoratedLeft(child)) ) / 2f);
                final float d = Math.min(d1, distanceToCenter);
                final float scaleFactor = 1f - shrinkAmount * d / d1;
                switch (scaleDirection) {
                    case BOTH: {
                        child.setScaleX(scaleFactor);
                        child.setScaleY(scaleFactor);
                        break;
                    }
                    case HORIZONTAL: { child.setScaleX(scaleFactor); break; }
                    case VERTICAL: { child.setScaleY(scaleFactor); break; }
                }
            }
        }
    }
}
