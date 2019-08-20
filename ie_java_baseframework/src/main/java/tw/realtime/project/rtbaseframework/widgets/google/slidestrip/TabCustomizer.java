package tw.realtime.project.rtbaseframework.widgets.google.slidestrip;

import androidx.annotation.ColorInt;

/**
 * Allows complete control over the colors drawn in the tab layout. Set with
 * {@link SlidingTabStrip#setCustomTabColorizer(TabCustomizer)}.
 */
public interface TabCustomizer {
    /**
     * @return return the color of the indicator used when {@code position} is selected.
     */
    @ColorInt
    int getIndicatorColor(int position);

    /**
     * Added by vexonelite
     * @return
     */
    int getIndicatorThickness();

    /**
     * @return return the color of the divider drawn to the right of {@code position}.
     */
    @ColorInt int getDividerColor(int position);

    /**
     * Added by vexonelite
     * @return
     */
    int getDividerThickness();

    /** 0.0f to 1.0f */
    float getDividerHeight();

    /**
     *
     * @return
     */
    int getBottomBorderThickness();
}

