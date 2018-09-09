package tw.realtime.project.rtbaseframework.widgets.experiment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.ui.nestedscroll.TakeOverTouchEventDelegate;


/**
 *
 * @author VexonElite
 */
public final class RecyclerInsideViewPagerImpl2 extends RecyclerInsideViewPagerHelper {

    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;

    public RecyclerInsideViewPagerImpl2(@NonNull TakeOverTouchEventDelegate callback) {
        super(callback);
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
                break;
            case MotionEvent.ACTION_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onInterceptTouchEvent - ACTION_POINTER_UP");
                break;
        }
        final boolean isDragging = (scrollState == RecyclerView.SCROLL_STATE_DRAGGING);
        takeOverTouchEventCallback.timeToInterceptTouchEvent(isDragging);
        return false;
    }

////// implementation of RecyclerView.OnScrollListener
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        scrollState = newState;
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_IDLE");
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_DRAGGING");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                LogWrapper.showLog(Log.INFO, getLogTag(), "RecyclerView#onScrollStateChanged - SCROLL_STATE_SETTLING");
                break;
        }
    }

}
