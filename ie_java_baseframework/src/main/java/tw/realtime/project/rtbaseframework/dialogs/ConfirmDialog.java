package tw.realtime.project.rtbaseframework.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * 系統預設樣式對話框
 */
public final class ConfirmDialog extends DialogFragment {

    private AlertDialog.Builder mBuilder;

    public ConfirmDialog() {
        this.setCancelable(false);
    }

    public void setAlertDialogBuilder(@NonNull AlertDialog.Builder builder) {
        mBuilder = builder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return mBuilder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        //int style = DialogFragment.STYLE_NO_FRAME, theme = 0;
        //int style = DialogFragment.STYLE_NO_FRAME, theme = android.R.style.Theme_Dialog;

        /*
        mNum = getArguments().getInt("num");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }

        */
        setStyle(style, theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        setWindowStyle();
    }


    /**
     * Set desired looks of the dialog, including background image/ color,
     * window size, margin, etc.
     *
     * Refs:
     * https://gist.github.com/ishitcno1/9408188
     */
    private void setWindowStyle() {
        final Dialog theDialog = getDialog();
        if (null == theDialog) { return; }
        final Window window = getDialog().getWindow();
        if (null == window) { return; }
        final WindowManager.LayoutParams params = window.getAttributes();
        // Use dimAmount to control the amount of dim
        params.dimAmount = 0.6f;
        window.setAttributes(params);
        /*
         * if you want to enable Immersive Mode
         * Ref: {@link tw.realtime.project.rtbaseframework.app.BaseActivity#enableImmersiveMode}
         */
        //window.getDecorView().setSystemUiVisibility(getSystemUiVisibility());
    }

    private int getSystemUiVisibility() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN  // hide status bar
                //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                ;
    }
}
