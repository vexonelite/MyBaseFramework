package tw.realtime.project.rtbaseframework.models;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.delegates.OptionDelegate;

public final class SimpleStringOption implements OptionDelegate<String> {

    private final String option;

    public SimpleStringOption(@NonNull String option) {
        this.option = option;
    }

    @NonNull
    @Override
    public String getOptionTitle() {
        return option;
    }

    @NonNull
    @Override
    public String getHeldObject() {
        return option;
    }
}
