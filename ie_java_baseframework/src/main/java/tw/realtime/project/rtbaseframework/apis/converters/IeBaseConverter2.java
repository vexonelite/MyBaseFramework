package tw.realtime.project.rtbaseframework.apis.converters;

import android.util.Log;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResponse;


public abstract class IeBaseConverter2<T, R> implements IMapFunction<T, IeApiResponse<R>> {

    @NonNull
    protected final String getLogTag() {
        return this.getClass().getSimpleName();
    }

    @NonNull
    @Override
    public final IeApiResponse<R> convertIntoData(@NonNull T input) throws IeRuntimeException {
        try {
            final R result = doConversion(input);
            return new IeApiResponse<>(result, null);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Error on doConversion()");
            final IeRuntimeException error = (cause instanceof IeRuntimeException)
                ? (IeRuntimeException) cause : new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            return new IeApiResponse<>(null, error);
        }
    }

    @NonNull
    protected abstract R doConversion(@NonNull T input) throws Exception;
}
