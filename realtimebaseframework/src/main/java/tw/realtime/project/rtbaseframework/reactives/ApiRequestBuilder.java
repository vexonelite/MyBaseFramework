package tw.realtime.project.rtbaseframework.reactives;


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

    @Override
    public ApiParameterSetDelegate setAesEncodingEnableFlag(boolean flag) {
        bAesEncodingEnable = flag;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setAesKey(String aesKey) {
        bAesKey = aesKey;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setAesIv(String aesIv) {
        bAesIv = aesIv;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setHttpLogEnableFlag(boolean flag) {
        bHttpLogEnable = flag;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setApiUrl (String apiUrl) {
        bApiUrl = apiUrl;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setAccessToken (String accessToken) {
        bAccessToken = accessToken;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setDeviceToken(String deviceToken) {
        bDeviceToken = deviceToken;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setApiDataDelegate(ApiDataDelegate delegate) {
        bApiDataDelegate = delegate;
        return this;
    }

    @Override
    public ApiParameterSetDelegate setTimeGap(long timeGap) {
        if (timeGap > 0L) {
            bTimeGap = timeGap;
        }
        return this;
    }

    @Override
    public ApiParameterSetDelegate setCacheEnableFlag(boolean flag) {
        bCacheEnable = flag;
        return this;
    }

    @Override
    public boolean doesEnableAesEncoding () {
        return bAesEncodingEnable;
    }

    @Override
    public String getAesKey() {
        return bAesKey;
    }

    @Override
    public String getAesIv() {
        return bAesIv;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return bHttpLogEnable;
    }

    @Override
    public String getApiUrl () {
        return bApiUrl;
    }

    @Override
    public String getAccessToken () {
        return bAccessToken;
    }

    @Override
    public String getDeviceToken() {
        return bDeviceToken;
    }

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
