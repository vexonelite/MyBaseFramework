package tw.realtime.project.rtbaseframework.widgets.recyclerviews.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.adapters.recyclerview.BaseRecyclerViewAdapter;

/**
 * {@link RecyclerView} relevant
 */
public abstract class BaseLoopingRecyclerLayout<V, K extends RecyclerView.ViewHolder> extends FrameLayout {

    private static final long DEFAULT_INTERVAL_IN_MILLIS = 3000L;

    private final byte[] lock = new byte[0];

    private Disposable dispose;

    private final List<V> dataSetHolder = new ArrayList<>();

    private RecyclerView recyclerView;

    private BaseRecyclerViewAdapter<V, K> recyclerAdapter;

    private final LinearLayoutManager layoutManager;

    private final RecyclerView.OnScrollListener onScrollCallback = new LoopingRecyclerOnScrollCallback();

    private int currentPosition;

    private MuzaffarPageIndicator pageIndicator;

    private boolean doesScrollAutomatically = false;


    public BaseLoopingRecyclerLayout(@NonNull Context context) {
        super(context);
        layoutManager = new SmoothLinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        //layoutManager.setInitialPrefetchItemCount(3);
    }

    public BaseLoopingRecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        layoutManager = new SmoothLinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        //layoutManager.setInitialPrefetchItemCount(3);
    }

    public BaseLoopingRecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutManager = new SmoothLinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        //layoutManager.setInitialPrefetchItemCount(3);
    }

    @TargetApi(21)
    public BaseLoopingRecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        layoutManager = new SmoothLinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        //layoutManager.setInitialPrefetchItemCount(3);
    }


    private void setupRecyclerView (@NonNull RecyclerView givenRecyclerView) {
        recyclerView = givenRecyclerView;
        recyclerAdapter = getRecyclerAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public void initialization(@NonNull RecyclerView givenRecyclerView,
                               LinearLayout givenContainer,
                               @DrawableRes int indicatorDrawable) {
        setupRecyclerView(givenRecyclerView);
        if (null != givenContainer) {
            pageIndicator = new MuzaffarPageIndicator(givenContainer);
            pageIndicator.setIndicatorDrawable(indicatorDrawable);
        }
    }

    public void initialization(@NonNull RecyclerView givenRecyclerView,
                               @NonNull MuzaffarPageIndicator givenPageIndicator) {
        setupRecyclerView(givenRecyclerView);
        pageIndicator = givenPageIndicator;
    }

    private String getLogTag(){
        return this.getClass().getSimpleName();
    }

    @NonNull
    protected abstract BaseRecyclerViewAdapter<V, K> getRecyclerAdapter();

    private class LoopingRecyclerOnScrollCallback extends RecyclerView.OnScrollListener {

        private int cachePosition = -1;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //super.onScrolled(recyclerView, dx, dy)

            final int firstItemVisible = layoutManager.findFirstVisibleItemPosition();
            if (cachePosition != firstItemVisible) {
                cachePosition = firstItemVisible;

                final int pageNumber = mappingIntoPageNumber(firstItemVisible);
                if (null != pageIndicator) {
                    pageIndicator.setIndicatorAsSelected(pageNumber);
                }

                final int tailIndex = recyclerAdapter.getItemCount() - 1;
                final int modResult = firstItemVisible % tailIndex;
                //LogWrapper.showLog(Log.WARN, getLogTag(), "onScrolled - firstItemVisible: " + firstItemVisible + ", value: " + value");

                if (firstItemVisible > 0 && modResult == 0) {
                    // When position reaches end of the list, it should go back to the beginning
                    //LogWrapper.showLog(Log.WARN, getLogTag(), "onScrolled - recyclerView?.scrollToPosition to head!");
                    if (null != recyclerView) {
                        recyclerView.scrollToPosition(1);
                    }
                } else if (firstItemVisible == 0) {
                    // When position reaches beginning of the list, it should go back to the end
                    //LogWrapper.showLog(Log.WARN, getLogTag(), "onScrolled - recyclerView?.scrollToPosition to tail!");
                    if (null != recyclerView) {
                        recyclerView.scrollToPosition(tailIndex);
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //super.onScrollStateChanged(recyclerView, newState)

            switch (newState) {
                // When dragging, we assume user swiped. So we will stop auto rotation
                case RecyclerView.SCROLL_STATE_DRAGGING: {
                    stopAutoScrollIfNeeded();
                    break;
                }
                case RecyclerView.SCROLL_STATE_IDLE: {
                    final int firstItemVisible = layoutManager.findFirstVisibleItemPosition();
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onScrollStateChanged: SCROLL_STATE_IDLE and firstItemVisible: " + firstItemVisible);
                    final int pageNumber = mappingIntoPageNumber(firstItemVisible);
                    if (null != pageIndicator) {
                        pageIndicator.setIndicatorAsSelected(pageNumber);
                    }
                    if (doesScrollAutomatically) {
                        startAutoScrollIfNeededInternally(dataSetHolder.size(), DEFAULT_INTERVAL_IN_MILLIS);
                    }
                }
            }
        }
    }

    private int mappingIntoPageNumber (int firstItemVisible) {
        final int tailIndex = recyclerAdapter.getItemCount() - 1;
        if (firstItemVisible == 0) {
            return dataSetHolder.size() - 1;
        }
        else if (firstItemVisible == tailIndex) {
            return 0;
        }
        else {
            return firstItemVisible - 1;
        }
    }

    public final int dataSetCapacity() {
        return dataSetHolder.size();
    }

    @NonNull
    private List<V> getDataSetForAdapter (@NonNull List<V> dataSet) {
        final List<V> adapterDataSet = new ArrayList<>();
        if (dataSet.size() > 1) {
            final int tailIndex = dataSet.size() - 1;
            adapterDataSet.add(dataSet.get(tailIndex));
            adapterDataSet.addAll(dataSetHolder);
            adapterDataSet.add(dataSet.get(0));
        }
        else {
            adapterDataSet.addAll(dataSet);
        }
        return adapterDataSet;
    }

    public final void setDataSet (@NonNull List<V> dataSet) {
        setDataSet(dataSet, false);
    }

    public final void setDataSet (@NonNull List<V> dataSet, boolean scrollAutomatically) {
        if (dataSet.isEmpty()) {
            return;
        }

        synchronized (lock) {
            dataSetHolder.clear();
            dataSetHolder.addAll(dataSet);

            final List<V> adapterDataSet = getDataSetForAdapter(dataSetHolder);
            recyclerAdapter.appendNewDataSet(adapterDataSet, true);
            LogWrapper.showLog(Log.WARN, getLogTag(), "setDataSet - recyclerAdapter.getItemCount: " + recyclerAdapter.getItemCount());

            if (null != pageIndicator) {
                pageIndicator.pageCount = dataSetHolder.size();
                pageIndicator.showIndicators();
            }

            if (dataSet.size() > 1) {
                recyclerView.addOnScrollListener(onScrollCallback);
                recyclerView.scrollToPosition(1);
                if (scrollAutomatically) {
                    startAutoScrollIfNeeded();
                    //startAutoScrollIfNeededInternally(dataSetHolder.size(), DEFAULT_INTERVAL_IN_MILLIS);
                }
                LogWrapper.showLog(Log.INFO, getLogTag(), "setDataSet: enable Looping Mode!");
            }
        }
    }

    public final void resetDataSet () {
        synchronized (lock) {
            dataSetHolder.clear();
            recyclerAdapter.removeAllExistingData(true);
            recyclerView.removeOnScrollListener(onScrollCallback);
        }
    }


//    public void setDoesScrollAutomaticallyFlag (boolean flag) {
//        doesScrollAutomatically = flag;
//    }

    public final void startAutoScrollIfNeeded() {
        doesScrollAutomatically = true;
        startAutoScrollIfNeededInternally(dataSetHolder.size(), DEFAULT_INTERVAL_IN_MILLIS);
    }

    /**
     *
     * @param holderSize    this would be the dataSetHolder.size()
     * @param intervalInMillis  default value is DEFAULT_INTERVAL_IN_MILLIS
     */
    private void startAutoScrollIfNeededInternally(final int holderSize, final long intervalInMillis) {
        if ( (null != dispose) && (!dispose.isDisposed()) ) {
//            LogWrapper.showLog(Log.WARN, getLogTag(), "startAutoScrollIfNeededInternally - has not Disposed!");
            return;
        }
        if (holderSize <= 1) {
//            LogWrapper.showLog(Log.WARN, getLogTag(), "startAutoScrollIfNeededInternally - holderSize <= 1 !!");
            return;
        }

        dispose = Flowable.interval(intervalInMillis, TimeUnit.MILLISECONDS)
//                .map(timeStamp -> {
//                    return (int)(timeStamp % listSize + 1);
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::nextPositionConsumer1);
//        LogWrapper.showLog(Log.WARN, getLogTag(), "startAutoScrollIfNeededInternally - setup new dispose!");
    }

    private void nextPositionConsumer1 (@NonNull Long timeStamp) throws Exception {
        if (null == recyclerView) {
            return;
        }
        final int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //LogWrapper.showLog(Log.INFO, getLogTag(), "subscribe - findFirstVisibleItemPosition: " + firstPosition + ", next position: " + (firstPosition - 1));
        //recyclerView.smoothScrollToPosition(it.toInt() + 1);
        recyclerView.smoothScrollToPosition(firstPosition + 1);
    }

    private void autoScroll(final int listSize, final long intervalInMillis) {
        if ( (null != dispose) && (!dispose.isDisposed()) ) {
            return;
        }
        dispose = Flowable.interval(intervalInMillis, TimeUnit.MILLISECONDS)
                .map(timeStamp -> {
                    return (int)(timeStamp % listSize + 1);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::nextPositionConsumer2);
    }

    // implements Consumer<Integer>
    private void nextPositionConsumer2 (@NonNull Integer position) throws Exception {
        if (null != recyclerView) {
            recyclerView.smoothScrollToPosition(position + 1);
        }
    }

    public final void stopAutoScrollIfNeeded() {
        if (null != dispose) {
            dispose.dispose();
        }
//        LogWrapper.showLog(Log.WARN, getLogTag(), "stopAutoScrollIfNeeded!");
    }

}
