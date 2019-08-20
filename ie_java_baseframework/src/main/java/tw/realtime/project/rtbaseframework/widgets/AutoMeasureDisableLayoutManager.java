package tw.realtime.project.rtbaseframework.widgets;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * I come across a problem where
 * the width item looks like wrap content instead match parent.
 * My layout has a constraint layout as container, then
 * there is only one TextView inside.
 * TextView's width supposes to be matched the width of constraint layout.
 * However, the constraint layout only wrap the content of TextView when app runs.
 * I think this might be a bug at the moment.
 * I google the issue and find the link
 * https://stackoverflow.com/questions/35904409/item-in-recyclerview-not-filling-its-width-match-parent
 * and 'anqe1ki11er' mentioned that "Since API v27.1.0 we need to extend the LinearLayoutManager and
 * override the isAutoMeasureEnabled() function to return false
 * because setAutoMeasureEnabled() method is removed."
 */
public final class AutoMeasureDisableLayoutManager extends LinearLayoutManager {

    private boolean isAutoMeasureEnabled = true;

    /**
     * Creates a vertical LinearLayoutManager
     *
     * @param context Current context, will be used to access resources.
     */
    public AutoMeasureDisableLayoutManager(Context context) {
        super(context);
    }

    /**
     * @param context       Current context, will be used to access resources.
     * @param orientation   Layout orientation. Should be {@link #HORIZONTAL} or {@link
     *                      #VERTICAL}.
     * @param reverseLayout When set to true, layouts from end to start.
     */
    public AutoMeasureDisableLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to vertical orientation.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_android_orientation
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_reverseLayout
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_stackFromEnd
     */
    public AutoMeasureDisableLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return isAutoMeasureEnabled;
    }

    public void setIsAutoMeasureEnabledFlag (boolean flag) {
        isAutoMeasureEnabled = flag;
    }
}
