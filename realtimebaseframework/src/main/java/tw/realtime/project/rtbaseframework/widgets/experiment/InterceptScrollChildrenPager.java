package tw.realtime.project.rtbaseframework.widgets.experiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.ui.nestedscroll.TakeOverTouchEventDelegate;
import tw.realtime.project.rtbaseframework.widgets.ui.HackyViewPager;

/**
 * please refers to {@link RecyclerInsideViewPagerHelper}.
 * The class implements {@link TakeOverTouchEventDelegate} and wait the result from
 * {@link TakeOverTouchEventDelegate#timeToInterceptTouchEvent}.
 */
public final class InterceptScrollChildrenPager extends HackyViewPager {

    public InterceptScrollChildrenPager(@NonNull Context context) {
        super(context);
    }

    public InterceptScrollChildrenPager(@NonNull Context context, @Nullable AttributeSet attrs) {
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
        return super.onTouchEvent(event);
    }

}
