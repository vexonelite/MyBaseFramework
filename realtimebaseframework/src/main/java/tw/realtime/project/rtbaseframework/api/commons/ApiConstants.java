package tw.realtime.project.rtbaseframework.api.commons;

/**
 * 定義 Api 共用的常數
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public class ApiConstants {

    /**
     * OkHttp 的設定
     */
    public interface OkHttpSetting {
        long CONNECTION_TIME = 60000;
        long READ_TIMEOUT = 120000;
        long WRITE_TIMEOUT = 120000;
    }

    /**
     * AsyncApiException 使用到的 Code
     */
    public interface ExceptionCode {
        String INTERNAL_CONVERSION_ERROR = "99999";
        String HTTP_REQUEST_ERROR = "99998";
        String HTTP_RESPONSE_ERROR = "99997";
        String HTTP_RESPONSE_PARSING_ERROR = "99996";
        String HTTP_REQUEST_WRAPPING_FAILURE = "99995";
        String REQUEST_PARAMETER_EXAMINATION_FAILURE = "99994";
        String JSON_WRAPPING_FAILURE = "99993";
        String JSON_PARSING_FAILURE = "99992";
        String NO_SPECIFIED_KEY_IN_JSON = "99991";
        String SERVER_INVALID_ACCESS_TOKEN = "99990";
        String AES_ENCRYPTION_ERROR = "99989";
        String AES_DECRYPTION_ERROR = "99988";
        String HAS_EXISTED_IN_SQL = "99987";
        String SQLITE_OPERATION_FAILURE = "99986";
        String SQLITE_OBJECT_DOES_NOT_EXIST = "99985";
        String SQLITE_NO_KEY_JSON_FOR_OBJECT = "99984";
        String SQLITE_QUERY_FAILURE = "99983";
        String SQLITE_DELETE_FAILURE = "99982";
        String SQLITE_INSERT_FAILURE = "99981";
        String SQLITE_CURSOR_CONVERSION_ERROR = "99980";
        String IMAGE_CONVERSION_ERROR = "99979";
        String DATE_ITEM_GENERATION_FAILURE = "99978";
        String DISPLAY_ERROR_CODE_AND_MESSAGE = "99977";

    }
}
