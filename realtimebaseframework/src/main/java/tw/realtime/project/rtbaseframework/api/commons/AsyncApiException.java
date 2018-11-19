package tw.realtime.project.rtbaseframework.api.commons;

import androidx.annotation.NonNull;

/**
 * 執行 Api 過程中會丟出的例外
 * <p>
 * Created by vexonelite on 2017/5/25.
 * revision on 2018/11/19.
 */
public final class AsyncApiException extends RuntimeException {

    private final String exceptionCode;
    private final String statusCode;
    private final String jsonResponse;

    public AsyncApiException(@NonNull String message,
                             @NonNull String exceptionCode,
                             @NonNull String statusCode,
                             @NonNull String jsonResponse) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    public AsyncApiException(@NonNull String message,
                             @NonNull Throwable cause,
                             @NonNull String exceptionCode,
                             @NonNull String statusCode,
                             @NonNull String jsonResponse) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    public AsyncApiException(@NonNull Throwable cause,
                             @NonNull String exceptionCode,
                             @NonNull String statusCode,
                             @NonNull String jsonResponse) {
        super(cause);
        this.exceptionCode = exceptionCode;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    /**
     *
     */
    @NonNull
    public String getExceptionCode () {
        return exceptionCode;
    }

    /**
     *
     */
    @NonNull
    public String getStatusCode () {
        return statusCode;
    }

    /**
     *
     */
    @NonNull
    public String getJsonResponse () {
        return jsonResponse;
    }
}
