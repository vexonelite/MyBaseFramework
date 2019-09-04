package tw.realtime.project.rtbaseframework.factories;

import androidx.annotation.NonNull;

public interface ParameterFactory<T, R> {
    @NonNull
    R create(@NonNull T parameter);
}
