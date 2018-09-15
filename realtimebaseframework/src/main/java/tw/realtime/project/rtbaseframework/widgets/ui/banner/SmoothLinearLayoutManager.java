package tw.realtime.project.rtbaseframework.widgets.ui.banner;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class SmoothLinearLayoutManager extends LinearLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 100f;

    private MyLinearSmoothScroller myLinearSmoothScroller;

    public SmoothLinearLayoutManager(Context context) {
        super(context);
        myLinearSmoothScroller = new MyLinearSmoothScroller(context);
    }

    public SmoothLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        myLinearSmoothScroller = new MyLinearSmoothScroller(context);
    }

    public SmoothLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        myLinearSmoothScroller = new MyLinearSmoothScroller(context);
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state,
                                       int position) {

        // Docs do not tell us anything about this,
        // but we need to set the position we want to scroll to.
        myLinearSmoothScroller.setTargetPosition(position);

        // Call startSmoothScroll(SmoothScroller)? Check.
        startSmoothScroll(myLinearSmoothScroller);
    }


    private class MyLinearSmoothScroller extends LinearSmoothScroller {

        private MyLinearSmoothScroller(Context context) {
            super(context);
        }

        // This controls the direction in which smoothScroll looks for your list item
        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
        // What is PointF? A class that just holds two float coordinates.
        // Accepts a (x , y)
        // for y: use -1 for up direction, 1 for down direction.
        // for x (did not test): use -1 for left direction, 1 for right direction.
        // We let our custom LinearLayoutManager calculate PointF for us
            return SmoothLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }


        // The holy grail of smooth scrolling
        // returns the milliseconds it takes to scroll one pixel.

        // calculateSpeedPerPixel(DisplayMetrics displayMetrics) allows you
        // to adjust how fast you want the SmoothScroller to scroll over a pixel on your screen!

        // Some devices have 480 pixels in an inch and some have 240 pixels in an inch.
        // More pixels means that it will take longer to scroll one inch as compared to a lower dpi device.
        // Therefore, we need to find an equation that can solve that problem:

        // MILLISECONDS_PER_INCH is how fast we want it to scroll one inch.
        // We divide it by displayMetrics.densityDpi because we want that speed
        // to look consistent through different devices with different pixel densities.
        // Higher dpi devices will have to scroll through pixels at a faster rate than
        // that of a lower dpi device to keep up. I used 50 ms/inch but you can do whatever you like.
        @Override
        public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
        }
    }

}
