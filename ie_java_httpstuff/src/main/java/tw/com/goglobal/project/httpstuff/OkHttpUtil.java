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

    public static final class ClientFactory {
        @NonNull
        public OkHttpClient create(@NonNull final OkHttpClient.Builder parameter) {
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
    public static OkHttpClient.Builder defaultOkHttpClientBuilder(final boolean enableHttpLoggingInterceptor) {
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
    public static okhttp3.Call generateRequestCall(
            @NonNull final RequestBody requestBody,
            @NonNull final String apiUrl,
            final boolean enableHttpLoggingInterceptor
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
    public static FormBody.Builder getFormBodyBuilder(
            @NonNull final String key, @NonNull final String outString) {
        final FormBody.Builder formBodyBuilder = new okhttp3.FormBody.Builder();
        if ( (!key.isEmpty()) && (!outString.isEmpty()) ) {
            formBodyBuilder.add(key, outString);
            LogWrapper.showLog(Log.INFO, "okHttpUtils", "getFormBodyBuilder - (" + key + ", " + outString + ")");
        }
        return formBodyBuilder;
    }

    @NonNull
    public static MultipartBody.Builder getMultipartBodyBuilder(
            @NonNull final String key, @NonNull final String outString) {
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
    public static okhttp3.Call generateHttpGetRequestCall(
            @NonNull final String apiUrl,
            final boolean enableHttpLoggingInterceptor,
            final boolean doesAddContentTypeForJson
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
    public static MediaType getJsonMediaType() { return MediaType.parse("application/json; charset=utf-8"); }

    @NonNull
    public static MediaType getMultiFormPartMediaType() { return MultipartBody.FORM; }

    @NonNull
    public static RequestBody getRequestBody(
            @NonNull final MediaType mediaType, @NonNull final String value) {
        return RequestBody.create(mediaType, value);
    }

    @NonNull
    public static MultipartBody.Part getMultipartBodyFromFile(
            @NonNull final File file, @NonNull final String parameterName) {
        final RequestBody fileRequestBody = RequestBody.create(null, file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(parameterName, file.getName(), fileRequestBody);
    }
}



