package tw.realtime.project.rtbaseframework.widgets.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.R;


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
 * <p/>
 * revision in 2018/09/07
 * <p>
 * I have added swipe vertically feature.
 * @see <a href="https://www.journaldev.com/19336/android-nested-viewpager-vertical-viewpager">Android Nested ViewPager, Vertical ViewPager</a>
 * @see <a href="https://medium.com/@jimitpatel/viewpager-with-vertical-swiping-ability-e40200094281">ViewPager with vertical Swiping ability</a>
 * <p/>
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final float MIN_SCALE = 0.75f;

    /** Lock for touch event */
    private boolean isLocked;
    private boolean canScrollHorizontally;

    private int swipeOrientation = HORIZONTAL;


    public HackyViewPager(@NonNull Context context) {
        super(context);
        isLocked = false;
        canScrollHorizontally = true;
        swipeOrientation = HORIZONTAL;
        initSwipeMethods();
    }

    public HackyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
        canScrollHorizontally = true;
        setSwipeOrientation(context, attrs);
        initSwipeMethods();
    }

    protected final String getLogTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "[] onInterceptTouchEvent");
        //version 3 - support both lock and swipe vertically
        if (isLocked) {
            return false;
        }
        else {
            final boolean resultFromSuper = super.onInterceptHoverEvent(swapXY(event));
            //LogWrapper.showLog(Log.INFO, getLogTag(), "[] onInterceptTouchEvent - resultFromSuper: " + resultFromSuper);
            swapXY(event);
            return resultFromSuper;
        }

        //version 2
//        return (!isLocked) && (null != getAdapter()) && (getAdapter().getCount() > 0);

        //version 1
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

    // https://stackoverflow.com/questions/27462468/custom-view-overrides-ontouchevent-but-not-performclick
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean resultFromSuper = super.onTouchEvent(swipeOrientation == VERTICAL ? swapXY(event) : event);
        //LogWrapper.showLog(Log.INFO, getLogTag(), "[] onTouchEvent - resultFromSuper: " + resultFromSuper);
        // version 2 - support both lock and swipe vertically
        return (!isLocked) && resultFromSuper;

        //version 1
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
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return canScrollHorizontally;
    }

    public final void setCanScrollHorizontallyFlag (boolean flag) {
        canScrollHorizontally = flag;
    }

    public final void toggleLock() {
        isLocked = !isLocked;
    }

    public final void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public final boolean isLocked() {
        return isLocked;
    }

    private void setSwipeOrientation(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (null == attrs) {
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HackyViewPager);
        swipeOrientation = typedArray.getInteger(R.styleable.HackyViewPager_swipe_orientation, 0);
        typedArray.recycle();
    }

    private void initSwipeMethods() {
        if (swipeOrientation == VERTICAL) {
            // The majority of the work is done over here
            setPageTransformer(true, this::verticalPageTransformer);
            //setPageTransformer(true, this::verticalPageTransformerAnimate);

            // The easiest way to get rid of the overscroll drawing that happens on the left and right
            setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        final float width = getWidth();
        final float height = getHeight();

        final float newX = (ev.getY() / height) * width;
        final float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }


    /**
     * implements ViewPager.PageTransformer (void transformPage(@NonNull View page, float position)) via method reference.
     * <p></p>
     * We create a custom implementation of PageTransformer class where
     * instead of translating the view horizontally using translationX,
     * we do so vertically using translationY.
     * Same is done for motion events when the user swipes on the screen.
     */
    private void verticalPageTransformer (@NonNull View page, float position) {
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            page.setAlpha(1);

            // Counteract the default slide transition
            page.setTranslationX(page.getWidth() * -position);

            //set Y position to swipe in from top
            final float yPosition = position * page.getHeight();
            page.setTranslationY(yPosition);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }
    }

    /**
     * implements ViewPager.PageTransformer (void transformPage(@NonNull View page, float position)) via method reference.
     */
    private void verticalPageTransformerAnimate(@NonNull View page, float position) {

        final int pageWidth = page.getWidth();
        final int pageHeight = page.getHeight();
        float alpha = 0;
        if (0 <= position && position <= 1) {
            alpha = 1 - position;
        }
        else if (-1 < position && position < 0) {
            final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            final float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
            final float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horizontalMargin - verticalMargin / 2);
            }
            else {
                page.setTranslationX(-horizontalMargin + verticalMargin / 2);
            }

            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            alpha = position + 1;
        }

        page.setAlpha(alpha);
        page.setTranslationX(page.getWidth() * -position);
        final float yPosition = position * page.getHeight();
        page.setTranslationY(yPosition);
    }
}
