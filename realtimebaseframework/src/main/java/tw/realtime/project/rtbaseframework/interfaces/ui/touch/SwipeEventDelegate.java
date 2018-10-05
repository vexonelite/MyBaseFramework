package tw.realtime.project.rtbaseframework.interfaces.ui.touch;


import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;

/**
 * Created by vexonelite on 2018/2/8.
 */

public interface SwipeEventDelegate {
    /**
     * the callback of a captured swipe gesture
     */
    boolean onSwipe(SwipeDirection direction);
}
