package tw.realtime.project.rtbaseframework.dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import tw.realtime.project.rtbaseframework.R;


public class PercentageProgressDialog extends BaseDialogFragment {

    private ProgressBar progressBar;
    private TextView progressValueView;
    private TextView captionView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_dialog_percentage_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        progressBar = rootView.findViewById(R.id.progressBar);
        progressValueView = rootView.findViewById(R.id.progressValue);
        captionView = rootView.findViewById(R.id.caption);
        setProgress(0);
    }

    public final void setProgress(final int progress) {
        if (null != progressBar) { progressBar.setProgress(progress); }
        if (null != progressValueView) { progressValueView.setText(String.valueOf(progress)); }
    }

    public final void setCaptionResId(@StringRes final int textResId) {
        if (null != captionView) { captionView.setText(textResId); }
    }

    public final void setCaptionText(@NonNull final String text) {
        if (null != captionView) { captionView.setText(text); }
    }

    public final void setCaptionVisibility(final int visibility) {
        if (null != captionView) { captionView.setVisibility(visibility); }
    }

//    private Disposable timerDisposable;
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        final Bundle args = getArguments();
//        final long timeUpValue = (null != args) ? args.getLong(AppConstants.AppKeys.COUNTER, 0L) : 0L;
//        final long periodValue = (null != args) ? args.getLong(AppConstants.AppKeys.PERIOD, 0L) : 0L;
//        LogWrapper.showLog(Log.INFO, "MyProgressbarDialogPercentFragment", "onActivityCreated - timeUpValue: " + timeUpValue + ", periodValue: " + periodValue);
//        if ((timeUpValue > 0) && (periodValue > 0)) {
//            countTo100TimerTask(timeUpValue, periodValue);
//        }
//    }
//
//    public void onDestroyView() {
//        super.onDestroyView();
//        rxDisposeIfPossible();
//    }
//
//    // Implementation of RxDisposeDelegate
//    @Override
//    public void rxDisposeIfPossible() {
//        if (null != timerDisposable) {
//            if (!timerDisposable.isDisposed()) {
//                timerDisposable.dispose();
//                LogWrapper.showLog(Log.WARN, "MyProgressbarDialogPercentFragment", "rxDisposableIfNeeded - dispose");
//                //LogWrapper.showLog(Log.WARN, "DEBUG_Kessil_WiFi", "rxDisposableIfNeeded - dispose");
//            }
//            timerDisposable = null;
//            LogWrapper.showLog(Log.WARN, "MyProgressbarDialogPercentFragment", "rxDisposableIfNeeded - reset");
//            //LogWrapper.showLog(Log.WARN, "DEBUG_Kessil_WiFi", "rxDisposableIfNeeded - reset");
//        }
//    }
//
//    /** {@link LampAdapterV2} has similar code snippet */
//    private void countTo100TimerTask(final long timeUpValue, final long periodValue) {
//        rxDisposeIfPossible();
//        LogWrapper.showLog(Log.INFO, "MyProgressbarDialogPercentFragment", "countTo100TimerTask - timeUpValue: " + timeUpValue + ", periodValue: " + periodValue);
//        //LogWrapper.showLog(Log.INFO, "DEBUG_Kessil_WiFi", "countTo100TimerTask");
//        timerDisposable = Flowable.interval(0L, periodValue, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new TimeValueConsumer(timeUpValue));
//    }
//
//    private class TimeValueConsumer implements Consumer<Long> {
//
//        final long timeUpValue;
//
//        TimeValueConsumer(final long timeUpValue) { this.timeUpValue = timeUpValue; }
//
//        @Override
//        public void accept(@NonNull final Long value) throws Exception {
//            final boolean isTimeUp = (value > timeUpValue);
//            LogWrapper.showLog(Log.INFO, "MyProgressbarDialogPercentFragment", "TimeValueConsumer - value: " + value + ", isTimeUp: " + isTimeUp);
//            if (isTimeUp) { rxDisposeIfPossible(); }
//            else { setProgress((int)value.longValue()); }
//        }
//    }
}