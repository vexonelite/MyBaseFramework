package tw.realtime.project.rtbaseframework.app;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.R;


public class WebviewFragment extends BaseFragment {

    private SwipeRefreshLayout mSwipeRefresh;
    private WebView mWebView;


    private boolean mSwipeRefreshEnabledFlag = true;
    private boolean mHomeButtonEnabledFlag = false;
    private boolean isLocalContent = false;
    private boolean mDefaultWebViewClientEnabledFlag = false;
    private boolean mAutoDismissProgressDialogEnabledFlag = false;
    private boolean isWebViewLoading = false;

    private long mAutoDismissProgressDialogSecond = 10L;

    private String mCustomizedUserAgent;
    private String mTargetUrl = "";
    private String mActionBarTitle = "";

    private int mSwipeRefreshColorSchemeResourceId = android.R.color.holo_blue_light;

    //@Deprecated
    //private WebViewClient mWebViewClient;


    public WebviewFragment setDefaultWebViewClientFlag (boolean flag) {
        mDefaultWebViewClientEnabledFlag = flag;
        return this;
    }

    public WebviewFragment setEnableLocalContentFlag (boolean flag) {
        isLocalContent = flag;
        return this;
    }

    public WebviewFragment setHomeButtonEnabledFlag (boolean flag) {
        mHomeButtonEnabledFlag = flag;
        return this;
    }

    public WebviewFragment setSwipeRefreshEnabledFlag (boolean flag) {
        mSwipeRefreshEnabledFlag = flag;
        return this;
    }

    public WebviewFragment setAutoDismissProgressDialogEnabledFlag (boolean flag) {
        mAutoDismissProgressDialogEnabledFlag = flag;
        return this;
    }

    public WebviewFragment setTargetUrl (@NonNull String url) {
        mTargetUrl = url;
        return this;
    }

    public WebviewFragment setActionBarTitle (@NonNull String actionBarTitle) {
        mActionBarTitle = actionBarTitle;
        return this;
    }

    public WebviewFragment setCustomizedUserAgent (String userAgent) {
        mCustomizedUserAgent = userAgent;
        return this;
    }

//    @Deprecated
//    public WebviewFragment setCustomizedWebViewClient (WebViewClient webViewClient) {
//        mWebViewClient = webViewClient;
//        return this;
//    }

    public WebviewFragment setSwipeRefreshColorSchemeResourceId (int resourceId) {
        mSwipeRefreshColorSchemeResourceId = resourceId;
        return this;
    }

    public WebviewFragment setAutoDismissProgressDialogSecond (long second) {
        if (second > 0) {
            mAutoDismissProgressDialogSecond = second;
        }
        return this;
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_refreash_webview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        setupSwipeRefreshLayout( (SwipeRefreshLayout) rootView);
        setupWebView(rootView.findViewById(R.id.webView));
        webViewLoadInitialContent();
    }

    @Override
    public void onResume () {
        super.onResume();
        if (null != mActionBarTitle) {
            setUpActionBar(mActionBarTitle, mHomeButtonEnabledFlag, true);
        }
    }

    private void setupSwipeRefreshLayout (@NonNull SwipeRefreshLayout srLayout) {
        mSwipeRefresh = srLayout;
        mSwipeRefresh.setColorSchemeResources(mSwipeRefreshColorSchemeResourceId);
        mSwipeRefresh.setOnRefreshListener(this::swipeRefreshOnRefreshCallback);
        mSwipeRefresh.setEnabled(mSwipeRefreshEnabledFlag);
    }

    /** implements SwipeRefreshLayout.OnRefreshListener */
    private void swipeRefreshOnRefreshCallback ()  {
        if (!mSwipeRefresh.canChildScrollUp() ) {
            onRefreshHandler();
        }
    }

    private void onRefreshHandler() {
        if (null == mWebView) {
            return;
        }
        if (!isWebViewLoading) {
            mWebView.reload();
        }
    }

    private void resetSwipeRefresh () {
        if ((null != mSwipeRefresh) && (mSwipeRefresh.isRefreshing() ) ) {
            mSwipeRefresh.setRefreshing(false);
        }
        isWebViewLoading = false;
        mWebView.setEnabled(true);
    }

    // Ref:
    //    http://stackoverflow.com/questions/6199717/how-can-i-know-that-my-webview-is-loaded-100
    //    http://stackoverflow.com/questions/5049616/android-webviewclient-callbacks-called-too-often
    private void setupWebView (@NonNull WebView webView) {
        mWebView = webView;
        final WebSettings settings = mWebView.getSettings();
        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);
        // Hide the zoom controls for HONEYCOMB+
        settings.setDisplayZoomControls(false);

        //settings.setSupportZoom(true);
        //settings.setSaveFormData(true);
        //settings.setLoadsImagesAutomatically(true);
        //settings.setBlockNetworkImage(false);
        //settings.setDefaultTextEncodingName("UTF-8");
        //settings.setPluginState(PluginState.ON);

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Enable Javascript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        onAddJavascriptInterface(mWebView);
//        }

//        if (null != mWebViewClient) {
//            mWebView.setWebViewClient(mWebViewClient);
//        }
//        else
        if (mDefaultWebViewClientEnabledFlag) {
            mWebView.setWebViewClient(new DefaultWebViewClient());
        }
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        // retrieve the user agent String
        final String userAgentStr = settings.getUserAgentString();
        if (null != mCustomizedUserAgent) {
            // append the customized user agent to the tail of user agent String,
            // then update the user agent to the setting of WebView
            mWebView.getSettings().setUserAgentString(userAgentStr + mCustomizedUserAgent);
        }
        //MyLog.d(getLogTag(), "setupWebView - userAgentStr [post]: " + settings.getUserAgentString());

