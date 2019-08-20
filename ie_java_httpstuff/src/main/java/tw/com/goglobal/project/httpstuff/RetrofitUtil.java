package tw.com.goglobal.project.httpstuff;


import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import tw.com.goglobal.project.baseframework.instances.factories.ParameterFactory;


public final class RetrofitUtil {

    private volatile static Retrofit instance;

    public static class RetrofitFactory implements ParameterFactory<Retrofit, Retrofit.Builder> {
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
