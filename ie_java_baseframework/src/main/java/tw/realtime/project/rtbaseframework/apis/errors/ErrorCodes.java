package tw.realtime.project.rtbaseframework.apis.errors;

public final class ErrorCodes {

    public interface Base {
        String ILLEGAL_ARGUMENT_ERROR = "99998";

        String FAIL_TO_WRITE_TO_FILE = "99997";
        String FAIL_TO_READ_FROM_FILE = "99996";

        String RUN_PREDICATION_ERROR = "99995";

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

        String NETWORK_IS_DISCONNECTED = "99950";

        String THREAD_INTERRUPTED = "99949";

        String CALCULATE_DIFF_RESULT_ERROR = "99948";

        String NULL_POINTER_ERROR = "99947";

        String DEFAULT_UNIT_TEST_ERROR = "99946";

        String INTERNAL_GENERATION_ERROR = "99945";
        String INTERNAL_CONVERSION_ERROR = "99944";
        String INTERNAL_FILTERING_ERROR = "99943";
        String INTERNAL_PROCESS_ERROR = "99942";
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
        String DROP_TABLE_FAILURE = "99690";
    }


    public interface HTTP {
        String FAIL_TO_EXECUTE_API_REQUEST = "99599";
        /** catch the {@link java.io.IOException} */
        String REQUEST_ERROR = "99598";
        String RESPONSE_ERROR = "99597";
        String RESPONSE_PARSING_ERROR = "99596";
        String REQUEST_WRAPPING_FAILURE = "99595";
        String REQUEST_PARAMETER_EXAMINATION_FAILURE = "99594";
        String SERVER_INVALID_ACCESS_TOKEN = "99593";
        String WRONG_STATUS_CODE = "99592";
        String MALFORMED_URL = "99591";
    }

    public interface Socket {
        String CREATION_FAILURE = "99499";
        String CONNECTION_FAILURE = "99498";
        String CONNECTION_TIMEOUT = "99497";
        String SOCKET_HAS_BEEN_CLOSED = "99496";
        String CLOSE_FAILURE = "99495";
        String CHANNEL_REGISTRATION_FAILURE = "99494";
        String FAIL_TO_SEND_OUT_PACKET = "99493";
        String FAIL_TO_READ_FROM_OUT_PACKET = "99492";
        String FAIL_TO_READ_TO_UDP_PACKET = "99491";
        String READ_TIMEOUT = "99490";
        String INVALID_INCOMING_PACKET_FORMAT = "99489";
        String INVALID_CALIBRATION_CHANNEL_VALUE = "99488";
        String SELECTOR_IS_NULL = "99487";
        String NO_SELECTED_RESULT = "99486";
        String NO_WORKABLE_RESULT = "99485";

        String GENERAL_OPERATION_FAILURE = "99401";
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
        String DELETE_FAILURE = "99391";
        String REMOTE_PASV_MODE_FAILURE = "99390";

        String GENERAL_OPERATION_FAILURE = "99379";
    }

    public interface WiFi {
        String TIMEOUT = "99299";
        String REACH_RETRY_LIMIT = "99298";
        /** general case for the device cannot connect to the specific Wi-Fi router */
        String FAIL_TO_CONNECT_TO_SSID = "99297";
        /** used when {@link android.net.ConnectivityManager.NetworkCallback#onUnavailable} gets called */
        String NETWORK_UNAVAILABLE = "99296";
        /** used when either {@link android.net.ConnectivityManager.NetworkCallback#onAvailable} or
         * {@link android.net.ConnectivityManager.NetworkCallback#onCapabilitiesChanged} gets called
         * but the SSID of connected Wi-Fi is not the specific one!
         */
        String CONNECTED_SSID_IS_UNEXPECTED = "99295";
    }
}

