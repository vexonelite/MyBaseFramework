package tw.realtime.project.rtbaseframework.interfaces.ui.touch;

import android.view.MotionEvent;

/**
 * Created by vexonelite on 2018/2/8.
 */

public interface DoubleTapEventCallback {
    /**
     * The event will get called several time. You should check the action of motion event,
     * and take acton for a certain type of action. e.g., ACTION_UP!
     * @param motionEvent
     * @return
     */
    boolean onDoubleTapEvent(MotionEvent motionEvent);
}
