package tw.realtime.project.rtbaseframework.reactives;

/**
 * Used to get a configuration of Api call, including, the URL of Api, the access token, the device token,
 * the flag of enabling AES Encryption, the flag of enabling HTTP Log, and the key-value parameters.
 * There are two additional parameters for the configuration: the flag of enabling SQLite cache, and
 * the cache expiry time.
 * <p>
 * Created by vexonelite on 2017/10/3.
 */
public interface ApiParameterDelegate {
    /**
     * @return the flag of enabling AES Encryption
     */
    boolean doesEnableAesEncoding();
    /**
     * @return the flag of enabling HTTP Log
     */
    boolean doesEnableHttpLog();
    /**
     * @return the URL of Api
     */
    String getApiUrl();
    /**
     * @return the access token
     */
    String getAccessToken();
    /**
     * @return the device token
     */
    String getDeviceToken();
    /**
     * @return the the key-value parameters
     */
    ApiDataDelegate getApiDataDelegate();
    /**
     * @return the flag of enabling SQLite cache
     */
    boolean doesEnableCache();
    /**
     * @return AES encryption Key
     */
    String getAesKey();
    /**
     * @return AES IV
     */
    String getAesIv();
    /**
     * @return the cache expiry time
     */
    long getTimeGap();
}
