package tw.realtime.project.rtbaseframework.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vexonelite on 2017/06/15.
 */

public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int verticalMargin;
    private int horizontalMargin;
    private int verticalOffset;

    public static class Builder {

        private Context bContext;
        private float bHorizontalMargin = 8f;
        private float bVerticalMargin = 8f;
        private float bVerticalOffset = 0f;

        public Builder (Context context) {
            bContext = context;
        }

        /**
         *
         * @param horizontalMargin Unit DP
         * @return
         */
        public Builder setHorizontalMargin (float horizontalMargin) {
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
        public Builder setVerticalMargin (float verticalMargin) {
            if (verticalMargin > 0f) {
                bVerticalMargin = verticalMargin;
            }
            return this;
        }

        public Builder setVerticalOffset (float verticalOffset) {
            if (verticalOffset > 0f) {
                bVerticalOffset = verticalOffset;
            }
            return this;
        }

        public MarginDecoration build () {
            return new MarginDecoration(this);
        }
    }

    private MarginDecoration(Builder builder) {
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
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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
