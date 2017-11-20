package tw.realtime.project.rtbaseframework.reactives;

import io.reactivex.SingleOnSubscribe;
import okhttp3.Response;


public abstract class BaseApiRequestSingle implements ApiParameterDelegate, SingleOnSubscribe<Response> {

    private boolean mAesEncodingEnable = false;
    private boolean mHttpLogEnable = true;
    private String mApiUrl;
    private String mAccessToken;
    private String mDeviceToken;
    private ApiDataDelegate mApiDataDelegate;

    @Override
    public boolean doesEnableAesEncoding () {
        return mAesEncodingEnable;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return mHttpLogEnable;
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
            mAesEncodingEnable = delegate.doesEnableAesEncoding();
            mHttpLogEnable = delegate.doesEnableHttpLog();
            mApiUrl = delegate.getApiUrl();
            mAccessToken = (null != delegate.getAccessToken()) ? delegate.getAccessToken() : "";
            mDeviceToken = (null != delegate.getDeviceToken()) ? delegate.getDeviceToken() : "";
            mApiDataDelegate = delegate.getApiDataDelegate();
        }
    }
}