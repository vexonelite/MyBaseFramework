package tw.realtime.project.rtbaseframework.delegates.ui.touch;

import androidx.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by vexonelite on 2018/2/8.
 */

public interface DoubleTapEventDelegate {
    /**
     * The event will get called several time. You should check the action of motion event,
     * and take acton for a certain type of action. e.g., ACTION_UP!
     * @param motionEvent
     * @return
     */
    boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent);
}
