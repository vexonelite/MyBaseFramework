package tw.realtime.project.rtbaseframework.app;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tw.realtime.project.rtbaseframework.LogWrapper;

/**
 * Created by vexonelite on 2020/2/7.
 */

public final class FragmentUtils {

    public static void dismissDialogFragment(@Nullable DialogFragment dialog) {
        if (null == dialog) { return; }
        try { dialog.dismissAllowingStateLoss(); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, dialog.getClass().getSimpleName(), "Error on DialogFragment#dismissAllowingStateLoss"); }
    }

    public static boolean isDialogShowing(@NonNull DialogFragment dialog) {
        return (null != dialog.getDialog())
                && (dialog.getDialog().isShowing())
                && (!dialog.isRemoving());
    }
}
