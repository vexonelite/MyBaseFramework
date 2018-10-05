package tw.realtime.project.rtbaseframework.widgets.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Interpolator;

public abstract class BaseAnimator {

    private boolean clickLock = false;
    protected final View refView;


    public BaseAnimator(@NonNull View refView) {
        this.refView = refView;
    }


    protected final String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public final boolean isClickLocked() {
        return clickLock;
    }

    public final void playAnimation () {
        if (clickLock) {
            return;
        }
        final ValueAnimator animator = getAnimator();
        animator.addListener(new MyAnimationCallback());
        animator.start();
    }

    @NonNull
    protected abstract ValueAnimator getAnimator ();


    public void playInitialAnimation () {

    }

    private class MyAnimationCallback extends DefaultAnimationCallback {
        @Override
        public void onAnimationStart(Animator animator) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onAnimationStart");
            clickLock = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onAnimationEnd");
            clickLock = false;
        }
    }

    protected final ValueAnimator createPathAnimator (@NonNull Path path,
                                                      long duration,
                                                      @NonNull Interpolator interpolator) {

        final ValueAnimator animator;
        if (Build.VERSION.SDK_INT >= 21) {
            animator = ObjectAnimator.ofFloat(refView, View.X, View.Y, path);
        }
        else {
            // Animates a float value from 0 to 1
            animator = ValueAnimator.ofFloat(0.0f, 1.0f);

            // This listener onAnimationUpdate will be called during every step in the animation
            // Gets called every millisecond in my observation
            animator.addUpdateListener(new PathAnimatorUpdateCallback(path));
        }

        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.addListener(new MyAnimationCallback());
        return animator;
    }

    private class PathAnimatorUpdateCallback implements ValueAnimator.AnimatorUpdateListener {

        private final float[] pointArray = {0f, 0f};
        private final PathMeasure pathMeasure;

        private PathAnimatorUpdateCallback (@NonNull Path path) {
//            pathMeasure = new PathMeasure(path, true);
            pathMeasure = new PathMeasure(path, false);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // Gets the animated float fraction
            final float fraction = animation.getAnimatedFraction();
            pathMeasure.getPosTan(pathMeasure.getLength() * fraction, pointArray, null);
            refView.setTranslationX(pointArray[0]);
            refView.setTranslationY(pointArray[1]);
        }
    }


    protected final PointF getRecCenter (@NonNull Rect rect) {
        final float xPoint = ((float) rect.left) + ( ( ((float)rect.right) - ((float) rect.left) ) / 2f );
        final float yPoint = ((float) rect.top) + ( ( ((float)rect.bottom) - ((float) rect.top) ) / 2f );
        return new PointF(xPoint, yPoint);
    }

    /*
    private void drawByLineTo () {
        final Path path = new Path();
        final PointF startPoint = getRecCenter(leftBottomRect);
        path.moveTo(startPoint.x, startPoint.y);
        Log.i(getLogTag(), "startPoint: " + startPoint);

        final PointF stepPoint1 = getRecCenter(leftCenterRect);
        path.lineTo(stepPoint1.x, stepPoint1.y);
        Log.i(getLogTag(), "stepPoint1: " + stepPoint1);

        final PointF stepPoint2 = getRecCenter(centerTopRect);
        path.lineTo(stepPoint2.x, stepPoint2.y);
        Log.i(getLogTag(), "stepPoint2: " + stepPoint2);

        final PointF stepPoint3 = getRecCenter(rightCenterRect);
        path.lineTo(stepPoint3.x, stepPoint3.y);
        Log.i(getLogTag(), "stepPoint3: " + stepPoint3);

        final PointF destination = getRecCenter(rightBottomRect);
        path.lineTo(destination.x, destination.y);
        Log.i(getLogTag(), "destination: " + destination);

        drawPathView.setDrawingPath(path);
        createPathAnimator(path, 700L, new AccelerateDecelerateInterpolator());
    }

    private void drawByCubicTo () {
        final Path path = new Path();
        final PointF startPoint = getRecCenter(leftCenterRect);
        path.moveTo(startPoint.x, startPoint.y);
        Log.i(getLogTag(), "startPoint: " + startPoint);

        final PointF stepPoint1 = getRecCenter(centerTopRect);
        final PointF stepPoint2 = getRecCenter(rightCenterRect);
        final PointF destination = getRecCenter(centerBottomRect);
        Log.i(getLogTag(), "stepPoint1: " + stepPoint1);
        Log.i(getLogTag(), "stepPoint2: " + stepPoint2);
        Log.i(getLogTag(), "destination: " + destination);
        path.cubicTo(
                stepPoint1.x, stepPoint1.y,
                stepPoint2.x, stepPoint2.y,
                destination.x, destination.y);
        drawPathView.setDrawingPath(path);
        createPathAnimator(path, 700L, new AccelerateDecelerateInterpolator());
    }

    private void drawByQuadTo () {
        final Path path = new Path();
        final PointF startPoint = getRecCenter(leftCenterRect);
        path.moveTo(startPoint.x, startPoint.y);
        Log.i(getLogTag(), "startPoint: " + startPoint);

        final PointF stepPoint1 = getRecCenter(centerTopRect);
        final PointF destination = getRecCenter(rightBottomRect);
        Log.i(getLogTag(), "stepPoint1: " + stepPoint1);
        Log.i(getLogTag(), "destination: " + destination);
        path.quadTo(
                stepPoint1.x, stepPoint1.y,
                destination.x, destination.y);
        drawPathView.setDrawingPath(path);
        createPathAnimator(path, 700L, new AccelerateDecelerateInterpolator());
    }
    */

}
