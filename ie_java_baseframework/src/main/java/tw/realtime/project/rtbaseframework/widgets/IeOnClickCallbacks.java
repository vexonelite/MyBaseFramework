package tw.realtime.project.rtbaseframework.widgets;

import android.view.View;

public final class IeOnClickCallbacks {

    public static final class DoNothing implements View.OnClickListener {
        @Override
        public void onClick(View view) { }
    }


    public static final class ViewVisibility implements View.OnClickListener {

        private final int visibility;

        public ViewVisibility(final int visibility) { this.visibility = visibility; }

        @Override
        public void onClick(View view) {
            if ((visibility == View.VISIBLE) || (visibility == View.INVISIBLE) || (visibility == View.GONE) ) {
                view.setVisibility(visibility);
            }
        }
    }


}
