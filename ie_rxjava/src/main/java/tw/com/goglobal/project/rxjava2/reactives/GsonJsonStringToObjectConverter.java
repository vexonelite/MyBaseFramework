package tw.com.goglobal.project.rxjava2.reactives;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import io.reactivex.rxjava3.functions.Function;
import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public final class GsonJsonStringToObjectConverter<T>
        extends IeBaseConverter<String, T> implements Function<String, T> {

    private Class<T> tClass;

    public GsonJsonStringToObjectConverter(@NonNull Class<T> tClass) { this.tClass = tClass; }

    @NonNull
    @Override
    public T apply(@NonNull String jsonString) throws IeRuntimeException {
        return convertIntoData(jsonString);
    }

    @NonNull
    @Override
    protected T doConversion(@NonNull String input) throws IeRuntimeException {
        try {
            final T result = new Gson().fromJson(input, tClass);
            if (null != result) { return result; }
            else { throw new IeRuntimeException("Gson().fromJson returns a null result!!", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR); }
        }
        catch (Exception cause) {
            if (cause instanceof IeRuntimeException) { throw (IeRuntimeException) cause; }
            else { throw new IeRuntimeException(input, cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR); }
        }
    }
}

