package tw.realtime.project.rtbaseframework.delegates.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tw.realtime.project.rtbaseframework.enumerations.DialogAction;


public interface DialogDecisionDelegate<T> {
    void onDecisionMade(
            @NonNull DialogFragment dialogFragment, @NonNull DialogAction action, @Nullable T item);
}
