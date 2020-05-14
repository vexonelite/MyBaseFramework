package tw.realtime.project.rtbaseframework.widgets.recyclerviews.banner;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView} relevant
 */
public final class MuzaffarPageIndicator {

    private int indicatorDrawable = 0;
    private int indicatorSpacing = 0;
    private int indicatorSize = 0;
    private int initialPage = 0;
    public int pageCount = 0;

    private final int defaultSizeInDp = 12;
    private final int defaultSpacingInDp = 12;

    private final LinearLayout container;

    public MuzaffarPageIndicator (@NonNull LinearLayout linearLayout) {
        this.container = linearLayout;
    }


    public void setInitialPage(int page) {
        initialPage = page;
    }

    public void setIndicatorDrawable(@DrawableRes int drawable) {
        indicatorDrawable = drawable;
    }

    public void setIndicatorSpacing(@DimenRes int spacingRes) {
        indicatorSpacing = spacingRes;
    }

    public void setIndicatorSize(@DimenRes int dimenRes) {
        indicatorSize = dimenRes;
    }

    public void showIndicators() {
        initIndicators();
        setIndicatorAsSelected(initialPage);
    }

    private void initIndicators() {
        final Resources resources = container.getContext().getResources();
        container.removeAllViews();

        if (pageCount <= 0) { return; }

        //mViewPager.addOnPageChangeListener(this)
        for (int i = 0; i< pageCount; i++) {
            final View view = new View(container.getContext());
            final int dimen = (indicatorSize != 0)
                    ? resources.getDimensionPixelSize(indicatorSize)
                    : (int)(resources.getDisplayMetrics().density * defaultSizeInDp);
            final int margin = (indicatorSpacing != 0)
                    ? resources.getDimensionPixelSize(indicatorSpacing)
                    : (int)(resources.getDisplayMetrics().density * defaultSpacingInDp);
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dimen, dimen);
            layoutParams.setMargins(((i == 0) ? 0 : margin), 0, 0, 0);
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(indicatorDrawable);
            view.setSelected(i == 0);
            container.addView(view);
        }
    }

    public void setIndicatorAsSelected(int index) {
        for (int i = 0; i < container.getChildCount(); i++) {
            final View view = container.getChildAt(i);
            view.setSelected(i == index);
        }
    }
}
