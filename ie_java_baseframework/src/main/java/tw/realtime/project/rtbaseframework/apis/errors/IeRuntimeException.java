package tw.realtime.project.rtbaseframework.apis.errors;

import androidx.annotation.NonNull;


public class IeRuntimeException extends RuntimeException {
    //自訂錯誤碼
    private final String exceptionCode;

    public IeRuntimeException(@NonNull String message, @NonNull String exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public IeRuntimeException(@NonNull Throwable cause, @NonNull String exceptionCode) {
        super(cause);
        this.exceptionCode = exceptionCode;
    }

    public IeRuntimeException(
            @NonNull String message, @NonNull Throwable cause, @NonNull String exceptionCode) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    /**
     *
     */
    @NonNull
    public final String getExceptionCode () { return exceptionCode; }
}
