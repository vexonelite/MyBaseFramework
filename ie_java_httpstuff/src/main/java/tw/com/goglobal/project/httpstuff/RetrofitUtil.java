package tw.com.goglobal.project.httpstuff;


import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import tw.realtime.project.rtbaseframework.factories.ParameterFactoryDelegate;


public final class RetrofitUtil {

    private volatile static Retrofit instance;

    public static final class RetrofitFactory implements ParameterFactoryDelegate<Retrofit.Builder, Retrofit> {
        @NonNull
        @Override
        public Retrofit create(@NonNull Retrofit.Builder parameter) {
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
    public Retrofit.Builder defaultRetrofitBuilder(
            @NonNull String baseApiUrl, boolean enableHttpLoggingInterceptor) {

        final OkHttpClient.Builder okHttpBuilder =
                new OkHttpUtil().defaultOkHttpClientBuilder(enableHttpLoggingInterceptor);

        return new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(new OkHttpUtil.ClientFactory().create(okHttpBuilder));
    }

    /** create a Retrofit 2 service representing a asynchronized api task.   */
    @NonNull
    public <S> S createService(@NonNull Retrofit.Builder builder, Class<S> serviceClass) {
        return new RetrofitFactory()
                .create(builder)
                .create(serviceClass);
    }

}
