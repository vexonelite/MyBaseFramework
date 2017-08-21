package tw.realtime.project.rtbaseframework.interfaces;

/**
 *
 * <p>
 * Created by vexonelite on 2017/07/06.
 */
public interface ShapeTouchDetector {
    /**
     * Determine if the given touch point (x, y) is in the circle.
     * @param xTouch get it from MotionEvent.getX()
     * @param yTouch MotionEvent.getY()
     * @return
     */
    boolean hasContained(float xTouch, float yTouch);
}
