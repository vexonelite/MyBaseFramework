package tw.realtime.project.rtbaseframework.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Found at http://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager.
 * Convenient way to temporarily disable ViewPager navigation while interacting with ImageView.
 *
 * Julia Zudikova
 */

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

    private boolean isLocked;

    public HackyViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return (!isLocked) && (null != getAdapter()) && (getAdapter().getCount() > 0);
//        if (!isLocked) {
//            try {
//                if (getAdapter() == null || getAdapter().getCount() == 0) {
//                    return false;
//                } else {
//                    return super.onInterceptTouchEvent(event);
//                }
//            } catch (RuntimeException e) {
//                LogWrapper.showLog(Log.ERROR, "HackyViewPager", "RuntimeException on onInterceptTouchEvent", e);
//                return false;
//            }
//        }
//        return false;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (isLocked) {
//            return false;
//        } else {
//            if (getAdapter() == null || getAdapter().getCount() == 0) {
//                return false;
//            } else {
//                try {
//                    return super.onTouchEvent(event);
//                } catch (RuntimeException e) {
//                    LogWrapper.showLog(Log.ERROR, "HackyViewPager", "RuntimeException on onTouchEvent", e);
//                    return false;
//                }
//            }
//        }
//    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
