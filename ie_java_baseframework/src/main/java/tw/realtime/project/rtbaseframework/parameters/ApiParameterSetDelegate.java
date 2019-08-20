package tw.realtime.project.rtbaseframework.parameters;

import androidx.annotation.NonNull;

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
    @NonNull
    ApiParameterSetDelegate setAesEncodingEnableFlag(boolean flag);
    /**
     * @param aesKey
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setAesKey(@NonNull String aesKey);
    /**
     * @param aesIv
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setAesIv(@NonNull String aesIv);
    /**
     * @param flag the flag of enabling HTTP Log
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setHttpLogEnableFlag(boolean flag);
    /**
     * @param apiUrl the URL of Api
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setApiUrl(@NonNull String apiUrl);
    /**
     * @param accessToken the access token
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setAccessToken(@NonNull String accessToken);
    /**
     * @param deviceToken the device token
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setDeviceToken(@NonNull String deviceToken);
    /**
     * @param delegate the key-value parameters
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setApiDataDelegate(@NonNull ApiDataDelegate delegate);
    /**
     * @param timeGap the cache expiry time
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setTimeGap(long timeGap);
    /**
     * @param flag the flag of enabling SQLite cache
     * @return
     */
    @NonNull
    ApiParameterSetDelegate setCacheEnableFlag(boolean flag);
}
