package tw.realtime.project.rtbaseframework.apis.filters;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public interface IFilterFunction2<T, R> {
    /**
     * Test the given input value and return a boolean.
     */
    boolean predicate(@NonNull T input1, @NonNull R input2) throws IeRuntimeException;
}
