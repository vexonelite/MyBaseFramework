package tw.realtime.project.rtbaseframework.widgets;

import androidx.annotation.NonNull;


public abstract class CommonItemWrapper<T> {

    private static final int MIN_CLICK_TIME_DIFFERENCE = 500;

    private long mLastClickTime = 0;
    private final int mPosition;
    @NonNull
    private final String mAction;
    @NonNull
    private final T mObject;


    /**
     * Test if the time difference between the current click event and the last click event
     * exceeds the pre-defined threshold.
     */
    final protected boolean canHandleClickEvent() {
        long currentTime = System.currentTimeMillis();
        if ( (currentTime - mLastClickTime) > MIN_CLICK_TIME_DIFFERENCE) {
            mLastClickTime = currentTime;
            return true;
        }
        else {
            return false;
        }
    }

    public CommonItemWrapper(@NonNull T object, @NonNull String action, int position) {
        mObject = object;
        mAction = action;
        mPosition = position;
    }

    final public int getPosition () {
        return mPosition;
    }

    @NonNull
    final public String getAction () {
        return mAction;
    }

    @NonNull
    final public T getDataObject () {
        return mObject;
    }
}
