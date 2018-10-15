package tw.realtime.project.rtbaseframework.interfaces.ui.touch;


import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;

/**
 * Created by vexonelite on 2018/10/02.
 */

public interface SwipeDirectionDetectorDelegate {
    /**
     * the callback of a captured swipe gesture
     */
    void onDirectionDetected(@NonNull SwipeDirection direction);
}
