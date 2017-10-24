package tw.realtime.project.rtbaseframework.reactives;

import io.reactivex.SingleOnSubscribe;
import okhttp3.Response;


public abstract class BaseApiRequestSingle implements ApiParameterDelegate, SingleOnSubscribe<Response> {

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

    protected BaseApiRequestSingle(ApiParameterSetDelegate delegate) {
        if (null != delegate) {
            mApiUrl = delegate.getApiUrl();
            mAccessToken = (null != delegate.getAccessToken()) ? delegate.getAccessToken() : "";
            mDeviceToken = (null != delegate.getDeviceToken()) ? delegate.getDeviceToken() : "";
            mApiDataDelegate = delegate.getApiDataDelegate();
        }
    }
}