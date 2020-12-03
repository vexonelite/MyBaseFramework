package tw.realtime.project.rtbaseframework.apis.errors;

import androidx.annotation.NonNull;


public final class IePingException extends IeRuntimeException {

    public IePingException(@NonNull final String message, @NonNull final String exceptionCode) {
        super(message, exceptionCode);
    }

    public IePingException(@NonNull final Throwable cause, @NonNull final String exceptionCode) {
        super(cause, exceptionCode);
    }

    public IePingException(@NonNull final String message, @NonNull final Throwable cause, @NonNull final String exceptionCode) {
        super(message, cause, exceptionCode);
    }
}