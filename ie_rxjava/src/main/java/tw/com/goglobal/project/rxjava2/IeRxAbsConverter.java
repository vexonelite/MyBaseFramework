package tw.com.goglobal.project.rxjava2;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.functions.Function;
import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public abstract class IeRxAbsConverter<T, R> extends IeBaseConverter<T, R> implements Function<T, R> {
    @NonNull
    @Override
    public final R apply(@NonNull T input) throws IeRuntimeException { return convertIntoData(input); }
}
