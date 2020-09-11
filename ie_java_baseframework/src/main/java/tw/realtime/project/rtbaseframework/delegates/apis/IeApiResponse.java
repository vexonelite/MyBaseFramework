package tw.realtime.project.rtbaseframework.delegates.apis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public final class IeApiResponse<T>  {

    @Nullable
    public final T result;

    @Nullable
    public final IeRuntimeException error;

    public IeApiResponse(@Nullable final T result, @Nullable final IeRuntimeException error) {
        this.result = result;
        this.error = error;
    }

    @NonNull
    @Override
    public String toString() {
        return "IeApiResponse {result: " + result + ", error: " + error + "}";
    }
}
