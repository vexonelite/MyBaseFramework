package tw.realtime.project.rtbaseframework.interfaces.ui.touch;

import android.view.MotionEvent;

/**
 * Created by vexonelite on 2018/2/8.
 */

public interface SingleTapEventCallback {
    boolean onSingleTapConfirmed(MotionEvent motionEvent);
}