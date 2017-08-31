package tw.realtime.project.rtbaseframework.api.commons;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * 取得 OkHttpClient 的工具物件，且可以產生 OkHttp 的 Request Call
 */
public class OkHttpUtils {

    private volatile static OkHttpClient instance;

    /**
     * 取得 OkHttpClient 實體
     */
    public static OkHttpClient getInstance(boolean enableHttpLoggingInterceptor) {
        if (instance == null) {
            synchronized (OkHttpClient.class) {
                if (instance == null) {
                    instance = getOkHttpClient(enableHttpLoggingInterceptor);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化一個 OkHttpClient
     * @return
     */
    private static OkHttpClient getOkHttpClient (boolean enableHttpLoggingInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(ApiConstants.OkHttpSetting.CONNECTION_TIME, TimeUnit.MILLISECONDS)
                .readTimeout(ApiConstants.OkHttpSetting.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(ApiConstants.OkHttpSetting.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .followRedirects(true)
                .followSslRedirects(true);

        if (enableHttpLoggingInterceptor) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    /*
    private static MultipartBody.Builder getMultipartBodyBuilder (String outString) {
        MultipartBody.Builder multiPartBuilder = new MultipartBody.Builder();
        multiPartBuilder.setType(MultipartBody.FORM);
        if ( (null != outString) && (!outString.isEmpty()) ) {
            multiPartBuilder.addFormDataPart(Constants.RtApiUsageKey.DATA, outString);
            LogWrapper.showLog(Log.INFO, "okHttpUtils", "getMultipartBodyBuilder - (" + Constants.RtApiUsageKey.DATA + ", " + outString + ")");
        }
        return multiPartBuilder;
    }
    */

    /**
     * 產生 OkHttp Request Call
     * @param requestBody   Request Body
     * @param apiUrl        Request 的網址
     * @return
     */
    public static okhttp3.Call generateRequestCall (RequestBody requestBody,
                                                    String apiUrl,
                                                    boolean enableHttpLoggingInterceptor) {
        Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(apiUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        LogWrapper.showLog(Log.INFO, "okHttpUtils", "generateRequestCall - apiUrl: " + apiUrl);
        return getInstance(enableHttpLoggingInterceptor).newCall(request);
    }

    public static FormBody.Builder getFormBodyBuilder (String key, String outString) {
        FormBody.Builder formBodyBuilder = new okhttp3.FormBody.Builder();
        if ( (null != key) && (!key.isEmpty()) && (null != outString) && (!outString.isEmpty()) ) {
            formBodyBuilder.add(key, outString);
            LogWrapper.showLog(Log.INFO, "okHttpUtils", "getFormBodyBuilder - (" + key + ", " + outString + ")");
        }
        return formBodyBuilder;
    }

    public static MultipartBody.Builder getMultipartBodyBuilder (String key, String outString) {
        MultipartBody.Builder multiPartBuilder = new MultipartBody.Builder();
        multiPartBuilder.setType(MultipartBody.FORM);
        if ( (null != key) && (!key.isEmpty()) && (null != outString) && (!outString.isEmpty()) ) {
            multiPartBuilder.addFormDataPart(key, outString);
            LogWrapper.showLog(Log.INFO, "okHttpUtils", "getMultipartBodyBuilder - (" + key + ", " + outString + ")");
        }
        return multiPartBuilder;
    }


}
