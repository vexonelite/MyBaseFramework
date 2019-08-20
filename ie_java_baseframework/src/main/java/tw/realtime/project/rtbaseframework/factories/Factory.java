package tw.realtime.project.rtbaseframework.factories;

import androidx.annotation.NonNull;

public interface Factory<T> {
    @NonNull
    T create();
}
