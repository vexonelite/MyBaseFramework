package tw.realtime.project.rtbaseframework.apis;

import androidx.annotation.NonNull;

import java.io.IOException;


public final class ExceptionHelper {

    @NonNull
    public static IeHttpException toIeHttpException(@NonNull Exception cause) {
        if (cause instanceof IeHttpException) {
            return (IeHttpException) cause;
        }
        else if (cause instanceof IOException) {
            return new IeHttpException(cause, ErrorCodes.HTTP.REQUEST_ERROR, "", "");
        }
        else {
            return new IeHttpException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR, "", "");
        }
    }

    @NonNull
    public static IeHttpException toIeHttpException(@NonNull Throwable cause) {
        if (cause instanceof IeHttpException) {
            return (IeHttpException) cause;
        }
        else if (cause instanceof IOException) {
            return new IeHttpException(cause, ErrorCodes.HTTP.REQUEST_ERROR, "", "");
        }
        else {
            return new IeHttpException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR, "", "");
        }
    }

    @NonNull
    public static IeRuntimeException toIeRuntimeException(@NonNull Exception cause) {
        if (cause instanceof IeRuntimeException) {
            return (IeRuntimeException) cause;
        }
        else {
            return new IeRuntimeException("unknown error", "00000");
        }
    }

    @NonNull
    public static IeRuntimeException toIeRuntimeException(@NonNull Throwable cause) {
        if (cause instanceof IeRuntimeException) {
            return (IeRuntimeException) cause;
        }
        else {
            return new IeRuntimeException("unknown error", "00000");
        }
    }

}
