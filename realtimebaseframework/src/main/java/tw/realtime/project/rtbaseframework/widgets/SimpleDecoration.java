package tw.realtime.project.rtbaseframework.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vexonelite on 2017/06/15.
 */

public class SimpleDecoration extends RecyclerView.ItemDecoration {

    private int verticalMargin;

    public static class Builder {

        private Context bContext;
        private float bVerticalMargin = 8f;

        public Builder (Context context) {
            bContext = context;
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

        public SimpleDecoration build () {
            return new SimpleDecoration(this);
        }
    }

    private SimpleDecoration(Builder builder) {
        float density = builder.bContext.getResources().getDisplayMetrics().density;
        verticalMargin = (int)(density * builder.bVerticalMargin);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.set(0, 0, 0, 0);
        }
        else {
            outRect.set(0, verticalMargin, 0, 0);
        }
    }
}
