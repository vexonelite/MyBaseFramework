package tw.realtime.project.rtbaseframework.interfaces.ui.touch;

/**
 * Created by vexonelite on 2018/10/05.
 */

public interface TouchDownEventDelegate {
    /**
     * Notify that 'ACTION_DOWN' MotionEvent happenes
     */
    void onTouchDown(/* @NonNull MotionEvent motionEvent */ );
}
