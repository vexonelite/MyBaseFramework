package tw.realtime.project.rtbaseframework.apis.errors;

import androidx.annotation.NonNull;


public final class IeSocketException extends IeRuntimeException {

    public IeSocketException(@NonNull final String message, @NonNull final String exceptionCode) {
        super(message, exceptionCode);
    }

    public IeSocketException(@NonNull final Throwable cause, @NonNull final String exceptionCode) {
        super(cause, exceptionCode);
    }

    public IeSocketException(@NonNull final String message, @NonNull final Throwable cause, @NonNull final String exceptionCode) {
        super(message, cause, exceptionCode);
    }
}