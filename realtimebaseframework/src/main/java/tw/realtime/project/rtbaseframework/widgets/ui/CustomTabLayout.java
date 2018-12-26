package tw.realtime.project.rtbaseframework.widgets.ui;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;

import tw.realtime.project.rtbaseframework.LogWrapper;

public final class CustomTabLayout extends TabLayout {

    private static final int WIDTH_INDEX = 0;
    private static final int DIVIDER_FACTOR = 3;
    // legacy support library
    //private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";
    // Android X:
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "scrollableTabMinWidth";


    public CustomTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();
    }

    private void initTabMinWidth() {
//        final int[] wh = Utils.getScreenSize(getContext());
//        final int tabMinWidth = wh[WIDTH_INDEX] / DIVIDER_FACTOR;
        final float density = getContext().getResources().getDisplayMetrics().density;
        final int tabMinWidth = (int)(density * 20f);

        try {
            final Field field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            LogWrapper.showLog(Log.ERROR, "CustomTabLayout", "NoSuchFieldException on initTabMinWidth", e);
        }
        catch (IllegalAccessException e) {
            LogWrapper.showLog(Log.ERROR, "CustomTabLayout", "IllegalAccessException on initTabMinWidth", e);
        }
    }
}
