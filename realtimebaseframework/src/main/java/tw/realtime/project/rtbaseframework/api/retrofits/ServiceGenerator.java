package tw.realtime.project.rtbaseframework.api.retrofits;

import java.io.File;

import androidx.annotation.NonNull;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import tw.realtime.project.rtbaseframework.api.commons.OkHttpUtils;

//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 2 service generator
 * <p></p>
 * Created by vexonelite on 2016/12/29.
 * revision on 2018/10/10
 */
public class ServiceGenerator {

    private volatile static Retrofit instance;

    /** Returns singleton class instance */
    private static Retrofit getInstance(@NonNull Retrofit.Builder builder) {
        if (instance == null) {
            synchronized (Retrofit.class) {
                if (instance == null) {
                    instance = builder.build();
                }
            }
        }
        return instance;
    }

    public static Retrofit.Builder getBasicRetrofitBuilder (@NonNull String baseApiUrl,
                                                            boolean enableHttpLoggingInterceptor) {
        return new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(OkHttpUtils.getInstance(enableHttpLoggingInterceptor));

//        if (enableGsonConver) {
//            builder.addConverterFactory(GsonConverterFactory.create());
//        }
//        if (enableRxJavaCallAdapter) {
//            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
//        }
//        if (enableSimpleXmlConverter) {
//            final Strategy strategy = new AnnotationStrategy();
//            final Serializer serializer = new Persister(strategy);
//            builder.addConverterFactory(SimpleXmlConverterFactory.create(serializer));
//            //builder.addConverterFactory(XStreamXmlConverterFactory.create(new XStream(new DomDriver())));
//        }
    }

    /** create a Retrofit 2 service representing a asynchronized api task.  */
    @NonNull
    public static <S> S createService(@NonNull Retrofit.Builder builder, Class<S> serviceClass) {
        return getInstance(builder).create(serviceClass);
    }

    @NonNull
    public static RequestBody getStringRequestBody(@NonNull String value) {
        return RequestBody.create(MultipartBody.FORM, value);
    }

    @NonNull
    public static MultipartBody.Part getMultipartBodyFromFile(@NonNull File file,
                                                              @NonNull String parameterName) {
        final RequestBody fileRequestBody = RequestBody.create(null, file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(parameterName, file.getName(), fileRequestBody);
    }
}
