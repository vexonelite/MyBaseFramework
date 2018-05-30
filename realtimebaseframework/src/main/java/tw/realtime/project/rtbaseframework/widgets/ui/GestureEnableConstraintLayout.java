package tw.realtime.project.rtbaseframework.widgets.ui;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;
import tw.realtime.project.rtbaseframework.interfaces.ui.touch.DoubleTapEventCallback;
import tw.realtime.project.rtbaseframework.interfaces.ui.touch.SingleTapEventCallback;
import tw.realtime.project.rtbaseframework.interfaces.ui.touch.SwipeEventCallback;


/**
 *
 * <p>
 * Created by vexonelite on 2018/03/28.
 * <p>
 * @see <a href="https://gist.github.com/romannurik/882650">Custom ViewGroup Reference 1</a>
 * @see <a href="http://stacktips.com/tutorials/android/how-to-create-custom-layout-in-android-by-extending-viewgroup-class">Custom ViewGroup Reference 2</a>
 * <p></p>
 */
public class GestureEnableConstraintLayout extends ConstraintLayout {

    private GestureDetector mSwipeGestureDetector;
    private GestureDetector.OnDoubleTapListener mInternalDoubleTapCallback;

    private SingleTapEventCallback mSingleTapCallback;
    private DoubleTapEventCallback mDoubleTapCallback;
    private SwipeEventCallback mSwipeCallback;


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }


    public void setSingleTapEventCallback (SingleTapEventCallback callback) {
        mSingleTapCallback = callback;
    }

    public void setDoubleTapEventCallback (DoubleTapEventCallback callback) {
        mDoubleTapCallback = callback;
    }

    public void setSwipeEventCallback (SwipeEventCallback callback) {
        mSwipeCallback = callback;
    }


    public GestureEnableConstraintLayout(Context context) {
        super(context);
        setupSwipeGestureDetector();
    }

    public GestureEnableConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupSwipeGestureDetector();
    }

    public GestureEnableConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupSwipeGestureDetector();
    }


    @Override
    public boolean onTouchEvent (MotionEvent event) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - event: " + event);
        // https://github.com/MikeOrtiz/TouchImageView
        mSwipeGestureDetector.onTouchEvent(event);
        return true;
    }

    //http://android.okhelp.cz/view-overrides-ontouchevent-but-not-performclick/
    @Override
    public boolean performClick() {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick();

        // Handle the action for the custom click here
        return true;
    }


    // setup the OnTouchListener, and pass the touch event to
    // the GestureDetector.SimpleOnGestureListener and SimpleOnGestureListener.OnDoubleTapListener.
    private void setupSwipeGestureDetector () {
        mInternalDoubleTapCallback = new DoubleTapCallback();
        mSwipeGestureDetector = new GestureDetector(getContext(), new SimpleGestureCallback());
    }

    // get the size of device's screen by excluding embedding control panel
    // (such as back button, home button, etc.)
    private Point getRealScreenSize () {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                manager.getDefaultDisplay().getRealSize(size);
            } catch (NoSuchMethodError e) {
                LogWrapper.showLog(Log.ERROR, "error", "it can't work");
            }
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            size.x = metrics.widthPixels;
            size.y = metrics.heightPixels;
        }
        return size;
    }

    // get the size of device's screen.
    private Point getScreenSize () {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        size.x = metrics.widthPixels;
        size.y = metrics.heightPixels;
        return size;
    }


    /**
     * Implementation of GestureDetector.OnDoubleTapListener.
     * <p>
     * Ref:
     * https://github.com/MikeOrtiz/TouchImageView
     */
    private class DoubleTapCallback implements GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onSingleTapConfirmed - motionEvent: " + motionEvent);
            if (null != mSingleTapCallback) {
                return mSingleTapCallback.onSingleTapConfirmed(motionEvent);
            }
            else {
                return false;
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onDoubleTap - motionEvent: " + motionEvent);
            return true;
        }

        // The event will get called several time. You should check the action of motion event,
        // and take acton for a certain type of action. e.g., ACTION_UP!
        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onDoubleTapEvent - motionEvent: " + motionEvent);
            if (null != mDoubleTapCallback) {
                return mDoubleTapCallback.onDoubleTapEvent(motionEvent);
            }
            else {
                return false;
            }
        }
    }

    /** Implementation of GestureDetector.SimpleOnGestureListener. */
    private class SimpleGestureCallback extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onSingleTapConfirmed - motionEvent: " + motionEvent);
            if (null != mInternalDoubleTapCallback) {
                return mInternalDoubleTapCallback.onSingleTapConfirmed(motionEvent);
            }
            else {
                return false;
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onDoubleTap - motionEvent: " + motionEvent);
            if (null != mInternalDoubleTapCallback) {
                return mInternalDoubleTapCallback.onDoubleTap(motionEvent);
            }
            else {
                return false;
            }
        }

        // The event will get called several time. You should check the action of motion event,
        // and take acton for a certain type of action. e.g., ACTION_UP!
        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "SimpleGestureCallback - onDoubleTapEvent - motionEvent: " + motionEvent);
            if (null != mInternalDoubleTapCallback) {
                return mInternalDoubleTapCallback.onDoubleTapEvent(motionEvent);
            }
            else {
                return false;
            }
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
            if (null != mSwipeCallback) {
                return mSwipeCallback.onSwipe(direction);
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
