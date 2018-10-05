package tw.realtime.project.rtbaseframework.widgets.animator;

import android.animation.Animator;

/**
 * App Default Animation Callback that implements {@link Animator.AnimatorListener}.
 * Subclass can  implement parts of method depending upon the requirement.
 * <p>
 * Created by vexonelite on 2016/12/5.
 */

public class DefaultAnimationCallback implements Animator.AnimatorListener {

    protected final String getLogTag () {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
