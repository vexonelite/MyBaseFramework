package tw.realtime.project.rtbaseframework.widgets.ui.fling;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.enumerations.SwipeDirection;
import tw.realtime.project.rtbaseframework.interfaces.ui.touch.TouchSwipeDirectionDelegate;

public class SwipeViewMovementHelper implements TouchSwipeDirectionDelegate {

    private final View swipeView;
    private int thresholdTranslationY = -1;
    private int maxTranslationY = -1;
    private float startY;

    public SwipeViewAppearDelegate swipeViewAppearCallback;

    public SwipeViewMovementHelper(@NonNull View swipeView) {
        this.swipeView = swipeView;
        swipeView.getViewTreeObserver().addOnGlobalLayoutListener(this::swipeViewOnGlobalLayoutCallback);
    }

    public SwipeViewMovementHelper(@NonNull View swipeView, @Nullable SwipeViewAppearDelegate callback) {
        this.swipeView = swipeView;
        swipeView.getViewTreeObserver().addOnGlobalLayoutListener(this::swipeViewOnGlobalLayoutCallback);
        this.swipeViewAppearCallback = callback;
    }

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    // implements ViewTreeObserver.OnGlobalLayoutListener
    private void swipeViewOnGlobalLayoutCallback () {
        swipeView.getViewTreeObserver().removeOnGlobalLayoutListener(this::swipeViewOnGlobalLayoutCallback);
        if (maxTranslationY < 0) {
            final Rect localVisibleRect = new Rect();
            swipeView.getLocalVisibleRect(localVisibleRect);
            maxTranslationY = localVisibleRect.bottom;
            thresholdTranslationY = (int)( ((float) maxTranslationY) / 3f);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "swipeView localVisibleRect: " + localVisibleRect
//                + ", maxTranslationY: " + maxTranslationY + ", thresholdTranslationY: " + thresholdTranslationY);
            new Handler(Looper.getMainLooper()).post(this::dismissSwipeView);
        }
    }

    // https://stackoverflow.com/questions/27462468/custom-view-overrides-ontouchevent-but-not-performclick
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchWithDirection(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
        final float currentTranslationY = swipeView.getTranslationY();
        LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - currentTranslationY: " + currentTranslationY);

//        final int action = motionEvent.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - ACTION_POINTER_UP");
//                break;
//        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startY = motionEvent.getY();
//                LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - startY: " + startY);
                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                animateSwipeView(motionEvent, direction);
                return true;

            case MotionEvent.ACTION_MOVE: {
                if (direction == SwipeDirection.UP) {
                    final float translationY = startY - motionEvent.getY();
                    final float newTranslationY = maxTranslationY - translationY;
//                    LogWrapper.showLog(Log.INFO, getLogTag(), "onTouch - translationY: " + translationY + ", newTranslationY: " + newTranslationY);
                    swipeView.setTranslationY(newTranslationY);
                }
                return (direction == SwipeDirection.UP);
            }
        }

        return false;
    }

    private void animateSwipeView(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
        if (direction != SwipeDirection.UP) {
            return;
        }

        final float currentPosition = swipeView.getTranslationY();
        final float translationY = startY - motionEvent.getY();
        final boolean hasAppeared = translationY > thresholdTranslationY;
        final float animateTo = hasAppeared ? 0f : swipeView.getHeight();

        final ObjectAnimator animator = ObjectAnimator.ofFloat(
                swipeView, "translationY", currentPosition, animateTo);
        animator.setDuration(200L);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        notifySwipeViewAppearCallbackIfNeeded();
                    }
                });
        animator.start();
    }

    public void dismissSwipeView () {
        swipeView.setTranslationY(maxTranslationY);
    }

    private void notifySwipeViewAppearCallbackIfNeeded () {
        if (null != swipeViewAppearCallback) {
            swipeViewAppearCallback.onSwipeViewAppeared();
        }
    }

    public interface SwipeViewAppearDelegate {
        void onSwipeViewAppeared();
    }
}
