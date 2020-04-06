package tw.realtime.project.rtbaseframework.app;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;

/**
 * Created by vexonelite on 2020/2/7.
 */

public final class FragmentUtils {

    public static void showDialogFragment(
            @NonNull FragmentManager fragmentManager, @NonNull DialogFragment dialog, @NonNull String tag) {
        try { dialog.show(fragmentManager, tag); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "FragmentUtils", "error on showDialogFragment"); }
    }

    public static void dismissDialogFragment(@NonNull DialogFragment dialog) {
        try { dialog.dismissAllowingStateLoss(); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "FragmentUtils", "Error on DialogFragment#dismissAllowingStateLoss"); }
    }

    /**
     * The android could not find the target Fragment in a short period time
     * soon after the app has committed a fragment transaction via FragmentManager.
     * @param fragmentManager
     * @param dialog
     * @return
     */
    @Nullable
    public static DialogFragment hasDialogFragmentExisted(
            @NonNull FragmentManager fragmentManager, @NonNull DialogFragment dialog) {
        final List<Fragment> fragments = fragmentManager.getFragments();
        for (final Fragment fragment : fragments) {
            if (dialog.getClass().isAssignableFrom(fragment.getClass())) {
                final DialogFragment found = (DialogFragment) fragment;
                LogWrapper.showLog(Log.INFO, "FragmentUtils", "hasDialogFragmentExisted - found: " + found.getClass());
                return found;
            }
        }
        return null;
    }

    public static boolean isDialogShowing(@Nullable DialogFragment dialog) {
        return (null != dialog) && (null != dialog.getDialog())
                && (dialog.getDialog().isShowing())
                && (!dialog.isRemoving());
    }
}
