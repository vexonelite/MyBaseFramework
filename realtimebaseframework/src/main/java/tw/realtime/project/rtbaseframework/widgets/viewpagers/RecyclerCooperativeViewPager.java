package tw.realtime.project.rtbaseframework.widgets.viewpagers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.ui.nestedscroll.TakeOverTouchEventDelegate;

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
                LogWrapper.showLog(Log.INFO, "RecyclerCooperativeViewPager", "onInterceptTouchEvent - ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogWrapper.showLog(Log.INFO, "RecyclerCooperativeViewPager", "onInterceptTouchEvent - ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogWrapper.showLog(Log.INFO, "RecyclerCooperativeViewPager", "onInterceptTouchEvent - ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogWrapper.showLog(Log.INFO, "RecyclerCooperativeViewPager", "onInterceptTouchEvent - ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                LogWrapper.showLog(Log.INFO, "RecyclerCooperativeViewPager", "onInterceptTouchEvent - ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                LogWrapper.showLog(Log.INFO, "RecyclerCooperativeViewPager", "onInterceptTouchEvent - ACTION_POINTER_UP");
                break;
        }
        return super.onInterceptHoverEvent(event) || canInterceptTouchEvent;
    }

    @Override
    public void timeToInterceptTouchEvent(boolean canInterceptTouchEvent) {
        this.canInterceptTouchEvent = canInterceptTouchEvent;
    }
}
