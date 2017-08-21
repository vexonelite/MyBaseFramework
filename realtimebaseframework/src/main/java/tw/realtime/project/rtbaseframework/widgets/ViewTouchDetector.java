package tw.realtime.project.rtbaseframework.widgets;

import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import tw.realtime.project.rtbaseframework.interfaces.ShapeTouchDetector;


/**
 * Created by vexonelite on 2017/7/6.
 */

public class ViewTouchDetector implements View.OnTouchListener {

    private ShapeTouchDetector mTouchDetector;

    private DetectorCallback mCallback;

    private Shape mShape = Shape.RECTANGLE;

    private State mState = State.DEFAULT;


    public enum Shape {
        RECTANGLE,
        CIRCLE
    }

    public enum State {
        BEGIN,
        END,
        KEEP,
        DEFAULT;
    }

    public interface DetectorCallback {
        void onTouch(State state);
    }


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }


    public ViewTouchDetector setShape (Shape shape) {
        if (null != shape) {
            mShape = shape;
        }
        return this;
    }

    public ViewTouchDetector setDetectorCallback (DetectorCallback callback) {
        mCallback = callback;
        return this;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

//        switch (motionEvent.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_MOVE");
//                break;
//        }

        if (null == mTouchDetector) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getMeasuredWidth(): " + view.getMeasuredWidth());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getWidth(): " + view.getWidth());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getMeasuredHeight(): " + view.getMeasuredHeight());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getHeight(): " + view.getHeight());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getLeft(): " + view.getLeft());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getTop(): " + view.getTop());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getRight(): " + view.getRight());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - view.getBottom(): " + view.getBottom());

            switch (mShape) {
                case CIRCLE: {
                    float radius = ( (float) view.getMeasuredWidth()) / 2f;
                    float leftCorner = 0f;  // do not use view.getLeft();
                    float topCorner = 0f;   // do not use view.getTop();
                    mTouchDetector = new ViewCircle.Builder()
                            .setRadius(radius)
                            .setLeftCorner(leftCorner)
                            .setTopCorner(topCorner)
                            .build();
                    break;
                }
                case RECTANGLE: {
                    //RectF rectF = new RectF (view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    RectF rectF = new RectF(0f, 0f, view.getMeasuredWidth(), view.getMeasuredHeight());
                    mTouchDetector = new ViewRect.Builder().setViewRectF(rectF).build();
                    break;
                }
            }
        }

        float xTouch = motionEvent.getX();
        float yTouch = motionEvent.getY();
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - xTouch: " + xTouch + ", yTouch: " + yTouch);

        boolean result = mTouchDetector.hasContained(xTouch, yTouch);
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - result: " + result);

        if (result) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_DOWN");
                    mState = State.BEGIN;
                    if (null != mCallback) {
                        mCallback.onTouch(State.BEGIN);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_UP");
                    mState = State.END;
                    if (null != mCallback) {
                        mCallback.onTouch(State.END);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mState = State.END;
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_CANCEL");
                    if (null != mCallback) {
                        mCallback.onTouch(State.BEGIN);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    mState = State.KEEP;
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - ACTION_MOVE");
                    break;
            }
        }
        else {
            if ( (mState == State.BEGIN) || (mState == State.KEEP) ) {
                mState = State.DEFAULT;
                if (null != mCallback) {
                    mCallback.onTouch(State.END);
                }
            }
        }

        //LogWrapper.showLog(Log.INFO, getLogTag(), "onTouchEvent - mState: " + mState);

        return result;
    }
}
