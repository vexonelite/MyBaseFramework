package tw.realtime.project.rtbaseframework.apis.converters;

import android.util.Log;
import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public abstract class IeBaseConverter<T, R> implements IMapFunction<T, R> {

    @NonNull
    protected String getLogTag() {
        return this.getClass().getSimpleName();
    }

    @NonNull
    @Override
    public final R convertIntoData(@NonNull T input) throws IeRuntimeException {
        try {
            return doConversion(input);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Error on doConversion()");
            if (cause instanceof IeRuntimeException) {
                throw ((IeRuntimeException) cause);
            }
            else {
                throw new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
        }
    }

    @NonNull
    protected abstract R doConversion(@NonNull T input) throws Exception ;
}
