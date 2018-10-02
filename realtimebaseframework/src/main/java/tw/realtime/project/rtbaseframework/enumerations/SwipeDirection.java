package tw.realtime.project.rtbaseframework.enumerations;

/**
 * 列舉類別，用來指定所偵測到的手勢是向左、向上、向右或向下。
 * <p>
 * Created by vexonelite on 2018/2/8.
 * <p>
 * http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down/26387629#26387629
 */

public enum SwipeDirection {

    NOT_DETECTED,
    LEFT,
    UP,
    RIGHT,
    DOWN;

    /**
     * Returns a direction given an angle.
     * Directions are defined as follows:
     *
     * Up: [45, 135]
     * Right: [0,45] and [315, 360]
     * Down: [225, 315]
     * Left: [135, 225]
     *
     * @param angle an angle from 0 to 360 - e
     * @return the direction of an angle
     */
    public static SwipeDirection get(double angle){
        if(inRange(angle, 45, 135)){
            return SwipeDirection.UP;
        }
        else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
            return SwipeDirection.RIGHT;
        }
        else if(inRange(angle, 225, 315)){
            return SwipeDirection.DOWN;
        }
        else{
            return SwipeDirection.LEFT;
        }
    }

    /**
     * @param angle an angle
     * @param init the initial bound
     * @param end the final bound
     * @return returns true if the given angle is in the interval [init, end).
     */
    private static boolean inRange(double angle, float init, float end) {
        return (angle >= init) && (angle < end);
    }

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    public static SwipeDirection getDirection(float x1, float y1, float x2, float y2) {
        final double angle = getAngle(x1, y1, x2, y2);
        return SwipeDirection.get(angle);
    }

    /**
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    public static double getAngle(float x1, float y1, float x2, float y2) {
        final double rad = Math.atan2(y1 - y2, x2 - x1) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }
}
