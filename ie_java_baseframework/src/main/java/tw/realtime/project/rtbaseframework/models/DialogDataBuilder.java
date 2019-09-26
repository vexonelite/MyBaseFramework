package tw.realtime.project.rtbaseframework.models;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


public final class DialogDataBuilder {

    private String title = "";
    private String message = "";
    private String cancelButtonTitle = "";
    private String confirmButtonTitle = "";

    @ColorInt
    private int dividerColor;
    @ColorInt
    private int cancelButtonColor;
    @ColorInt
    private int confirmButtonColor;


    public DialogDataBuilder setTitle(@NonNull String title) {
        this.title = title;
        return this;
    }

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
}
