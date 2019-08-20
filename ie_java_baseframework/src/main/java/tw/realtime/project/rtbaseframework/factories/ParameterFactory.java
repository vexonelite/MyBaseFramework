package tw.realtime.project.rtbaseframework.factories;

import androidx.annotation.NonNull;

public interface ParameterFactory<T, R> {
    @NonNull
    T create(@NonNull R parameter);
}
