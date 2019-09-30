package tw.realtime.project.rtbaseframework.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tw.realtime.project.rtbaseframework.delegates.ui.view.DialogDecisionDelegate;
import tw.realtime.project.rtbaseframework.enumerations.DialogAction;


public final class DefaultSingleActionDialogCallback implements DialogDecisionDelegate<Boolean> {
    @Override
    public void onDecisionMade(
            @NonNull DialogFragment dialogFragment,
            @NonNull DialogAction action,
            @Nullable Boolean item) {
        dialogFragment.dismiss();
    }
}
