package tw.realtime.project.rtbaseframework.widgets.ui.fling;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;
import tw.realtime.project.rtbaseframework.interfaces.ui.touch.TouchDownEventDelegate;


/**
 *
 * <p>
 * Created by vexonelite on 2018/10/02.
 * <p>
 * @see <a href="https://stfalcon.com/en/blog/post/learning-android-gestures">Learning Android gestures</a>
 * <p>
 */
public final class FlingDispatchFrameLayout extends FrameLayout {

    private SwipeDirectionDetector swipeDirectionDetector;
    private SwipeDirection swipeDirection;

    private RecyclerView refRecyclerView;

    private SwipeViewMovementHelper swipeViewMovementHelper;

    public TouchDownEventDelegate touchDownEventCallback;


    public FlingDispatchFrameLayout(Context context) {
        super(context);
    }

    public FlingDispatchFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingDispatchFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }


    public void doInitialization(@IdRes int recyclerViewId,
                                 @NonNull View swipeView) {
        final Context context = getContext();
        refRecyclerView = findViewById(recyclerViewId);
//        if (null != refRecyclerView) {
//            refRecyclerView.addOnItemTouchListener(new RecyclerViewOnItemTouchCallback());
//        }

        swipeViewMovementHelper = new SwipeViewMovementHelper(swipeView);

        swipeDirectionDetector = new SwipeDirectionDetector(context);
        swipeDirectionDetector.swipeDirectionDetectorCallback = this::onDirectionDetected;
    }


    /** implements SwipeDirectionDetectorDelegate via method reference */
    private void onDirectionDetected(@NonNull SwipeDirection direction) {
        this.swipeDirection = direction;
        LogWrapper.showLog(Log.INFO, getLogTag(), "onDirectionDetected - direction: " + direction);
    }

    /** implements SwipeViewAppearDelegate via method reference */
    private void onSwipeViewAppeared() {

    }

    public void dismissSwipeView () {
        if (null != swipeViewMovementHelper) {
            swipeViewMovementHelper.dismissSwipeView();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
//        final int action = motionEvent.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - ACTION_POINTER_UP");
//                break;
//        }

        onUpDownEvent(motionEvent);

        swipeDirectionDetector.onTouchEvent(motionEvent);
        if (null != swipeDirection) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - swipeDirection: " + swipeDirection);
            switch (swipeDirection) {
                case UP: {
                    return dispatchTouchEventToSwipeViewIfNeed(motionEvent, swipeDirection);
                }
                case DOWN: {
                    return dispatchTouchEventToSwipeViewIfNeed(motionEvent, swipeDirection);
                }
                case LEFT: {
                    return dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
                }
                case RIGHT: {
                    return dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
                }
            }
        }
//        else {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEvent - swipeDirection is null!");
//        }

        return true;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
//        final int action = motionEvent.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - ACTION_POINTER_UP");
//                break;
//        }
//
//        return super.onInterceptTouchEvent(motionEvent);
//    }

    // https://stackoverflow.com/questions/27462468/custom-view-overrides-ontouchevent-but-not-performclick
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent motionEvent) {
//        final int action = motionEvent.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_POINTER_UP");
//                break;
//        }
//        return super.onTouchEvent(motionEvent);
//    }


    private void onUpDownEvent(@NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            onActionDown(motionEvent);
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            onActionUp(motionEvent);
        }
    }

    private void onActionDown(@NonNull MotionEvent motionEvent) {
        swipeDirection = null;
//        LogWrapper.showLog(Log.INFO, getLogTag(), "onActionDown - reset swipeDirection");
        if (null != touchDownEventCallback) {
            touchDownEventCallback.onTouchDown();
        }
        dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
        dispatchTouchEventToSwipeViewIfNeed(motionEvent, SwipeDirection.NOT_DETECTED);
    }

    private void onActionUp(@NonNull MotionEvent motionEvent) {
        dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
        dispatchTouchEventToSwipeViewIfNeed(motionEvent, SwipeDirection.NOT_DETECTED);
    }

    private boolean dispatchTouchEventToRecyclerViewIfNeed(@NonNull MotionEvent motionEvent) {
//        LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEventToRecyclerViewIfNeed");
        final boolean handled;
        if (null != refRecyclerView) {
            handled = refRecyclerView.dispatchTouchEvent(motionEvent);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEventToRecyclerViewIfNeed: " + handled);
        }
        else {
            handled = false;
        }
        return handled;
    }


    private boolean dispatchTouchEventToSwipeViewIfNeed(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
//        LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEventToSwipeViewIfNeed");
        final boolean handled;
        if (null != swipeViewMovementHelper) {
            handled = swipeViewMovementHelper.onTouchWithDirection(motionEvent, direction);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "dispatchTouchEventToSwipeViewIfNeed: " + handled);
        }
        else {
            handled = false;
        }
        return handled;
    }

    private class RecyclerViewOnItemTouchCallback implements RecyclerView.OnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onInterceptTouchEvent - ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onInterceptTouchEvent - ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onInterceptTouchEvent - ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onInterceptTouchEvent - ACTION_CANCEL");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onInterceptTouchEvent - ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onInterceptTouchEvent - ACTION_POINTER_UP");
                    break;
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onTouchEvent - ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onTouchEvent - ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onTouchEvent - ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onTouchEvent - ACTION_CANCEL");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView # onTouchEvent - ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_POINTER_UP");
                    break;
            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
