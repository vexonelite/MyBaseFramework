package tw.realtime.project.rtbaseframework.widgets;

import android.graphics.RectF;

import tw.realtime.project.rtbaseframework.interfaces.ShapeTouchDetector;


/**
 * Created by vexonelite on 2017/7/5.
 */

public class ViewRect implements ShapeTouchDetector {

    private RectF mRect;

    @Override
    public boolean hasContained (float xTouch, float yTouch) {
        //LogWrapper.showLog(Log.INFO, "ViewRect", "hasContained - mRect: " + mRect);
        return ( (null != mRect) && (mRect.contains(xTouch, yTouch)) );
    }

    public static class Builder {
        private RectF bRect;

        public Builder setViewRectF (RectF rectF) {
            bRect = rectF;
            return this;
        }

        public ViewRect build () {
            return new ViewRect(this);
        }
    }

    private ViewRect(Builder builder) {
        mRect = builder.bRect;
    }
}
