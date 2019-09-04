package tw.realtime.project.rtbaseframework.apis.converters;

import androidx.annotation.NonNull;

public interface IMapFunction<T, R> {
    @NonNull
    R convertIntoData(@NonNull T input);
}
