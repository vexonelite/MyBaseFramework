package tw.realtime.project.rtbaseframework.widgets.viewpagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.ui.nestedscroll.TakeOverTouchEventDelegate;


/**
 * The Helper is used for the case where a {@link ViewPager} contains several {@link RecyclerView}
 * or {@link Fragment} whose main layout is {@link RecyclerView}.
 * <p>
 * The total cell height of {@link RecyclerView} might exceed the height of {@link ViewPager}.
 * In such condition, {@link RecyclerView} typically handles the swipe or scrolling events.
 * <p>
 * As a result, user only can see all content of a single {@link RecyclerView}.
 * In other words, she cannot see the content from reset of {@link RecyclerView}
 * because she cannot swipe via {@link ViewPager}.
 * <p>
 * To tackle the issue, the helper implements both
 * {@link RecyclerView#addOnItemTouchListener} and {@link RecyclerView#addOnScrollListener},
 * and keeps a reference of {@link TakeOverTouchEventDelegate}.
 * <p>
 * On the other hand, developer has to override the {@link ViewPager#onInterceptTouchEvent(MotionEvent)}
 * and returns the value received from {@link TakeOverTouchEventDelegate}.
 * <p>
 * The helper detects if {@link RecyclerView} cannot swipe upward of downward currently.
 * If it is the either case, the helper notifies {@link TakeOverTouchEventDelegate}
 * that now it is your turn to take over the following touch events.
 * <p>
 * In the moment, the subclass {@link ViewPager} has intercepted the touch event instantly and
 * handles the rest of touch events
 * <p>
 * @author VexonElite
 */
public final class RecyclerInsideViewPagerHelper extends RecyclerView.OnScrollListener
    implements RecyclerView.OnItemTouchListener {

    private int direction = 0;
    private final TakeOverTouchEventDelegate takeOverTouchEventCallback;

    public RecyclerInsideViewPagerHelper (@NonNull TakeOverTouchEventDelegate callback) {
        takeOverTouchEventCallback = callback;
    }

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

////// implementation of RecyclerView.OnItemTouchListener
    /**
     * Silently observe and/or take over touch events sent to the RecyclerView
     * before they are handled by either the RecyclerView itself or its child views.
     * <p>
     * <p>The onInterceptTouchEvent methods of each attached OnItemTouchListener will be run
     * in the order in which each listener was added, before any other touch processing
     * by the RecyclerView itself or child views occurs.</p>
     *
     * @param recyclerView
     * @param motionEvent  MotionEvent describing the touch event. All coordinates are in
     *           the RecyclerView's coordinate system.
     * @return true if this OnItemTouchListener wishes to begin intercepting touch events, false
     * to continue with the current behavior and continue observing future events in
     * the gesture.
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        final int action = motionEvent.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_MOVE");
                final boolean directionUp = recyclerView.canScrollVertically(-1);
                final boolean directionDown = recyclerView.canScrollVertically(1);
                LogWrapper.showLog(Log.INFO, getLogTag(), "canScrollVertically for Down: "
                        + directionDown + ", for Up: " + directionUp + ", direction: " + direction);
                if ( (direction > 0) && (!directionDown) ) {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "lock for for Down");
                    takeOverTouchEventCallback.timeToInterceptTouchEvent(true);
                } else if ( (direction < 0) && (!directionUp) ) {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "lock for for Up");
                    takeOverTouchEventCallback.timeToInterceptTouchEvent(true);
                }
                else {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "no lock");
                    //takeOverTouchEventCallback.timeToInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_UP");
                takeOverTouchEventCallback.timeToInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_CANCEL");
                takeOverTouchEventCallback.timeToInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_POINTER_UP");
                break;
        }

        return false;
    }

    /**
     * Process a touch event as part of a gesture that was claimed by returning true from
     * a previous call to {@link #onInterceptTouchEvent}.
     *
     * @param recyclerView
     * @param motionEvent  MotionEvent describing the touch event. All coordinates are in
     */
    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onTouchEvent - ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onTouchEvent - ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onTouchEvent - ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onTouchEvent - ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onTouchEvent - ACTION_POINTER_UP");
                break;
        }
    }

    /**
     * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
     * intercept touch events with
     * {@link ViewGroup#onInterceptTouchEvent(MotionEvent)}.
     *
     * @param disallowIntercept True if the child does not want the parent to
     *                          intercept touch events.
     * @see ViewParent#requestDisallowInterceptTouchEvent(boolean)
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


////// implementation of RecyclerView.OnScrollListener
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrolled for dy: " + dy);
        direction = dy;
//        if (dy > 0) {
//            // Scrolling down
//        } else {
//            // Scrolling up
//        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_IDLE");
            takeOverTouchEventCallback.timeToInterceptTouchEvent(false);
            direction = 0;
        }
    }

}
