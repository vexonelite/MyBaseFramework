package tw.realtime.project.rtbaseframework.widgets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public abstract class CommonItemWrapper<T> {

    private static final int MIN_CLICK_TIME_DIFFERENCE = 500;

    private long mLastClickTime = 0;
    private final int mPosition;
    private final String mAction;
    private final T mObject;


    /**
     * Test if the time difference between the current click event and the last click event
     * exceeds the pre-defined threshold.
     */
    protected boolean canHandleClickEvent() {
        long currentTime = System.currentTimeMillis();
        if ( (currentTime - mLastClickTime) > MIN_CLICK_TIME_DIFFERENCE) {
            mLastClickTime = currentTime;
            return true;
        }
        else {
            return false;
        }
    }

    public CommonItemWrapper(@NonNull T object, @Nullable String action, int position) {
        mObject = object;
        mAction = (null != action) ? action : "";
        mPosition = position;
    }

    public int getPosition () {
        return mPosition;
    }

    public String getAction () {
        return mAction;
    }

    @NonNull
    public T getDataObject () {
        return mObject;
    }
}
