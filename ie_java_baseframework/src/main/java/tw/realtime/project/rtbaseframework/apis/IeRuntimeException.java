package tw.realtime.project.rtbaseframework.apis;

import androidx.annotation.NonNull;


public class IeRuntimeException extends RuntimeException {

    private final String exceptionCode;
    private final String statusCode;
    private final String jsonResponse;

    public IeRuntimeException(
            @NonNull String message,
            @NonNull String exceptionCode,
            @NonNull String statusCode,
            @NonNull String jsonResponse
    ) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    public IeRuntimeException(
            @NonNull String message,
            @NonNull Throwable cause,
            @NonNull String exceptionCode,
            @NonNull String statusCode,
            @NonNull String jsonResponse
    ) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    public IeRuntimeException(
            @NonNull Throwable cause,
            @NonNull String exceptionCode,
            @NonNull String statusCode,
            @NonNull String jsonResponse
    ) {
        super(cause);
        this.exceptionCode = exceptionCode;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    /**
     *
     */
    @NonNull
    public final String getExceptionCode () {
        return exceptionCode;
    }

    /**
     *
     */
    @NonNull
    public final String getStatusCode () {
        return statusCode;
    }

    /**
     *
     */
    @NonNull
    public final String getJsonResponse () {
        return jsonResponse;
    }
}
