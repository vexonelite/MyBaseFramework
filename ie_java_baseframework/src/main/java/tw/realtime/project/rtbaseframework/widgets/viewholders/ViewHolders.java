package tw.realtime.project.rtbaseframework.widgets.viewholders;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.apis.filters.IFilterFunction;
import tw.realtime.project.rtbaseframework.databinding.BaseAdapterImageTitleDescriptionItemBinding;
import tw.realtime.project.rtbaseframework.databinding.BaseDialogSingleActionV2Binding;
import tw.realtime.project.rtbaseframework.databinding.BaseDialogTitleTextInputBinding;
import tw.realtime.project.rtbaseframework.databinding.BaseDialogTwinActionsV2Binding;
import tw.realtime.project.rtbaseframework.delegates.ui.view.DescriptionDelegate;
import tw.realtime.project.rtbaseframework.delegates.ui.view.DialogDecisionDelegate2;
import tw.realtime.project.rtbaseframework.dialogs.DialogDataBuilder;
import tw.realtime.project.rtbaseframework.enumerations.DialogAction;
import tw.realtime.project.rtbaseframework.widgets.CommonItemWrapper;

/**
 * Default ViewHolder for the default ViewType
 * @author elite_lin
 * @version 1.0
 */
public final class ViewHolders {

    public static final class CommonDivider extends RecyclerView.ViewHolder {
        public CommonDivider(View itemView) { super(itemView); }
    }

    public static final class WrapContentLoader extends RecyclerView.ViewHolder {
        public WrapContentLoader(View itemView) { super(itemView); }
    }

    public static final class MatchParentLoader extends RecyclerView.ViewHolder {
        public MatchParentLoader(View itemView) { super(itemView); }
    }

    public static final class MatchParentUnavailable extends RecyclerView.ViewHolder {
        public MatchParentUnavailable(View itemView) { super(itemView); }
    }

    // [start] added in 2020/12/02, revision in 2021/02/25
    public static final class IeSingleText extends RecyclerView.ViewHolder {

        public final TextView textView;

        public IeSingleText(@NonNull final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
        }

        public void onBind(@NonNull final DescriptionDelegate delegate, final int position) {
            textView.setText(delegate.theDescription());
        }
    }

    public static final class IeImageTitleDescription extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView titleView;
        public final TextView descriptionView;

        public IeImageTitleDescription(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
        }

