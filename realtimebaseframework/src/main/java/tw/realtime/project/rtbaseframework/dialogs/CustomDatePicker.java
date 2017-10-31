package tw.realtime.project.rtbaseframework.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.app.BaseDialogFragment;


/**
 * 日期挑選跳窗。
 * <p>
 * Created by vexonelite on 2017/7/10.
 */
public class CustomDatePicker extends BaseDialogFragment {

    private Listener mCallback = null;

    private String mDialogTitle;
    private String mDoneButtonTitle;

    private Calendar mGivenCalendar;

    private DatePicker mDatePicker;

    private final int mDefaultLayoutResId = tw.realtime.project.rtbaseframework.R.layout.base_dialog_custom_date_picker_fixed_height;

    public CustomDatePicker setDialogTitle (String title) {
        if ( (null != title) && (!title.isEmpty()) ) {
            mDialogTitle = title;
        }
        return this;
    }

    public CustomDatePicker setDoneTitle (String title) {
        if ( (null != title) && (!title.isEmpty()) ) {
            mDoneButtonTitle = title;
        }
        return this;
    }

    public CustomDatePicker setListener (Listener listener) {
        mCallback = listener;
        return this;
    }

    public CustomDatePicker setCalendar (Calendar given) {
        mGivenCalendar = given;
        return this;
    }


    public interface Listener extends BaseDialogFragment.OnDecisionMadeListener {
        void onSubmitButtonPressed(int year, int month, int day);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mDefaultLayoutResId, container);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        TextView titleView = rootView.findViewById(tw.realtime.project.rtbaseframework.R.id.dialogTitle);
        if ( (null != titleView) && (null != mDialogTitle) ) {
            titleView.setText(mDialogTitle);
        }

        View doneButton = rootView.findViewById(tw.realtime.project.rtbaseframework.R.id.doneButton);
        if (null != doneButton) {
            if ( (doneButton instanceof TextView) && (null != mDoneButtonTitle) ) {
                ((TextView) doneButton).setText(mDoneButtonTitle);
            }
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    notifyCallbackIfNeeded();
                    view.setEnabled(true);
                }
            });
        }

        DatePicker datePicker = rootView.findViewById(tw.realtime.project.rtbaseframework.R.id.datePicker);
        if (null != datePicker) {
            mDatePicker = datePicker;
            final Calendar calendar = (null != mGivenCalendar) ? mGivenCalendar : Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePicker.init(year, month, day, new DateChangedCallback());
        }
    }

    private class DateChangedCallback implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onDateChanged - year: " + year
                    + ", month: " + month + ", day: " + day);
        }
    }

    private void notifyCallbackIfNeeded () {
        if ( (null != mCallback) && (null != mDatePicker) ) {
            mCallback.onSubmitButtonPressed(
                    mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
            mCallback.onNotification(this, DialogAction.CONFIRM);
        }
    }
}

