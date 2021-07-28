package tw.realtime.project.rtbaseframework.widgets.experiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.ui.nestedscroll.TakeOverTouchEventDelegate;
import tw.realtime.project.rtbaseframework.widgets.ui.HackyViewPager;

/**
 * please refers to {@link RecyclerInsideViewPagerHelper}.
 * The class implements {@link TakeOverTouchEventDelegate} and wait the result from
 * {@link TakeOverTouchEventDelegate#timeToInterceptTouchEvent}.
 */
public final class RecyclerCooperativeViewPager extends HackyViewPager implements TakeOverTouchEventDelegate {

    private boolean canInterceptTouchEvent = false;

    public RecyclerCooperativeViewPager(@NonNull Context context) {
        super(context);
    }

    public RecyclerCooperativeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
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

        //return super.onInterceptTouchEvent(event) || canInterceptTouchEvent;
        final boolean result = super.onInterceptTouchEvent(event) || canInterceptTouchEvent;
        LogWrapper.showLog(Log.INFO, getLogTag(), "onInterceptTouchEvent - canInterceptTouchEvent: " + canInterceptTouchEvent + ", return value: " + result);
        return result;
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
                canInterceptTouchEvent = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.WARN, getLogTag(), "R onTouchEvent - ACTION_CANCEL");
                canInterceptTouchEvent = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "R onTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "R onTouchEvent - ACTION_POINTER_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

        @Override
    public void timeToInterceptTouchEvent(boolean canInterceptTouchEvent) {
        LogWrapper.showLog(Log.WARN, getLogTag(), "timeToInterceptTouchEvent - canInterceptTouchEvent: " + canInterceptTouchEvent);
        this.canInterceptTouchEvent = canInterceptTouchEvent;
    }
}
