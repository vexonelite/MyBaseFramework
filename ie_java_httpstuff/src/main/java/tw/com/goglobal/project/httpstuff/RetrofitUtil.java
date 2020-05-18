package tw.com.goglobal.project.httpstuff;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import tw.realtime.project.rtbaseframework.LogWrapper;


public final class RetrofitUtil {

    private volatile static Retrofit instance;

    public static final class RetrofitFactory {
        @NonNull
        public Retrofit create(@NonNull final Retrofit.Builder parameter) {
            if (instance == null) {
                synchronized (Retrofit.class) {
                    if (instance == null) {
                        instance = parameter.build();
                        LogWrapper.showLog(Log.INFO, "RetrofitUtil", "create - create new one");
                    }
                    else { LogWrapper.showLog(Log.INFO, "RetrofitUtil", "create - existing"); }
                }
            }
            return instance;
        }
    }

    @NonNull
    public static Retrofit.Builder defaultRetrofitBuilder(@NonNull final String baseApiUrl) {
        return defaultRetrofitBuilder(baseApiUrl, true, new ArrayList<>());
    }

    @NonNull
    public static Retrofit.Builder defaultRetrofitBuilder(
            @NonNull final String baseApiUrl,
            final boolean enableHttpLoggingInterceptor,
            @NonNull final List<Interceptor> interceptors) {
        final OkHttpClient.Builder okHttpBuilder = OkHttpUtil.defaultOkHttpClientBuilder();
        if (enableHttpLoggingInterceptor) { OkHttpUtil.addHttpLoggingInterceptor(okHttpBuilder); }
        OkHttpUtil.addNetworkInterceptors(okHttpBuilder, interceptors);
        return new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(new OkHttpUtil.ClientFactory().create(okHttpBuilder));
    }

    /** create a Retrofit 2 service representing a asynchronized api task.   */
    @NonNull
    public static <S> S createService(
            @NonNull final Retrofit.Builder builder, @NonNull final Class<S> serviceClass) {
        return new RetrofitFactory()
                .create(builder)
                .create(serviceClass);
    }

}
