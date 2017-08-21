package tw.realtime.project.rtbaseframework.widgets;

import android.view.View;


public abstract class BaseItemClicker<T> implements View.OnClickListener {

    private static final int MIN_CLICK_TIME_DIFFERENCE = 500;

    private long mLastClickTime = 0;
    private int mPosition;
    private String mAction;
    private T mObject;


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

    public BaseItemClicker(String action, T object) {
        mAction = action;
        mObject = object;
    }

    public BaseItemClicker(T object, int position) {
        mObject = object;
        mPosition = position;
    }

    public BaseItemClicker(T object, String action, int position) {
        mObject = object;
        mAction = action;
        mPosition = position;
    }

    public int getPosition () {
        return mPosition;
    }

    public String getAction () {
        return mAction;
    }

    public T getDataObject () {
        return mObject;
    }
}
