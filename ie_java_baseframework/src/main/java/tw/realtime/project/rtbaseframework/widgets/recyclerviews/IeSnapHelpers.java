package tw.realtime.project.rtbaseframework.widgets.recyclerviews;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class IeSnapHelpers {

    public interface OnSnapPositionChangeDelegate {
        void onSnapPositionChange(final int position);
    }

    public static final class SnapPositionScrollListener extends RecyclerView.OnScrollListener {

        private int currentScrollState = RecyclerView.SCROLL_STATE_IDLE;
        private int snapPosition = RecyclerView.NO_POSITION;
        private final SnapHelper snapHelper;
        private final OnSnapPositionChangeDelegate callback;

        public SnapPositionScrollListener(
                @NonNull final SnapHelper snapHelper,
                @Nullable final OnSnapPositionChangeDelegate callback) {
            this.snapHelper = snapHelper;
            this.callback = callback;
        }

        @Override
        public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int newState) {
            currentScrollState = newState;
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                maybeNotifySnapPositionChange(recyclerView);
            }
        }

        @Override
        public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
            if (currentScrollState != RecyclerView.SCROLL_STATE_IDLE) {
                maybeNotifySnapPositionChange(recyclerView);
            }
        }

        private void maybeNotifySnapPositionChange(@NonNull final RecyclerView recyclerView) {
            final int snapPosition = getSnapPosition(snapHelper, recyclerView);
            final boolean snapPositionChanged = this.snapPosition != snapPosition;
            if (snapPositionChanged) {
                this.snapPosition = snapPosition;
                if (null != callback) { callback.onSnapPositionChange(snapPosition); }
            }
        }

        public int getSnapPosition(@NonNull final SnapHelper snapHelper, @NonNull final RecyclerView recyclerView) {
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) { return RecyclerView.NO_POSITION; }
            final View snapView = snapHelper.findSnapView(layoutManager);
            if (null == snapView) { return RecyclerView.NO_POSITION; }
            return layoutManager.getPosition(snapView);
        }
    }


    /**
     * [How to programmatically snap to position on Recycler view with LinearSnapHelper](https://stackoverflow.com/questions/42988016/how-to-programmatically-snap-to-position-on-recycler-view-with-linearsnaphelper)
     */
    public static final class ScrollToSnapPositionTask implements Runnable {

        private final RecyclerView recyclerView;
        //private final RecyclerView.LayoutManager layoutManager;
        private final SnapHelper snapHelper;
        private final int snapPosition;

        public ScrollToSnapPositionTask(
                @NonNull final RecyclerView recyclerView,
                @NonNull final SnapHelper snapHelper,
                final int snapPosition) {
            this.recyclerView = recyclerView;
            this.snapHelper = snapHelper;
            this.snapPosition = snapPosition;
        }

        @Override public void run() {
            recyclerView.scrollToPosition(snapPosition);
            recyclerView.post(new ScrollToSnapPositionRunnable(recyclerView, snapHelper, snapPosition));
        }
    }

    /**
     * [How to programmatically snap to position on Recycler view with LinearSnapHelper](https://stackoverflow.com/questions/42988016/how-to-programmatically-snap-to-position-on-recycler-view-with-linearsnaphelper)
     */
    public static final class ScrollToSnapPositionRunnable implements Runnable {

        private final RecyclerView recyclerView;
        private final SnapHelper snapHelper;
        private final int snapPosition;

        public ScrollToSnapPositionRunnable(
                @NonNull final RecyclerView recyclerView,
                @NonNull final SnapHelper snapHelper,
                final int snapPosition) {
            this.recyclerView = recyclerView;
            this.snapHelper = snapHelper;
            this.snapPosition = snapPosition;
        }

        @Override public void run() {
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (null == layoutManager) {
                LogWrapper.showLog(Log.ERROR, "ScrollToSnapPositionTask_MyRunnable", "recyclerView.getLayoutManager() is null!!");
                return;
            }

            final View view = layoutManager.findViewByPosition(snapPosition);
            if (null == view) {
                LogWrapper.showLog(Log.ERROR, "ScrollToSnapPositionTask_MyRunnable", "Cant find target View for initial Snap");
                return;
            }

            final int[] snapDistance = snapHelper.calculateDistanceToFinalSnap(layoutManager, view);
            if ( (null != snapDistance) && ((snapDistance[0] != 0) || (snapDistance[1] != 0)) ) {
                //recyclerView.scrollBy(snapDistance[0], snapDistance[1]);
                recyclerView.smoothScrollBy(snapDistance[0], snapDistance[1]);
            }
        }
    }

}
