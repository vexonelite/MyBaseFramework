package tw.com.goglobal.project.httpstuff.callables;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;

import okhttp3.Response;
import tw.realtime.project.rtbaseframework.parameters.ApiDataDelegate;
import tw.realtime.project.rtbaseframework.parameters.ApiParameterDelegate;
import tw.realtime.project.rtbaseframework.parameters.ApiParameterSetDelegate;


public abstract class BaseApiRequestCallable implements ApiParameterDelegate, Callable<Response> {

    private boolean mAesEncodingEnable = false;
    private boolean mHttpLogEnable = true;
    private String mApiUrl;
    private String mAccessToken;
    private String mDeviceToken;
    private String mAesKey;
    private String mAesIv;
    private ApiDataDelegate mApiDataDelegate;


    @Override
    public boolean doesEnableAesEncoding () {
        return mAesEncodingEnable;
    }

    @Override
    public boolean doesEnableHttpLog() {
        return mHttpLogEnable;
    }

    @NonNull
    @Override
    public String getApiUrl() {
        return mApiUrl;
    }

    @NonNull
    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    @NonNull
    @Override
    public String getDeviceToken() {
        return mDeviceToken;
    }

    @NonNull
    @Override
    public String getAesKey() {
        return mAesKey;
    }

    @NonNull
    @Override
    public String getAesIv() {
        return mAesIv;
    }

    @NonNull
    @Override
    public ApiDataDelegate getApiDataDelegate() {
        return mApiDataDelegate;
    }


    protected BaseApiRequestCallable(@NonNull ApiParameterSetDelegate delegate) {
        mAesEncodingEnable = delegate.doesEnableAesEncoding();
        mHttpLogEnable = delegate.doesEnableHttpLog();
        mApiUrl = delegate.getApiUrl();
        mAccessToken = delegate.getAccessToken();
        mDeviceToken = delegate.getDeviceToken();
        mAesKey = delegate.getAesKey();
        mAesIv = delegate.getAesIv();
        mApiDataDelegate = delegate.getApiDataDelegate();
    }
}