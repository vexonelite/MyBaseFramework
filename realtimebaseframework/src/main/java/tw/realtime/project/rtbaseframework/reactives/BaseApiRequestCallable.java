package tw.realtime.project.rtbaseframework.reactives;

import java.util.concurrent.Callable;

import okhttp3.Response;


public abstract class BaseApiRequestCallable implements ApiParameterDelegate, Callable<Response> {

    private String mApiUrl;
    private String mAccessToken;
    private String mDeviceToken;
    private ApiDataDelegate mApiDataDelegate;

    @Override
    public boolean doesEnableAesEncoding () {
        return false;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return true;
    }

    @Override
    public String getApiUrl () {
        return mApiUrl;
    }

    @Override
    public String getAccessToken () {
        return mAccessToken;
    }

    @Override
    public String getDeviceToken() {
        return mDeviceToken;
    }

    @Override
    public ApiDataDelegate getApiDataDelegate () {
        return mApiDataDelegate;
    }

    protected BaseApiRequestCallable(ApiParameterSetDelegate delegate) {
        if (null != delegate) {
            mApiUrl = delegate.getApiUrl();
            mAccessToken = (null != delegate.getAccessToken()) ? delegate.getAccessToken() : "";
            mDeviceToken = (null != delegate.getDeviceToken()) ? delegate.getDeviceToken() : "";
            mApiDataDelegate = delegate.getApiDataDelegate();
        }
    }
}