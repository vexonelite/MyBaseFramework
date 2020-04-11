package tw.realtime.project.rtbaseframework.apis.filters;

import android.util.Log;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public abstract class IeBaseFilter<T> implements IFilterFunction<T> {

    @NonNull
    protected String getLogTag() {
        return this.getClass().getSimpleName();
    }

    @NonNull
    @Override
    public final boolean predicate(@NonNull T input) throws IeRuntimeException {
        try {
            return doTesting(input);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Error on doTesting()");
            if (cause instanceof IeRuntimeException) {
                throw ((IeRuntimeException) cause);
            }
            else {
                throw new IeRuntimeException(cause, ErrorCodes.Base.RUN_PREDICATION_ERROR);
            }
        }
    }

    protected abstract boolean doTesting(@NonNull T input) throws Exception;
}
