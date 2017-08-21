package tw.realtime.project.rtbaseframework.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * 系統預設樣式對話框
 */
public class ConfirmDialog extends DialogFragment {

    private AlertDialog.Builder mBuilder;

    public ConfirmDialog() {
        this.setCancelable(false);
    }

    public void setAlertDialogBuilder (AlertDialog.Builder builder) {
        mBuilder = builder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mBuilder.create();
    }
}
