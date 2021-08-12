package tw.com.goglobal.project.rxjava2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.dialogs.PercentageProgressDialog;


public final class RxPercentageProgressDialog extends PercentageProgressDialog implements RxDisposeDelegate {

    public static final String COUNTER = "_counter";
    public static final String PERIOD = "_period";

    private Disposable timerDisposable;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle args = getArguments();
        final long timeUpValue = (null != args) ? args.getLong(COUNTER, 0L) : 0L;
        final long periodValue = (null != args) ? args.getLong(PERIOD, 0L) : 0L;
        LogWrapper.showLog(Log.INFO, getLogTag(), "onActivityCreated - timeUpValue: " + timeUpValue + ", periodValue: " + periodValue);
        if ((timeUpValue > 0) && (periodValue > 0)) {
            countTo100TimerTask(timeUpValue, periodValue);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        rxDisposeIfPossible();
    }

    // Implementation of RxDisposeDelegate
    @Override
    public void rxDisposeIfPossible() {
        if (null != timerDisposable) {
            if (!timerDisposable.isDisposed()) {
                timerDisposable.dispose();
                LogWrapper.showLog(Log.WARN, getLogTag(), "rxDisposableIfNeeded - dispose");
            }
            timerDisposable = null;
            LogWrapper.showLog(Log.WARN, getLogTag(), "rxDisposableIfNeeded - reset");
        }
    }

    private void countTo100TimerTask(final long timeUpValue, final long periodValue) {
        rxDisposeIfPossible();
        LogWrapper.showLog(Log.INFO, getLogTag(), "countTo100TimerTask - timeUpValue: " + timeUpValue + ", periodValue: " + periodValue);
        timerDisposable = Flowable.interval(0L, periodValue, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TimeValueConsumer(timeUpValue));
    }

    private class TimeValueConsumer implements Consumer<Long> {

        final long timeUpValue;

        TimeValueConsumer(final long timeUpValue) { this.timeUpValue = timeUpValue; }

        @Override
        public void accept(@NonNull final Long value) throws Exception {
            final boolean isTimeUp = (value > timeUpValue);
            LogWrapper.showLog(Log.INFO, getLogTag(), "TimeValueConsumer - value: " + value + ", isTimeUp: " + isTimeUp);
            if (isTimeUp) { rxDisposeIfPossible(); }
            else { setProgress((int)value.longValue()); }
        }
    }
}