        public IeImageTitleDescription(@NonNull final BaseAdapterImageTitleDescriptionItemBinding viewBinding) {
            super(viewBinding.getRoot());
            imageView = itemView.findViewById(R.id.image);
            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
        }
    }

    // [end] added in 2020/12/02, revision in 2021/02/25

    public static abstract class AbsActionsDialog<T> {

        private DialogDecisionDelegate2<T> dialogCallback;

        private boolean titleVisibility = true;

        private String title = "";
        private String message = "";
        private String cancelButtonTitle = "";
        private String confirmButtonTitle = "";
        private final Rect titlePaddingRect = new Rect(8, 8, 8, 8);
        private final Rect messagePaddingRect = new Rect(10, 16, 10, 16);

        @ColorInt
        private int dividerColor;
        @ColorInt
        private int cancelButtonColor;
        @ColorInt
        private int confirmButtonColor;

        public final AbsActionsDialog<T> setData(@NonNull DialogDataBuilder builder) {
            this.titleVisibility = builder.theTitleVisibility();
            this.title = builder.theTitle();
            this.message = builder.theMessage();
            this.cancelButtonTitle = builder.theCancalButtonTitle();
            this.cancelButtonColor = builder.theCancelButtonColor();
            this.confirmButtonTitle = builder.theConfirmButtonTitle();
            this.confirmButtonColor = builder.theConfirmButtonColor();
            this.dividerColor = builder.theDividerColor();
            this.titlePaddingRect.bottom = builder.theTitlePaddingBottom();
            return this;
        }

        public AbsActionsDialog<T> setDialogDecisionCallback(@NonNull DialogDecisionDelegate2<T> callback) {
            this.dialogCallback = callback;
            return this;
        }

        ///

        protected final void uiInitializationBase(
                @NonNull final TextView titleView,
                @NonNull final TextView messageView,
                @NonNull final TextView confirmButton,
                @NonNull final View dividerHorizontal) {

            final Context context = titleView.getContext().getApplicationContext();
            final float density = context.getResources().getDisplayMetrics().density;

            titleView.setText(title);
            titleView.setVisibility(titleVisibility ? View.VISIBLE : View.GONE);
            titleView.setPadding(
                    (int) density * titlePaddingRect.left,
                    (int) density * titlePaddingRect.top,
                    (int) density * titlePaddingRect.right,
                    (int) density * titlePaddingRect.bottom);

            messageView.setText(message);
            if (!titleVisibility) {
                messageView.setPadding(
                        (int) density * messagePaddingRect.left,
                        (int) density * messagePaddingRect.top,
                        (int) density * messagePaddingRect.right,
                        (int) density * messagePaddingRect.bottom);
            }

            confirmButton.setText(confirmButtonTitle);
            confirmButton.setOnClickListener(new ConfirmButtonClickCallback());
            try { confirmButton.setTextColor(confirmButtonColor); }
            catch (Exception cause) { }

            try { dividerHorizontal.setBackgroundColor(dividerColor); }
            catch (Exception cause) { }
        }

        protected final void uiInitializationExt(
                @NonNull final TextView cancelButton, @NonNull final View dividerVertical) {

            cancelButton.setText(cancelButtonTitle);
            cancelButton.setOnClickListener(new CancelButtonClickCallback());
            try { cancelButton.setTextColor(cancelButtonColor); }
            catch (Exception cause) {}

            try { dividerVertical.setBackgroundColor(dividerColor); }
            catch (Exception cause) {}
        }

        private class ConfirmButtonClickCallback extends CommonItemWrapper<Boolean> implements View.OnClickListener {

            ConfirmButtonClickCallback() { super(false, "", 0); }

            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                if (null != dialogCallback) {
                    dialogCallback.onDecisionMade(DialogAction.CONFIRM, null);
                }
                view.setEnabled(true);
            }
        }

        private class CancelButtonClickCallback extends CommonItemWrapper<Boolean> implements View.OnClickListener {

            CancelButtonClickCallback() { super(false, "", 0); }

            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                if (null != dialogCallback) {
                    dialogCallback.onDecisionMade(DialogAction.CANCEL, null);
                }
                view.setEnabled(true);
            }
        }
    }

    public static final class IeTwinActionsDialog<T> extends AbsActionsDialog<T> {

        private BaseDialogTwinActionsV2Binding dialogTwinActionsBinding;

        public AbsActionsDialog<T> setViewBinding(@Nullable final BaseDialogTwinActionsV2Binding viewBinding) {
            this.dialogTwinActionsBinding = viewBinding;
            return this;
        }

        public final void uiInitializationBase() {
            if (null == dialogTwinActionsBinding) {
                LogWrapper.showLog(Log.INFO, "IeTwinActionsDialog", "uiInitializationBase - dialogTwinActionsBinding is null!!");
                return;
            }
            uiInitializationBase(
                    dialogTwinActionsBinding.title,
                    dialogTwinActionsBinding.message,
                    dialogTwinActionsBinding.confirmButton,
                    dialogTwinActionsBinding.dividerHorizontal);
        }


        public final void uiInitializationExt() {
            if (null == dialogTwinActionsBinding) {
                LogWrapper.showLog(Log.INFO, "IeTwinActionsDialog", "uiInitializationExt - dialogTwinActionsBinding is null!!");
                return;
            }
            uiInitializationExt(dialogTwinActionsBinding.cancelButton, dialogTwinActionsBinding.dividerVertical);
        }
    }

    public static final class IeSingleActionDialog<T> extends AbsActionsDialog<T> {

        private BaseDialogSingleActionV2Binding dialogSingleActionBinding;

        public AbsActionsDialog<T> setViewBinding(@Nullable final BaseDialogSingleActionV2Binding viewBinding) {
            this.dialogSingleActionBinding = viewBinding;
            return this;
        }

        public final void uiInitializationBase() {
            if (null == dialogSingleActionBinding) {
                LogWrapper.showLog(Log.INFO, "IeSingleActionDialog", "uiInitializationBase - dialogSingleActionBinding is null!!");
                return;
            }
            uiInitializationBase(
                    dialogSingleActionBinding.title,
                    dialogSingleActionBinding.message,
                    dialogSingleActionBinding.confirmButton,
                    dialogSingleActionBinding.dividerHorizontal);
        }
    }

    ///

    public final static class TitleTextInput implements TextWatcher {

        private final int resIdCancelButton = R.id.cancelButton;
        private final int resIdConfirmButton = R.id.confirmButton;

        public BaseDialogTitleTextInputBinding titleTextInputBinding;
        private DialogDecisionDelegate2<String> dialogCallback;
        private IFilterFunction<String> filterDelegate;
        public String warningMessage = "";


        private String getLogTag() { return this.getClass().getSimpleName(); }

        public TitleTextInput setViewBinding(@Nullable final BaseDialogTitleTextInputBinding viewBinding) {
            this.titleTextInputBinding = viewBinding;
            return this;
        }

        public TitleTextInput setDialogDecisionDelegate(@Nullable final DialogDecisionDelegate2<String> delegate) {
            this.dialogCallback = delegate;
            return this;
        }

        public TitleTextInput setTextValidationDelegate(@Nullable final IFilterFunction<String> delegate) {
            this.filterDelegate = delegate;
            return this;
        }

        public TitleTextInput setWarningMessage(@Nullable final String warningMessage) {
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
}

