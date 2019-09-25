package tw.realtime.project.rtbaseframework.app;

import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.transition.SidePropagation;
import androidx.transition.Slide;


public final class FragmentTransitions {

    @NonNull
    public static SidePropagation getSidePropagation(int gravity, float propagationSpeed) {
        final SidePropagation sidePropagation = new SidePropagation();
        sidePropagation.setSide(gravity);
        sidePropagation.setPropagationSpeed(propagationSpeed);
        return sidePropagation;
    }

    @NonNull
    public static Slide getSlide(@NonNull SidePropagation sidePropagation, int gravity, long startDelay) {
        final Slide slide = new Slide(gravity);
        slide.setPropagation(sidePropagation);
        slide.setStartDelay(startDelay);
        return slide;
    }

    @NonNull
    public static Slide defaultEnter1() {
        final SidePropagation propagateBottom = getSidePropagation(Gravity.BOTTOM, 2f);
        return getSlide(propagateBottom, Gravity.END, 100);
    }

    @NonNull
    public static Slide defaultExit1() {
        final SidePropagation propagateBottom = getSidePropagation(Gravity.BOTTOM, 2f);
        return getSlide(propagateBottom, Gravity.END, 100);
    }

    @NonNull
    public static Slide defaultEnter2() {
        final SidePropagation propagateTop = getSidePropagation(Gravity.TOP, 2f);
        return getSlide(propagateTop, Gravity.START, 200);
    }

    @NonNull
    public static Slide defaultExit2() {
        final SidePropagation propagateTop = getSidePropagation(Gravity.TOP, 2f);
        return getSlide(propagateTop, Gravity.START, 0);
    }
}
