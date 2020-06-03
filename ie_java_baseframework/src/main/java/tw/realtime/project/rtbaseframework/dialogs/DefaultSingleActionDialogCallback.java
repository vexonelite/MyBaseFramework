package tw.realtime.project.rtbaseframework.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tw.realtime.project.rtbaseframework.delegates.ui.view.DialogDecisionDelegate;
import tw.realtime.project.rtbaseframework.enumerations.DialogAction;


public final class DefaultSingleActionDialogCallback<T> implements DialogDecisionDelegate<T> {
    @Override
    public void onDecisionMade(
            @NonNull final DialogFragment dialogFragment,
            @NonNull final DialogAction action,
            @Nullable final T item) {
        dialogFragment.dismiss();
    }
}
