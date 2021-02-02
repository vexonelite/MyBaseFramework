package tw.realtime.project.rtbaseframework.delegates.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tw.realtime.project.rtbaseframework.enumerations.DialogAction;


public interface DialogDecisionDelegate2<T> {
    void onDecisionMade(@NonNull DialogAction action, @Nullable T item);
}
