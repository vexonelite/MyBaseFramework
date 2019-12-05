package tw.realtime.project.rtbaseframework.apis.converters;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public interface IMapFunction<T, R> {
    /**
     * Convert the input: T into the output:R.
     */
    @NonNull
    R convertIntoData(@NonNull T input) throws IeRuntimeException;
}
