package tw.com.goglobal.project.httpstuff;


import androidx.annotation.NonNull;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import tw.realtime.project.rtbaseframework.factories.ParameterFactoryDelegate;


public final class RetrofitUtil {

    private volatile static Retrofit instance;

    public static final class RetrofitFactory {
        @NonNull
        public Retrofit create(@NonNull final Retrofit.Builder parameter) {
            if (instance == null) {
                synchronized (Retrofit.class) {
                    if (instance == null) {
                        instance = parameter.build();
                    }
                }
            }
            return instance;
        }
    }

    @NonNull
    public static Retrofit.Builder defaultRetrofitBuilder(
            @NonNull final String baseApiUrl, final boolean enableHttpLoggingInterceptor) {

        final OkHttpClient.Builder okHttpBuilder =
                OkHttpUtil.defaultOkHttpClientBuilder(enableHttpLoggingInterceptor);

        return new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(new OkHttpUtil.ClientFactory().create(okHttpBuilder));
    }

    @NonNull
    private static Retrofit.Builder defaultRetrofitBuilder(
            @NonNull final String baseApiUrl,
            final boolean enableHttpLoggingInterceptor,
            @NonNull final List<Interceptor> interceptors) {

        final OkHttpClient.Builder okHttpBuilder =
                OkHttpUtil.defaultOkHttpClientBuilder(enableHttpLoggingInterceptor);
        for (final Interceptor theInterceptor : interceptors) {
            okHttpBuilder.addInterceptor(theInterceptor);
        }

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
