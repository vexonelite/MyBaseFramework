package tw.realtime.project.rtbaseframework.dialogs;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import tw.realtime.project.rtbaseframework.R;


public final class DialogDataBuilder {

    private boolean titleVisibility = true;

    private String title = "";
    private String message = "";
    private String cancelButtonTitle = "";
    private String confirmButtonTitle = "";

    private int titlePaddingBottom = 8;

    @ColorInt
    private int dividerColor;
    @ColorInt
    private int cancelButtonColor;
    @ColorInt
    private int confirmButtonColor;


    public DialogDataBuilder setTitleVisibility(boolean visibility) {
        this.titleVisibility = visibility;
        return this;
    }

    public boolean theTitleVisibility() { return titleVisibility; }

    public DialogDataBuilder setTitle(@NonNull String title) {
        this.title = title;
        return this;
    }

    public DialogDataBuilder setTitlePaddingBottom(final int paddingBottom) {
        this.titlePaddingBottom = paddingBottom;
        return this;
    }

    public int theTitlePaddingBottom() { return titlePaddingBottom; }

    public DialogDataBuilder setMessage(@NonNull String message) {
        this.message = message;
        return this;
    }

    public DialogDataBuilder setCancelButtonTitle(@NonNull String cancelButtonTitle) {
        this.cancelButtonTitle = cancelButtonTitle;
        return this;
    }

    public DialogDataBuilder setConfirmButtonTitle(@NonNull String confirmButtonTitle) {
        this.confirmButtonTitle = confirmButtonTitle;
        return this;
    }

    ///

    public DialogDataBuilder setDividerColor(@ColorInt int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    public DialogDataBuilder setCancelButtonColor(@ColorInt int cancelButtonColor) {
        this.cancelButtonColor = cancelButtonColor;
        return this;
    }

    public DialogDataBuilder setConfirmButtonColor(@ColorInt int confirmButtonColor) {
        this.confirmButtonColor = confirmButtonColor;
        return this;
    }

    ///

    @NonNull
    public String theTitle() { return title; }

    @NonNull
    public String theMessage() { return message; }

    @NonNull
    public String theCancalButtonTitle() { return cancelButtonTitle; }

    @NonNull
    public String theConfirmButtonTitle() { return confirmButtonTitle; }

    ///

    @ColorInt
    public int theDividerColor() { return dividerColor; }

    @ColorInt
    public int theCancelButtonColor() { return cancelButtonColor; }

    @ColorInt
    public int theConfirmButtonColor() { return confirmButtonColor; }

    ///

    @NonNull
    public static DialogDataBuilder getDefaultBuilder(@NonNull Context context) {
        return new DialogDataBuilder()
                .setCancelButtonTitle(context.getApplicationContext().getString(R.string.base_action_cancel))
                .setCancelButtonColor(ContextCompat.getColor(context.getApplicationContext(), R.color.base_grey_27))
                .setConfirmButtonColor(ContextCompat.getColor(context.getApplicationContext(), android.R.color.holo_blue_dark))
                .setDividerColor(ContextCompat.getColor(context.getApplicationContext(), R.color.base_grey_40));
    }
}
