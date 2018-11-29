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
        String FAIL_TO_EXECUTE_API_REQUEST = "99998";
        String HTTP_REQUEST_ERROR = "99997";
        String HTTP_RESPONSE_ERROR = "99996";
        String HTTP_RESPONSE_PARSING_ERROR = "99995";
        String HTTP_REQUEST_WRAPPING_FAILURE = "99994";
        String REQUEST_PARAMETER_EXAMINATION_FAILURE = "99993";
        String JSON_WRAPPING_FAILURE = "99992";
        String JSON_PARSING_FAILURE = "99991";
        String NO_SPECIFIED_KEY_IN_JSON = "99990";
        String SERVER_INVALID_ACCESS_TOKEN = "99989";
        String AES_ENCRYPTION_ERROR = "99988";
        String AES_DECRYPTION_ERROR = "99987";
        String HAS_EXISTED_IN_SQL = "99986";
        String SQLITE_OPERATION_FAILURE = "99985";
        String SQLITE_OBJECT_DOES_NOT_EXIST = "99984";
        String SQLITE_NO_KEY_JSON_FOR_OBJECT = "99983";
        String SQLITE_QUERY_FAILURE = "99982";
        String SQLITE_DELETE_FAILURE = "99981";
        String SQLITE_INSERT_FAILURE = "99980";
        String SQLITE_UPDATE_FAILURE = "99979";
        String SQLITE_CURSOR_CONVERSION_ERROR = "99978";
        String IMAGE_CONVERSION_ERROR = "99977";
        String DATE_ITEM_GENERATION_FAILURE = "99976";
        String DISPLAY_ERROR_CODE_AND_MESSAGE = "99975";
        String XML_PARSING_FAILURE = "99974";
        String FACEBOOK_GRAPH_RESPONSE_ERROR = "99973";
        String RX_MAYBE_ON_COMPLETE = "99972";
        String OUT_OF_MEMORY = "99971";
        String MALFORMED_URL = "99970";
        String UNSUPPORTED_ENCODING = "99969";

        String WRONG_STATUS_CODE = "99968";
    }
}
