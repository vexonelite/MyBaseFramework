package tw.realtime.project.rtbaseframework.reactives;

/**
 * Used to set a configuration of Api call, including, the URL of Api, the access token, the device token,
 * the flag of enabling AES Encryption, the flag of enabling HTTP Log,
 * the key-value parameters, and return the ApiParameterSetDelegate.
 * There are two additional parameters for the configuration: the flag of enabling SQLite cache, and
 * the cache expiry time.
 * The returned ApiParameterSetDelegate is handy for builder-style code.
 * <p>
 * Created by vexonelite on 2017/10/3.
 */
public interface ApiParameterSetDelegate extends ApiParameterDelegate {
    /**
     * @param flag the flag of enabling AES Encryption
     * @return
     */
    ApiParameterSetDelegate setAesEncodingEnableFlag(boolean flag);
    /**
     * @param aesKey
     * @return
     */
    ApiParameterSetDelegate setAesKey(String aesKey);
    /**
     * @param aesIv
     * @return
     */
    ApiParameterSetDelegate setAesIv(String aesIv);
    /**
     * @param flag the flag of enabling HTTP Log
     * @return
     */
    ApiParameterSetDelegate setHttpLogEnableFlag(boolean flag);
    /**
     * @param apiUrl the URL of Api
     * @return
     */
    ApiParameterSetDelegate setApiUrl(String apiUrl);
    /**
     * @param accessToken the access token
     * @return
     */
    ApiParameterSetDelegate setAccessToken(String accessToken);
    /**
     * @param deviceToken the device token
     * @return
     */
    ApiParameterSetDelegate setDeviceToken(String deviceToken);
    /**
     * @param delegate the key-value parameters
     * @return
     */
    ApiParameterSetDelegate setApiDataDelegate(ApiDataDelegate delegate);
    /**
     * @param timeGap the cache expiry time
     * @return
     */
    ApiParameterSetDelegate setTimeGap(long timeGap);
    /**
     * @param flag the flag of enabling SQLite cache
     * @return
     */
    ApiParameterSetDelegate setCacheEnableFlag(boolean flag);
}
