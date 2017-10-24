package tw.realtime.project.rtbaseframework.reactives;


/**
 * Created by vexonelite on 2016/11/14.
 */

public class ApiRequestBuilder implements ApiParameterSetDelegate {

    private String bApiUrl;
    private String bAccessToken;
    private String bDeviceToken;
    private ApiDataDelegate bApiDataDelegate;

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
    public boolean doesEnableAesEncoding () {
        return false;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return false;
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
}
