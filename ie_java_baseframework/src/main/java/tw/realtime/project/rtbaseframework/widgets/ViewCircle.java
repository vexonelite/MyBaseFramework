package tw.realtime.project.rtbaseframework.widgets;


import tw.realtime.project.rtbaseframework.delegates.ShapeTouchDetector;

/**
 * Created by vexonelite on 2017/7/5.
 */

public class ViewCircle implements ShapeTouchDetector {

    private float mRadius;
    private float mLeftCorner;
    private float mTopCorner;

    @Override
    public boolean hasContained (float xTouch, float yTouch) {
        // (xTouch - (x + radius)) * (xTouch - (x + radius)) +
        // (yTouch - (y + radius)) * (yTouch - (y + radius)) <= radius * radius
        return  (xTouch - (mLeftCorner + mRadius)) * (xTouch - (mLeftCorner + mRadius)) +
                (yTouch - (mTopCorner + mRadius)) * (yTouch - (mTopCorner + mRadius)) <=
                (mRadius * mRadius);
    }

    public static class Builder {
        private float bRadius;
        private float bLeftCorner;
        private float bTopCorner;

        public Builder setRadius (float radius) {
            if (radius > 0f) {
                bRadius = radius;
            }
            return this;
        }

        //You should take position of the view with View.getX() and View.getY() to get x and y of the upper left corner

        /**
         * Left corner position of View. you can get it by calling View.getX()
         * @param coordinate Left corner position of View
         * @return
         */
        public Builder setLeftCorner (float coordinate) {
            bLeftCorner = coordinate;
            return this;
        }

        /**
         * Top corner position of View. you can get it by calling View.getY()
         * @param coordinate Top corner position of View
         * @return
         */
        public Builder setTopCorner (float coordinate) {
            bTopCorner = coordinate;
            return this;
        }

        public ViewCircle build () {
            return new ViewCircle(this);
        }
    }

    private ViewCircle (Builder builder) {
        mRadius = builder.bRadius;
        mLeftCorner = builder.bLeftCorner;
        mTopCorner = builder.bTopCorner;
    }
}