        clearAllCookies();
    }

    protected void onAddJavascriptInterface (@NonNull WebView webView) {
//        if ( (null != mJavaScriptBridge) && (null != mJavaScriptBridgeName)
//                && (!mJavaScriptBridgeName.isEmpty()) ) {
//            mWebView.addJavascriptInterface(mJavaScriptBridge, mJavaScriptBridgeName);
//        }
    }

    /**
     * 清除 Webview 的 Cookies
     */
    private void clearAllCookies () {
        //remove cookie before loadUrl

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //MyLog.d(getLogTag(), "Using clearAllCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }
        else {
            //MyLog.d(getLogTag(), "Using clearAllCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
            final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
            cookieSyncManager.startSync();
            final CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }

    /**
     * 接受網頁來的 Cookies
     */
    private void acceptCookie () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //MyLog.d(getLogTag(), "Using acceptCookie code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
            CookieManager.getInstance().setAcceptCookie(true);
            //CookieManager.getInstance().setCookie(mTargetUrl, "JSESSIONID=" + mySampleSessionId);
        }
        else {
            //MyLog.d(getLogTag(), "Using acceptCookie code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
            final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
            cookieSyncManager.startSync();
            final CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }

    /**
     * 實作自訂的 WebViewClient 介面
     */
    private class DefaultWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onPageStarted: " + url);
            isWebViewLoading = true;
            mWebView.setEnabled(false);
            showProgressDialog(getString(R.string.base_data_been_loading));

            if (mAutoDismissProgressDialogEnabledFlag) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetSwipeRefresh();
                        dismissProgressDialog();
                    }
                }, (mAutoDismissProgressDialogSecond * 1000L));
            }
        }

        // As the host application if the browser should resend data as the requested page was a result of a POST.
        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onFormResubmission - dontResend: " + dontResend + ", resend: " + resend);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "shouldOverrideUrlLoading (webView, url): " + url);
            /*
            if (Uri.parse(url).getHost().equals(mEntryUrl) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            */
            webView.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "shouldOverrideUrlLoading (webView, request): " + request);
//            if (Build.VERSION.SDK_INT >= 24) {
//                if ( (null != request) && (null != request.getUrl()) ) {
//                    //final Uri uri = Uri.parse(request.getUrl() );
//                    webView.loadUrl(request.getUrl().toString());
//                }
//            }
//            return true;

            if ( (null != request) && (null != request.getUrl()) ) {
                //final Uri uri = Uri.parse(request.getUrl() );
                //webView.loadUrl(request.getUrl().toString());
                return shouldOverrideUrlLoading(webView, request.getUrl().toString() );
            }
            else {
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onPageFinished: " + url);
            resetSwipeRefresh();
            dismissProgressDialog();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onReceivedError - url: "
                    + failingUrl + ", errorCode: " + errorCode + ", error message: " + description);
            resetSwipeRefresh();
            dismissProgressDialog();
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView webView, WebResourceRequest request, WebResourceError error) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onReceivedError (new)");
            // Redirect to deprecated method, so you can use it in all SDK versions

            String errorDescription = "";
            String url = "";
            if ( (null != request) && (null != error) ) {
                if (null != request.getUrl()) {
                    url = url + request.getUrl().toString();
                }
                int errorCode = error.getErrorCode();
                if (null != error.getDescription()) {
                    errorDescription = errorDescription + error.getDescription();
                }
                onReceivedError(webView, errorCode, errorDescription, url);
            }
            else {
                resetSwipeRefresh();
                dismissProgressDialog();
            }
        }
    }

    /**
     * 點擊回上一頁時的後續動作
     */
    private void onGoBackButtonClicked () {
        if (null == mWebView) {
            return;
        }
        if (mWebView.canGoBack() ) {
            mWebView.goBack();
        }
    }

    /**
     * 點擊回下一頁時的後續動作
     */
    private void onGoForwardButtonClicked () {
        if (null == mWebView) {
            return;
        }
        if (mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    /**
     * 點擊消失 (xx 圖示) 時的後續動作
     */
    private void onCancelButtonClicked () {
        if (null != mWebView) {
            if (isWebViewLoading) {
                mWebView.stopLoading();
            }
        }
    }

    private void webViewLoadInitialContent () {
        String url = "";
        if (isLocalContent) {
            if ( (null != mTargetUrl) && (!mTargetUrl.isEmpty()) ) {
                url = url + mTargetUrl;
            }
            mWebView.loadData(url, "text/html; charset=utf-8", "UTF-8");
        }
        else {
            String prefix = "https://www.google.com/search?q=";
            if ( (null != mTargetUrl) && (!mTargetUrl.isEmpty()) ) {
                if ( (!mTargetUrl.startsWith("http://")) &&
                        (!mTargetUrl.startsWith("https://")) ) {
                    url = url + prefix + mTargetUrl;
                }
                else {
                    url = url + mTargetUrl;
                }
            }

            if (null != mWebView) {
                mWebView.loadUrl(url);
            }
        }
    }
}













