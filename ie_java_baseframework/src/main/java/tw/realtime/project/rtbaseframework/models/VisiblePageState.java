package tw.realtime.project.rtbaseframework.models;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;

import tw.realtime.project.rtbaseframework.widgets.recyclerviews.IePagerSnapScrollListener;

/**
 * {@link RecyclerView} relevant
 * see {@link IePagerSnapScrollListener }
 */
public final class VisiblePageState {
    public int index;
    public View view;
    @Px
    public int viewCenterX;
    @Px
    public int distanceToSettledPixels;
    public float distanceToSettled;

    public VisiblePageState(
            int index,
            @NonNull View view,
            @Px int viewCenterX,
            @Px int distanceToSettledPixels,
            float distanceToSettled) {
        this.index = index;
        this.view = view;
        this.viewCenterX = viewCenterX;
        this.distanceToSettledPixels = distanceToSettledPixels;
        this.distanceToSettled = distanceToSettled;
    }
}
