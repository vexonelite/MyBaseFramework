package tw.realtime.project.rtbaseframework.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

    private OnDecisionMadeListener mCallback = null;


    public void setDecisionMadeListener (OnDecisionMadeListener listener) {
        mCallback = listener;
    }


    public interface OnDecisionMadeListener {
        void onNotification (DialogFragment dialogFrag, int year, int month, int day);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
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

