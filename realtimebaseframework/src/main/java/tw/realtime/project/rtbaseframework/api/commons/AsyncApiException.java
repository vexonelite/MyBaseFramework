package tw.realtime.project.rtbaseframework.api.commons;

import androidx.annotation.NonNull;

/**
 * 執行 Api 過程中會丟出的例外
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public final class AsyncApiException extends RuntimeException {

    private final String code;
    private final String optionCode;

    /**
     * 建構子
     * @param theCode    自訂錯誤碼
     * @param theMessage 錯誤訊息
     */
    public AsyncApiException(@NonNull String theCode, @NonNull String theMessage) {
        super(theMessage);
        code = theCode;
        optionCode = "";
    }

    /**
     * 建構子
     * @param theCode   自訂錯誤碼
     * @param throwable 錯誤物件
     */
    public AsyncApiException(@NonNull String theCode, @NonNull Throwable throwable) {
        super(throwable);
        code = theCode;
        optionCode = "";
    }

    /**
     * 建構子
     * @param theCode    自訂錯誤碼
     * @param theMessage 錯誤訊息
     * @param optCode    額外輔助碼
     */
    public AsyncApiException(@NonNull String theCode, @NonNull String theMessage, @NonNull String optCode) {
        super(theMessage);
        code = theCode;
        optionCode = optCode;
    }

    /**
     * 建構子
     * @param theCode   自訂錯誤碼
     * @param throwable 錯誤物件
     * @param optCode   額外輔助碼
     */
    public AsyncApiException(@NonNull String theCode, @NonNull Throwable throwable, @NonNull String optCode) {
        super(throwable);
        code = theCode;
        optionCode = optCode;
    }

    /**
     * 取得自訂錯誤碼
     * @return 自訂錯誤碼
     */
    @NonNull
    public String getStatusCode () {
        return code;
    }

    /**
     * 取得額外輔助碼
     * @return 額外輔助碼
     */
    @NonNull
    public String getOptionCode () {
        return optionCode;
    }
}
