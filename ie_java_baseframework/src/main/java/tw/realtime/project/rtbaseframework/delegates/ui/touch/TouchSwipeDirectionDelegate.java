package tw.realtime.project.rtbaseframework.delegates.ui.touch;


import androidx.annotation.NonNull;
import android.view.MotionEvent;

import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;

/**
 * Created by vexonelite on 2018/10/02.
 */

public interface TouchSwipeDirectionDelegate {
    /**
     * the callback of a captured swipe gesture
     */
    boolean onTouchWithDirection(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction);
}
