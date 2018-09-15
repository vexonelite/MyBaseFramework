package tw.realtime.project.rtbaseframework.widgets.experiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;
import tw.realtime.project.rtbaseframework.interfaces.ui.nestedscroll.TakeOverTouchEventDelegate;
import tw.realtime.project.rtbaseframework.widgets.ui.HackyViewPager;

/**
 * please refers to {@link RecyclerInsideViewPagerHelper}.
 * The class implements {@link TakeOverTouchEventDelegate} and wait the result from
 * {@link TakeOverTouchEventDelegate#timeToInterceptTouchEvent}.
 */
public final class GestureDetectionPager extends HackyViewPager {

    private final GestureDetector swipeGestureDetector;

    public GestureDetectionPager(@NonNull Context context) {
        super(context);
        swipeGestureDetector = new GestureDetector(context, new SimpleGestureCallback());
    }

    public GestureDetectionPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        swipeGestureDetector = new GestureDetector(context, new SimpleGestureCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        boolean result = false;
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onInterceptTouchEvent - ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onInterceptTouchEvent - ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onInterceptTouchEvent - ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onInterceptTouchEvent - ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onInterceptTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onInterceptTouchEvent - ACTION_POINTER_UP");
                break;
        }

        return true;
    }

    // https://stackoverflow.com/questions/27462468/custom-view-overrides-ontouchevent-but-not-performclick
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onTouchEvent - ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogWrapper.showLog(Log.INFO, getLogTag(), "R onTouchEvent - ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogWrapper.showLog(Log.WARN, getLogTag(), "R onTouchEvent - ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.WARN, getLogTag(), "R onTouchEvent - ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "R onTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "R onTouchEvent - ACTION_POINTER_UP");
                break;
        }

        final boolean resultFromSuper = super.onTouchEvent(event);
        final boolean resultFromGesture = swipeGestureDetector.onTouchEvent(event);
        LogWrapper.showLog(Log.WARN, getLogTag(), "R onTouchEvent - resultFromGesture: " + resultFromGesture);
        return resultFromGesture || resultFromSuper;
    }


    /** Implementation of GestureDetector.SimpleOnGestureListener. */
    private class SimpleGestureCallback extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onSingleTapConfirmed - motionEvent: " + motionEvent);
//            if (null != mInternalDoubleTapCallback) {
//                return mInternalDoubleTapCallback.onSingleTapConfirmed(motionEvent);
//            }
//            else {
//                return false;
//            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onDoubleTap - motionEvent: " + motionEvent);
//            if (null != mInternalDoubleTapCallback) {
//                return mInternalDoubleTapCallback.onDoubleTap(motionEvent);
//            }
//            else {
//                return false;
//            }
            return false;
        }

        // The event will get called several time. You should check the action of motion event,
        // and take acton for a certain type of action. e.g., ACTION_UP!
        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onDoubleTapEvent - motionEvent: " + motionEvent);
//            if (null != mInternalDoubleTapCallback) {
//                return mInternalDoubleTapCallback.onDoubleTapEvent(motionEvent);
//            }
//            else {
//                return false;
//            }
            return false;
        }

        // https://github.com/MikeOrtiz/TouchImageView
        // http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down/26387629#26387629
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onFling - e1: " + e1 + ", e2: " + e2
                    + ", velocityX: " + velocityX + ", velocityY: " + velocityY);

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

            float x1 = e1.getX();
            float y1 = e1.getY();
            float x2 = e2.getX();
            float y2 = e2.getY();
            SwipeDirection direction = getDirection(x1, y1, x2, y2);
            LogWrapper.showLog(Log.INFO, getLogTag(), "VhcSimpleGestureListener - onFling: " + direction);
            return onSwipe(direction);
            //return super.onFling(e1, e2, velocityX, velocityY);
        }

        /** Override this method. The Direction enum will tell you how the user swiped. */
        private boolean onSwipe(SwipeDirection direction) {
//            if (null != mSwipeCallback) {
//                return mSwipeCallback.onSwipe(direction);
//            }
//            else {
//                return false;
//            }
            return false;
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
