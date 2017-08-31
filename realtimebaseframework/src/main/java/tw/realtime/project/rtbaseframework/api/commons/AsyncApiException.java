package tw.realtime.project.rtbaseframework.api.commons;

/**
 * 執行 Api 過程中會丟出的例外
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public class AsyncApiException extends RuntimeException {
    private String code;
    private String optionCode;

    /**
     * 建構子
     * @param theCode    自訂錯誤碼
     * @param theMessage 錯誤訊息
     */
    public AsyncApiException(String theCode, String theMessage) {
        super(theMessage);
        code = theCode;
    }

    /**
     * 建構子
     * @param theCode   自訂錯誤碼
     * @param throwable 錯誤物件
     */
    public AsyncApiException(String theCode, Throwable throwable) {
        super(throwable);
        code = theCode;
    }

    /**
     * 建構子
     * @param theCode    自訂錯誤碼
     * @param theMessage 錯誤訊息
     * @param optCode    額外輔助碼
     */
    public AsyncApiException(String theCode, String theMessage, String optCode) {
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
    public AsyncApiException(String theCode, Throwable throwable, String optCode) {
        super(throwable);
        code = theCode;
        optionCode = optCode;
    }

    /**
     * 取得自訂錯誤碼
     * @return 自訂錯誤碼
     */
    public String getStatusCode () {
        return code;
    }

    /**
     * 取得額外輔助碼
     * @return 額外輔助碼
     */
    public String getOptionCode () {
        return optionCode;
    }
}
