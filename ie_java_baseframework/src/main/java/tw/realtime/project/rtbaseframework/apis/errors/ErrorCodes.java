package tw.realtime.project.rtbaseframework.apis.errors;

public final class ErrorCodes {

    public interface Base {
        String INTERNAL_CONVERSION_ERROR = "99999";

        String ILLEGAL_ARGUMENT_ERROR = "99998";

        String JSON_WRAPPING_FAILURE = "99989";
        String JSON_PARSING_FAILURE = "99988";
        String NO_SPECIFIED_KEY_IN_JSON = "99987";

        String AES_ENCRYPTION_ERROR = "99979";
        String AES_DECRYPTION_ERROR = "99978";

        String IMAGE_CONVERSION_ERROR = "99959";
        String DATE_ITEM_GENERATION_FAILURE = "99958";
        String DISPLAY_ERROR_CODE_AND_MESSAGE = "99957";
        String XML_PARSING_FAILURE = "99956";
        String FACEBOOK_GRAPH_RESPONSE_ERROR = "99955";
        String RX_MAYBE_ON_COMPLETE = "99954";
        String OUT_OF_MEMORY = "99953";

        String UNSUPPORTED_ENCODING = "99952";

        String INVALID_REQUEST_CODE = "99951";

        //String JOB_CANCELLATION_REMINDER = "99951";
    }

    public interface Ping {
        String TIMEOUT = "99799";
        String INTERRUPTED = "99798";
        String IO_EXCEPTION = "99797";
        String ALL_PACKETS_GOT_LOST = "99796";
        String PARTIAL_PACKETS_GOT_LOST = "99795";
        String UNKNOWN_HOST = "99794";
        String EXIT_VALUE_ONE = "99793";
        String EXIT_VALUE_OTHERS = "99792";
        String INDEX_ERROR = "99791";
        String REACH_RETRY_LIMIT = "99790";
    }

    public interface SQL {
        String OPERATION_FAILURE = "99699";
        String OBJECT_DOES_NOT_EXIST = "99698";
        String NO_KEY_JSON_FOR_OBJECT = "99697";
        String QUERY_FAILURE = "99696";
        String DELETE_FAILURE = "99695";
        String INSERT_FAILURE = "99694";
        String UPDATE_FAILURE = "99693";
        String CURSOR_CONVERSION_ERROR = "99692";
        String HAS_EXISTED_IN_SQL = "99691";
    }


    public interface HTTP {
        String FAIL_TO_EXECUTE_API_REQUEST = "99599";
        String REQUEST_ERROR = "99598";
        String RESPONSE_ERROR = "99597";
        String RESPONSE_PARSING_ERROR = "99596";
        String REQUEST_WRAPPING_FAILURE = "99595";
        String REQUEST_PARAMETER_EXAMINATION_FAILURE = "99594";
        String SERVER_INVALID_ACCESS_TOKEN = "99593";
        String WRONG_STATUS_CODE = "99592";
        String MALFORMED_URL = "99591";
    }

    public interface FTP {
        String CONNECTION_FAILURE = "99399";
        String CONNECTION_TIMEOUT = "99398";
        String LOGIN_FAILURE = "99397";
        String LOGOUT_FAILURE = "99396";
        String CHANGE_DIRECTORY_FAILURE = "99395";
        String FAIL_TO_GET_FILE_LIST = "99394";
        String DOWNLOAD_FAILURE = "99393";
        String UPLOAD_FAILURE = "99392";

        String GENERAL_OPERATION_FAILURE = "99379";
    }


    public interface Socket {
        String CONNECTION_FAILURE = "99499";
        String CONNECTION_TIMEOUT = "99498";
        String SOCKET_HAS_BEEN_CLOSED = "99497";
        String CHANNEL_REGISTRATION_FAILURE = "99496";
        String FAIL_TO_SEND_OUT_PACKET = "99495";
        String FAIL_TO_READ_FROM_OUT_PACKET = "99494";
        String INVALID_INCOMING_PACKET_FORMAT = "99493";
        String INVALID_CALIBRATION_CHANNEL_VALUE = "99492";
    
        String SELECTOR_IS_NULL = "99491";
        String NO_SELECTED_RESULT = "99490";
        String NO_WORKABLE_RESULT = "99489";

        String GENERAL_OPERATION_FAILURE = "99479";
    }

}

