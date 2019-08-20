package tw.realtime.project.rtbaseframework.widgets.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.R;

/**
 * Created by vexonelite on 2016/10/27.
 */

public class ScrollingTextLayout extends LinearLayout {

    private List<RectF> mViewReferenceRectList;
    private List<AnimatedTextView> mTextViewHolder;

    private AnimatorSet mAnimatorSet;

    private boolean doesEnableAnimatorSet;

    private long mDuration;
    private Interpolator mInterpolator;

    private UpcomingTextListener mCallback;

    public interface UpcomingTextListener {
        String onInitialTextRequested();
        String onUpcomingTextRequested();
    }


    private static String getLogTag () {
        return ScrollingTextLayout.class.getSimpleName();
    }

    public ScrollingTextLayout(Context context) {
        super(context);
        init();
    }

    public ScrollingTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollingTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init () {
        mDuration = 5000;
        mInterpolator = new LinearInterpolator();
    }

    public void setDuration (long duration) {
        if (duration > 0) {
            mDuration = duration;
        }
    }

    public void setInterpolator (Interpolator interpolator) {
        if (null != interpolator) {
            mInterpolator = interpolator;
        }
    }

    public void setUpcomingTextListener (UpcomingTextListener callback) {
        mCallback = callback;
    }

    public void registerGlobalLayoutEvent () {
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        constructReferenceRectList();
                        removeOnGlobalLayoutEvent(ScrollingTextLayout.this, this);
                        if (doesEnableAnimatorSet) {
                            try {
                                constructInitialAnimation();
                                mAnimatorSet.start();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * @param target The View that you want to remove its GlobalLayoutListener.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void removeOnGlobalLayoutEvent(View target, ViewTreeObserver.OnGlobalLayoutListener listener){
        if (Build.VERSION.SDK_INT < 16) {
            target.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            target.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public void addAnimatedTextView (float heightDp) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final float density = getContext().getResources().getDisplayMetrics().density;
        final int height = (int) (density * heightDp);

        if (mTextViewHolder == null) {
            mTextViewHolder = new ArrayList<>();
        }
        else {
            mTextViewHolder.clear();
        }

        /*
        int[] colorArray = {
                (Build.VERSION.SDK_INT >= 23)
                        ? getContext().getResources().getColor(android.R.color.darker_gray, null)
                        : getContext().getResources().getColor(android.R.color.darker_gray),
                (Build.VERSION.SDK_INT >= 23)
                        ? getContext().getResources().getColor(android.R.color.holo_blue_light, null)
                        : getContext().getResources().getColor(android.R.color.holo_blue_light),
                (Build.VERSION.SDK_INT >= 23)
                        ? getContext().getResources().getColor(android.R.color.holo_green_light, null)
                        : getContext().getResources().getColor(android.R.color.holo_green_light),
                (Build.VERSION.SDK_INT >= 23)
                        ? getContext().getResources().getColor(android.R.color.holo_orange_light, null)
                        : getContext().getResources().getColor(android.R.color.holo_orange_light)
        };
        */

        for (int i = 0; i < 4; i++) {
            AnimatedTextView textView = (AnimatedTextView) inflater.inflate(
                    R.layout.base_animated_textview, ScrollingTextLayout.this, false);
            //textView.setBackgroundColor(colorArray[i]);
            //String text = "Hello World " + i;
            //textView.setText (text);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, height);

            if (i == 0) {
                params.setMargins(0, (-2 * height), 0, 0);
            }
            else {
                if (i == 2) {
                    String text = "";
                    if (null != mCallback) {
                        String givenStr = mCallback.onInitialTextRequested();
                        if (null != givenStr) {
                            text = text + givenStr;
                        }
                    }
                    textView.setText (text);
                }

                mTextViewHolder.add(textView);
                textView.updatePlace(i);
                textView.updateCurrentPlace(i);
            }

            addView(textView, params);
        }
    }

    private void constructReferenceRectList () {
        if (mViewReferenceRectList == null) {
            mViewReferenceRectList = new ArrayList<>();
        }
        else {
            mViewReferenceRectList.clear();
        }

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            AnimatedTextView textView = (AnimatedTextView) getChildAt(i);
            if (null != textView) {
                RectF rectF = new RectF();
                rectF.set(textView.getLeft(), textView.getTop(),
                        textView.getRight(), textView.getBottom());
                mViewReferenceRectList.add(rectF);
            }
        }

        for (RectF rectF : mViewReferenceRectList) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "" + rectF);
        }
    }

    public void stopAnimation () {
        doesEnableAnimatorSet = false;
    }

    //http://stackoverflow.com/questions/23603813/how-to-translate-animation-on-an-image-diagonally
    public void startAnimation () {
        doesEnableAnimatorSet = true;
        if (null == mAnimatorSet) {
            try {
                constructInitialAnimation();
                mAnimatorSet.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void constructInitialAnimation () {
        LogWrapper.showLog(Log.INFO, getLogTag(), "constructInitialAnimation");

        ArrayList<ObjectAnimator> animatorList = new ArrayList<>();

        for (AnimatedTextView textView : mTextViewHolder) {

            final int initPlace = textView.getPlace();
            final int currentPlace = textView.getCurrentPlace();
            RectF currentRectF = mViewReferenceRectList.get(initPlace);
            RectF destRectF = mViewReferenceRectList.get(currentPlace - 1);

            if (currentPlace == 3) {
                String text = "";
                if (null != mCallback) {
                    String givenStr = mCallback.onUpcomingTextRequested();
                    if (null != givenStr) {
                        text = text + givenStr;
                    }
                }
                textView.setText (text);
            }

            float verticalDiff = (destRectF.top + destRectF.bottom -
                    currentRectF.top - currentRectF.bottom) / 2f;

            LogWrapper.showLog(Log.INFO, getLogTag(), "animate - initPlace: " + initPlace + ", currentPlace: " + currentPlace
                    + ", verticalDiff: " + verticalDiff);

            ObjectAnimator translationYani = ObjectAnimator.ofFloat(textView, "translationY", verticalDiff);
            translationYani.setDuration(mDuration);
            translationYani.setInterpolator(mInterpolator);
            animatorList.add(translationYani);
        }

        ObjectAnimator[] animatorArray = animatorList.toArray(new ObjectAnimator[animatorList.size()]);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animatorArray);
        mAnimatorSet.addListener(new AnimationCallback());
    }

    private class DefaultAnimationCallback implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "DefaultAnimationCallback - onAnimationEnd");
            mAnimatorSet = null;
            if (doesEnableAnimatorSet) {
                try {
                    constructInitialAnimation();
                    mAnimatorSet.start();
                }
                catch (Exception e) {
                    LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on DefaultAnimationCallback - onAnimationEnd", e);
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    private class AnimationCallback extends DefaultAnimationCallback {

        @Override
        public void onAnimationEnd(Animator animator) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "AnimationCallback - onAnimationEnd");

            mAnimatorSet = null;

            for (AnimatedTextView textView : mTextViewHolder) {

                int currentPlace = textView.getCurrentPlace();
                currentPlace = currentPlace - 1;
                if (currentPlace < 1) {
                    currentPlace = 3;
                }
                textView.updateCurrentPlace(currentPlace);

                if (currentPlace == 3) {
                    final int initPlace = textView.getPlace();
                    RectF initRectF = mViewReferenceRectList.get(initPlace);
                    RectF destRectF = mViewReferenceRectList.get(currentPlace);

                    float verticalDiff = (destRectF.top + destRectF.bottom -
                            initRectF.top - initRectF.bottom) / 2f;
                    LogWrapper.showLog(Log.INFO, getLogTag(), "AnimationCallback - initPlace: " + initPlace +
                            ", ccurrentPlace: " + currentPlace + ", verticalDiff: " + verticalDiff);

                    ObjectAnimator translationYani = ObjectAnimator.ofFloat(textView, "translationY", verticalDiff);
                    translationYani.setDuration(0);
                    translationYani.setInterpolator(mInterpolator);
                    mAnimatorSet = new AnimatorSet();
                    mAnimatorSet.addListener(new DefaultAnimationCallback());
                    mAnimatorSet.play(translationYani);
                }
            }

            if (null != mAnimatorSet) {
                mAnimatorSet.start();
            }
        }

    }

}
