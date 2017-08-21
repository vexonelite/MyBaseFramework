package tw.realtime.project.rtbaseframework.commons;

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
        String JSON_WRAPPING_FAILURE = "99994";
        String JSON_PARSING_FAILURE = "99993";
        String NO_SPECIFIED_KEY_IN_JSON = "99992";
        String SERVER_INVALID_ACCESS_TOKEN = "99991";
        String HAS_EXISTED_IN_SQL = "99990";
        String SQLITE_OPERATION_FAILURE = "99988";
        String SQLITE_OBJECT_DOES_NOT_EXIST = "99987";
        String SQLITE_NO_KEY_JSON_FOR_OBJECT = "99986";
        String DATE_ITEM_GENERATION_FAILURE = "99984";
        String DISPLAY_ERROR_CODE_AND_MESSAGE = "99983";
        String SQLITE_QUERY_FAILURE = "99982";
        String SQLITE_DELETE_FAILURE = "99981";
        String SQLITE_INSERT_FAILURE = "99980";
        String SQLITE_CURSOR_CONVERSION_ERROR = "99979";
        String IMAGE_CONVERSION_ERROR = "99978";
    }
}
