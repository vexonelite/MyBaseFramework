package tw.realtime.project.rtbaseframework.enumerations;

/**
 * 列舉類別，用來指定所偵測到的手勢是向左、向上、向右或向下。
 * <p>
 * Created by vexonelite on 2018/2/8.
 * <p>
 * http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down/26387629#26387629
 */

public enum SwipeDirection {

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
}
