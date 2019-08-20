package tw.realtime.project.rtbaseframework.widgets.ui;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;
import tw.realtime.project.rtbaseframework.delegates.ui.touch.SwipeEventDelegate;


/**
 *
 */
public final class HackyRecyclerView extends RecyclerView {

    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;

    private final GestureDetector swipeGestureDetector;

    public SwipeEventDelegate swipeCallback;

    public HackyRecyclerView(Context context) {
        super(context);
        swipeGestureDetector = new GestureDetector(context, new SimpleGestureCallback());
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        swipeGestureDetector = new GestureDetector(context, new SimpleGestureCallback());
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        swipeGestureDetector = new GestureDetector(context, new SimpleGestureCallback());
    }

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public void addOnItemTouchListener () {
        addOnItemTouchListener(new OnItemTouchCallback());
    }

    private class OnItemTouchCallback implements RecyclerView.OnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            swipeGestureDetector.onTouchEvent(motionEvent);
//            final int maxScroll = recyclerView.computeVerticalScrollRange();
//            final int currentScroll = recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent();
//            LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - maxScroll: " + maxScroll
//                    + ", currentScroll: " + currentScroll);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void addOnScrollListener () {
        addOnScrollListener(new OnScrollCallback());
    }

    private class OnScrollCallback extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            scrollState = newState;
//            switch (newState) {
//                case RecyclerView.SCROLL_STATE_IDLE:
//                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_IDLE");
//                    break;
//                case RecyclerView.SCROLL_STATE_DRAGGING:
//                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_DRAGGING");
//                    break;
//                case RecyclerView.SCROLL_STATE_SETTLING:
//                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_SETTLING");
//                    break;
//            }
        }
    }

    private class SimpleGestureCallback extends GestureDetector.SimpleOnGestureListener {

        // https://github.com/MikeOrtiz/TouchImageView
        // http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down/26387629#26387629
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onFling - e1: " + e1 + ", e2: " + e2
//                    + ", velocityX: " + velocityX + ", velocityY: " + velocityY);

            // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
            // Let e1 be the initial event
            // e2 can be located at 4 different positions, consider the following diagram
            // (Assume that lines are separated by 90 degrees.)
            //
            //
            //         \ A  /
            //          \  /
            //       D   e1   B
            //          /  \
            //         / C  \
            //
            // So if (x2,y2) falls in region:
            //  A => it's an UP swipe
            //  B => it's a RIGHT swipe
            //  C => it's a DOWN swipe
            //  D => it's a LEFT swipe
            //

            final float x1 = e1.getX();
            final float y1 = e1.getY();
            final float x2 = e2.getX();
            final float y2 = e2.getY();
            final SwipeDirection direction = getDirection(x1, y1, x2, y2);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onFling: " + direction);
            return onSwipe(direction);
            //return super.onFling(e1, e2, velocityX, velocityY);
        }

        /** Override this method. The Direction enum will tell you how the user swiped. */
        private boolean onSwipe(SwipeDirection direction) {
            if (scrollState != SCROLL_STATE_DRAGGING) {
                return false;
            }

//            final boolean canScrollUpWard = canScrollVertically(-1);
//            final boolean canScrollDownWard = canScrollVertically(1);
//            LogWrapper.showLog(Log.WARN, getLogTag(), "SimpleGestureCallback - onSwipe - canScrollUpWard: " + canScrollUpWard
//            + ", canScrollDownWard: " + canScrollDownWard);
//            if ( (direction == SwipeDirection.DOWN) && (!canScrollUpWard) ) {
//                LogWrapper.showLog(Log.WARN, getLogTag(), "SimpleGestureCallback - onSwipe - ViewPager Previous Page!");
//            }
//            else if ( (direction == SwipeDirection.UP) && (!canScrollDownWard) ) {
//                LogWrapper.showLog(Log.WARN, getLogTag(), "SimpleGestureCallback - onSwipe - ViewPager Next Page!");
//            }

            if (null != swipeCallback) {
                return swipeCallback.onSwipe(direction);
            }
            else {
                return false;
            }

        }

        /**
         * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
         * returns the direction that an arrow pointing from p1 to p2 would have.
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @return the direction
         */
        private SwipeDirection getDirection(float x1, float y1, float x2, float y2){
            double angle = getAngle(x1, y1, x2, y2);
            return SwipeDirection.get(angle);
        }

        /**
         *
         * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
         * The angle is measured with 0/360 being the X-axis to the right, angles
         * increase counter clockwise.
         *
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @return the angle between two points
         */
        private double getAngle(float x1, float y1, float x2, float y2) {
            double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
            return (rad * 180 / Math.PI + 180) % 360;
        }
    }

}



