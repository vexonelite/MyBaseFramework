package tw.realtime.project.rtbaseframework.apis.errors;

import androidx.annotation.NonNull;


public final class IeHttpException extends IeRuntimeException {

    // 額外輔助碼
    private final String statusCode;
    private final String jsonResponse;

    public IeHttpException(
            @NonNull String message,
            @NonNull String exceptionCode,
            @NonNull String statusCode,
            @NonNull String jsonResponse
    ) {
        super(message, exceptionCode);
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    public IeHttpException(
            @NonNull Throwable cause,
            @NonNull String exceptionCode,
            @NonNull String statusCode,
            @NonNull String jsonResponse
    ) {
        super(cause, exceptionCode);
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    public IeHttpException(
            @NonNull String message,
            @NonNull Throwable cause,
            @NonNull String exceptionCode,
            @NonNull String statusCode,
            @NonNull String jsonResponse
    ) {
        super(message, cause, exceptionCode);
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
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
