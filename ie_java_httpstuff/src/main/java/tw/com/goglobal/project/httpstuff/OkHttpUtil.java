package tw.com.goglobal.project.httpstuff;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.factories.ParameterFactoryDelegate;


/**
 * 取得 OkHttpClient 的工具物件，且可以產生 OkHttp 的 Request Call
 * <p>
 * revision on 2018/10/10
 */
public final class OkHttpUtil {

    private volatile static OkHttpClient instance;

    public static final class ClientFactory implements ParameterFactoryDelegate<OkHttpClient.Builder, OkHttpClient> {
        @NonNull
        @Override
        public OkHttpClient create(@NonNull OkHttpClient.Builder parameter) {
            if (instance == null) {
                synchronized (OkHttpClient.class) {
                    if (instance == null) {
                        instance = parameter.build();
                    }
                }
            }
            return instance;
        }
    }

    @NonNull
    public OkHttpClient.Builder defaultOkHttpClientBuilder(boolean enableHttpLoggingInterceptor) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(OkHttpSetting.CONNECTION_TIME, TimeUnit.MILLISECONDS)
                .readTimeout(OkHttpSetting.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(OkHttpSetting.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .followRedirects(true)
                .followSslRedirects(true);

        if (enableHttpLoggingInterceptor) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder;
    }

    /**
     * 產生 OkHttp Request Call
     * @param requestBody   Request Body
     * @param apiUrl        Request 的網址
     * @return
     */
    @NonNull
    public okhttp3.Call generateRequestCall(
            @NonNull RequestBody requestBody,
            @NonNull String apiUrl,
            boolean enableHttpLoggingInterceptor
    ) {
        final Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(apiUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        LogWrapper.showLog(Log.INFO, "okHttpUtils", "generateRequestCall - apiUrl: " + apiUrl);

        final OkHttpClient.Builder okHttpBuilder = defaultOkHttpClientBuilder(enableHttpLoggingInterceptor);
        return new ClientFactory().create(okHttpBuilder).newCall(request);
    }

    @NonNull
    public FormBody.Builder getFormBodyBuilder(@NonNull String key, @NonNull String outString) {
        final FormBody.Builder formBodyBuilder = new okhttp3.FormBody.Builder();
        if ( (!key.isEmpty()) && (!outString.isEmpty()) ) {
            formBodyBuilder.add(key, outString);
            LogWrapper.showLog(Log.INFO, "okHttpUtils", "getFormBodyBuilder - (" + key + ", " + outString + ")");
        }
        return formBodyBuilder;
    }

    @NonNull
    public MultipartBody.Builder getMultipartBodyBuilder(@NonNull String key, @NonNull String outString) {
        final MultipartBody.Builder multiPartBuilder = new MultipartBody.Builder();
        multiPartBuilder.setType(MultipartBody.FORM);
        //if ( (null != key) && (!key.isEmpty()) && (null != outString) && (!outString.isEmpty()) ) {
        if ((!key.isEmpty())&& (!outString.isEmpty()) ) {
            multiPartBuilder.addFormDataPart(key, outString);
            LogWrapper.showLog(Log.INFO, "okHttpUtils", "getMultipartBodyBuilder - (" + key + ", " + outString + ")");
        }
        return multiPartBuilder;
    }

    @NonNull
    public okhttp3.Call generateHttpGetRequestCall(
            @NonNull String apiUrl,
            boolean enableHttpLoggingInterceptor,
            boolean doesAddContentTypeForJson
    ) {
        final Request.Builder builder = new Request.Builder()
                .url(apiUrl);
        if (doesAddContentTypeForJson) {
            builder.addHeader("Content-Type", "application/json");
        }

        LogWrapper.showLog(Log.INFO, "okHttpUtils", "generateHttpGetRequestCall - apiUrl: " + apiUrl);

        final OkHttpClient.Builder okHttpBuilder = defaultOkHttpClientBuilder(enableHttpLoggingInterceptor);
        return new ClientFactory().create(okHttpBuilder).newCall(builder.build());
    }

    @Nullable
    public MediaType getJsonMediaType() { return MediaType.parse("application/json; charset=utf-8"); }

    @NonNull
    public MediaType getMultiFormPartMediaType() { return MultipartBody.FORM; }

    @NonNull
    public RequestBody getRequestBody(@NonNull MediaType mediaType, @NonNull String value) {
        return RequestBody.create(mediaType, value);
    }

    @NonNull
    public MultipartBody.Part getMultipartBodyFromFile(
            @NonNull File file, @NonNull String parameterName) {
        final RequestBody fileRequestBody = RequestBody.create(null, file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(parameterName, file.getName(), fileRequestBody);
    }
}



