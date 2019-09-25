package tw.realtime.project.rtbaseframework.widgets.viewholders;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public final class Decorations {

    public static final class Simple extends RecyclerView.ItemDecoration {

        private int verticalMargin;
        private int horizontalMargin;

        public static final class Builder {

            private final Context bContext;
            private float bVerticalMargin = 8f;
            private float bHorizontalMargin = 8f;

            public Builder(@NonNull Context context) {
                bContext = context;
            }

            /**
             *
             * @param verticalMargin Unit DP
             * @return
             */
            public Builder setVerticalMargin(float verticalMargin) {
                if (verticalMargin >= 0f) {
                    bVerticalMargin = verticalMargin;
                }
                return this;
            }

            /**
             *
             * @param horizontalMargin Unit DP
             * @return
             */
            public Builder setHorizontalMargin(float horizontalMargin) {
                if (horizontalMargin >= 0f) {
                    bHorizontalMargin = horizontalMargin;
                }
                return this;
            }

            public Simple build() {
                return new Simple(this);
            }
        }

        private Simple(@NonNull Builder builder) {
            float density = builder.bContext.getResources().getDisplayMetrics().density;
            verticalMargin = (int)(density * builder.bVerticalMargin);
            horizontalMargin = (int)(density * builder.bHorizontalMargin);
        }


        @Override
        public void getItemOffsets(
                @NonNull Rect outRect,
                @NonNull View view,
                @NonNull RecyclerView parent,
                @NonNull RecyclerView.State state) {

            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.set(horizontalMargin, 0, horizontalMargin, 0);
            }
            else {
                outRect.set(horizontalMargin, verticalMargin, horizontalMargin, 0);
            }
        }
    }

    public static final class Margin extends RecyclerView.ItemDecoration {

        private int verticalMargin;
        private int horizontalMargin;
        private int verticalOffset;

        public static final class Builder {

            private final Context bContext;
            private float bHorizontalMargin = 8f;
            private float bVerticalMargin = 8f;
            private float bVerticalOffset = 0f;

            public Builder(@NonNull Context context) {
                bContext = context;
            }

            /**
             *
             * @param horizontalMargin Unit DP
             * @return
             */
            public Builder setHorizontalMargin(float horizontalMargin) {
                if (horizontalMargin > 0f) {
                    bHorizontalMargin = horizontalMargin;
                }
                return this;
            }

            /**
             *
             * @param verticalMargin Unit DP
             * @return
             */
            public Builder setVerticalMargin(float verticalMargin) {
                if (verticalMargin > 0f) {
                    bVerticalMargin = verticalMargin;
                }
                return this;
            }

            public Builder setVerticalOffset(float verticalOffset) {
                if (verticalOffset > 0f) {
                    bVerticalOffset = verticalOffset;
                }
                return this;
            }

            public Margin build() {
                return new Margin(this);
            }
        }

        private Margin(@NonNull Builder builder) {
            float density = builder.bContext.getResources().getDisplayMetrics().density;
            horizontalMargin = (int)(density * builder.bHorizontalMargin);
            verticalMargin = (int)(density * builder.bVerticalMargin);
            verticalOffset = (builder.bVerticalOffset > 0f)
                    ? (int)(density * builder.bVerticalOffset)
                    : 0;
        }

        private String getLogTag () {
            return this.getClass().getSimpleName();
        }

        @Override
        public void getItemOffsets(
                @NonNull Rect outRect,
                @NonNull View view,
                @NonNull RecyclerView parent,
                @NonNull RecyclerView.State state) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "getItemOffsets: " + outRect);
            RecyclerView.Adapter adapter = parent.getAdapter();

            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.set(horizontalMargin, verticalMargin + verticalOffset, horizontalMargin, verticalMargin);
            }
            else if (parent.getChildLayoutPosition(view) == (adapter.getItemCount() - 1) ) {
                outRect.set(horizontalMargin, 0, horizontalMargin, verticalMargin  + verticalOffset);
            }
            else {
                outRect.set(horizontalMargin, 0, horizontalMargin, verticalMargin);
            }
        }
    }

    public static final class BuiltIn2 extends RecyclerView.ItemDecoration {

        private final int horizontalMargin;
        private final int verticalMargin;
        private final boolean ignoreTop;

        public BuiltIn2(@NonNull Context context, int verticalUnitInDp, int horizontalUnitInDp, boolean ignoreTop) {
            final float density = context.getResources().getDisplayMetrics().density;
            verticalMargin = (int)(density * ((float)verticalUnitInDp));
            horizontalMargin = (int)(density * ((float)horizontalUnitInDp));
            this.ignoreTop = ignoreTop;
        }

        @Override
        public void getItemOffsets(
                @NonNull Rect outRect,
                @NonNull View view,
                @NonNull RecyclerView parent,
                @NonNull RecyclerView.State state) {
            final int position = parent.getChildLayoutPosition(view);
            outRect.left = horizontalMargin;
            outRect.right = horizontalMargin;
            outRect.bottom = verticalMargin;
            if (ignoreTop) {
                outRect.top = verticalMargin;
            }
            else {
                outRect.top = (position == 0) ? verticalMargin : 0;
            }
        }
    }
}
