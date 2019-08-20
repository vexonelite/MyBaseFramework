package tw.realtime.project.rtbaseframework.parameters;


import androidx.annotation.NonNull;

/**
 * Created by vexonelite on 2016/11/14.
 */

public class ApiRequestBuilder implements ApiParameterSetDelegate {

    private boolean bAesEncodingEnable = false;
    private boolean bHttpLogEnable = true;
    private boolean bCacheEnable = false;
    private long bTimeGap = 0L;
    private String bApiUrl;
    private String bAccessToken;
    private String bDeviceToken;
    private String bAesKey;
    private String bAesIv;
    private ApiDataDelegate bApiDataDelegate;

    @NonNull
    @Override
    public ApiParameterSetDelegate setAesEncodingEnableFlag(boolean flag) {
        bAesEncodingEnable = flag;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setAesKey(@NonNull String aesKey) {
        bAesKey = aesKey;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setAesIv(@NonNull String aesIv) {
        bAesIv = aesIv;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setHttpLogEnableFlag(boolean flag) {
        bHttpLogEnable = flag;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setApiUrl(@NonNull String apiUrl) {
        bApiUrl = apiUrl;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setAccessToken(@NonNull String accessToken) {
        bAccessToken = accessToken;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setDeviceToken(@NonNull String deviceToken) {
        bDeviceToken = deviceToken;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setApiDataDelegate(@NonNull ApiDataDelegate delegate) {
        bApiDataDelegate = delegate;
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setTimeGap(long timeGap) {
        if (timeGap > 0L) {
            bTimeGap = timeGap;
        }
        return this;
    }

    @NonNull
    @Override
    public ApiParameterSetDelegate setCacheEnableFlag(boolean flag) {
        bCacheEnable = flag;
        return this;
    }

    @Override
    public boolean doesEnableAesEncoding () {
        return bAesEncodingEnable;
    }

    @NonNull
    @Override
    public String getAesKey() {
        return bAesKey;
    }

    @NonNull
    @Override
    public String getAesIv() {
        return bAesIv;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return bHttpLogEnable;
    }

    @NonNull
    @Override
    public String getApiUrl () {
        return bApiUrl;
    }

    @NonNull
    @Override
    public String getAccessToken () {
        return bAccessToken;
    }

    @NonNull
    @Override
    public String getDeviceToken() {
        return bDeviceToken;
    }

    @NonNull
    @Override
    public ApiDataDelegate getApiDataDelegate() {
        return bApiDataDelegate;
    }

    @Override
    public boolean doesEnableCache() {
        return bCacheEnable;
    }

    @Override
    public long getTimeGap() {
        return bTimeGap;
    }
}
