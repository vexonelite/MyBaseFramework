package tw.realtime.project.rtbaseframework.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * 日期挑選跳窗。
 * <p>
 * Created by vexonelite on 2017/7/10.
 */
public class DefaultDatePicker extends DialogFragment
                        implements DatePickerDialog.OnDateSetListener {

    private Listener mCallback = null;
    private Calendar mGivenCalendar;


    public DefaultDatePicker setListener (Listener listener) {
        mCallback = listener;
        return this;
    }

    public DefaultDatePicker setCalendar (Calendar given) {
        mGivenCalendar = given;
        return this;
    }


    public interface Listener {
        void onNotification (DialogFragment dialogFrag, int year, int month, int day);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current date as the default date in the picker
        final Calendar calendar = (null != mGivenCalendar) ? mGivenCalendar : Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog,
                this,
                year,
                month,
                day);
        if (null != datePickerDialog.getWindow()) {
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return datePickerDialog;
    }

    // DatePickerDialog.OnDateSetListener
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (null != mCallback) {
            mCallback.onNotification(this, year, month, day);
        }
    }

}

