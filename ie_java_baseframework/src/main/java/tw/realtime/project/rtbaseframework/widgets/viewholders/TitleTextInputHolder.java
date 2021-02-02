package tw.realtime.project.rtbaseframework.widgets.viewholders;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.apis.filters.IFilterFunction;
import tw.realtime.project.rtbaseframework.databinding.BaseDialogTitleTextInputBinding;
import tw.realtime.project.rtbaseframework.delegates.ui.view.DialogDecisionDelegate2;
import tw.realtime.project.rtbaseframework.enumerations.DialogAction;
import tw.realtime.project.rtbaseframework.widgets.CommonItemWrapper;


public final class TitleTextInputHolder implements TextWatcher {

    private final int resIdCancelButton = R.id.cancelButton;
    private final int resIdConfirmButton = R.id.confirmButton;

    public BaseDialogTitleTextInputBinding titleTextInputBinding;
    private DialogDecisionDelegate2<String> dialogCallback;
    private IFilterFunction<String> filterDelegate;
    public String warningMessage = "";


    private String getLogTag() { return this.getClass().getSimpleName(); }

    public TitleTextInputHolder setViewBinding(@Nullable final BaseDialogTitleTextInputBinding viewBinding) {
        this.titleTextInputBinding = viewBinding;
        return this;
    }

    public TitleTextInputHolder setDialogDecisionDelegate(@Nullable final DialogDecisionDelegate2<String> delegate) {
        this.dialogCallback = delegate;
        return this;
    }

    public TitleTextInputHolder setTextValidationDelegate(@Nullable final IFilterFunction<String> delegate) {
        this.filterDelegate = delegate;
        return this;
    }

    public TitleTextInputHolder setWarningMessage(@Nullable final String warningMessage) {
        if (null != warningMessage) { this.warningMessage = warningMessage; }
        return this;
    }

    public void uiInitialization(
            @Nullable final String title, final boolean titleVisibility,
            @Nullable final String message, final boolean messageVisibility,
            @Nullable final String input) {
        if (null == titleTextInputBinding) {
            LogWrapper.showLog(Log.INFO, "TitleTextInputHolder", "uiInitialization - titleTextInputBinding is null!!");
            return;
        }

        final View[] buttonArray = {
                titleTextInputBinding.cancelButton, titleTextInputBinding.confirmButton,
        };
        for (final View button : buttonArray) {
            button.setOnClickListener(new MyOnClickCallback());
        }

        if (null != title) { titleTextInputBinding.title.setText(title); }
        titleTextInputBinding.title.setVisibility(titleVisibility ? View.VISIBLE : View.GONE);

        if (null != message) { titleTextInputBinding.message.setText(message); }
        titleTextInputBinding.message.setVisibility(messageVisibility ? View.VISIBLE : View.GONE);

        titleTextInputBinding.warningMessage.setText(warningMessage);

        if (null != input) { titleTextInputBinding.textInput.setText(input); }
        titleTextInputBinding.textInput.addTextChangedListener(this);

        if ( (null != filterDelegate) && (null != input) ) {
            final boolean isValid = filterDelegate.predicate(input);
            updateConfirmButtonLooks(isValid);
            //alterWarningMessageVisibility();
        }
    }

    private class MyOnClickCallback extends CommonItemWrapper<Boolean> implements View.OnClickListener {

        MyOnClickCallback() { super(false, "", 0); }

        @Override
        public void onClick(View view) {
            view.setEnabled(false);

            if (view.getId() == resIdConfirmButton) {
                if ( (null != titleTextInputBinding) && (null != dialogCallback) ) {
                    final String rawComment = titleTextInputBinding.textInput.getText().toString().trim();
                    dialogCallback.onDecisionMade(DialogAction.CONFIRM, rawComment);
                }
            }
            else if (view.getId() == resIdCancelButton) {
                if (null != dialogCallback) { dialogCallback.onDecisionMade(DialogAction.CANCEL, null); }
            }

            view.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onTextChanged - start: " + start + ", count: " + count);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "afterTextChanged: " + editable.toString());
        final String comment = editable.toString().trim();
        if (null != filterDelegate) {
            final boolean isValid = filterDelegate.predicate(comment);
            updateConfirmButtonLooks(isValid);
            //alterWarningMessageVisibility();
        }
    }

    private void updateConfirmButtonLooks(final boolean isValid) {
        if (null == titleTextInputBinding) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "updateConfirmButtonLooks - titleTextInputBinding is null!!");
            return;
        }

        final Context context = titleTextInputBinding.getRoot().getContext();
        final int colorResId = isValid ? android.R.color.holo_blue_dark : R.color.base_grey_40;
        final int textColor = ContextCompat.getColor(context.getApplicationContext(), colorResId);
        titleTextInputBinding.confirmButton.setTextColor(textColor);
        titleTextInputBinding.confirmButton.setEnabled(isValid);
    }
}
