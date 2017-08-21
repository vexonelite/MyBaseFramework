package tw.realtime.project.rtbaseframework.retrofits;

//import org.simpleframework.xml.Serializer;
//import org.simpleframework.xml.convert.AnnotationStrategy;
//import org.simpleframework.xml.core.Persister;
//import org.simpleframework.xml.strategy.Strategy;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

//import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 2 service generator
 * <p></p>
 * Created by vexonelite on 2016/12/29.
 */
public class ServiceGenerator {

    public static final long CONNECTION_TIME = 60000;
    public static final long READ_TIMEOUT = 120000;
    public static final long WRITE_TIMEOUT = 120000;

    private volatile static Retrofit instance;

    /** Returns singleton class instance */
    private static Retrofit getInstance(RetrofitParameter parameter) {
        if (instance == null) {
            synchronized (Retrofit.class) {
                if (instance == null) {
                    instance = getRetrofitInstance(parameter);
                }
            }
        }
        return instance;
    }

    private static Retrofit getRetrofitInstance (RetrofitParameter parameter) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(parameter.baseApiUrl)
                .client(parameter.okBuilder.build());

        if (parameter.enableGsonConver) {
            //builder.addConverterFactory(GsonConverterFactory.create());
        }

        if (parameter.enableRxJavaCallAdapter) {
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }

//        if (parameter.enableSimpleXmlConverter) {
//            Strategy strategy = new AnnotationStrategy();
//            Serializer serializer = new Persister(strategy);
//            builder.addConverterFactory(SimpleXmlConverterFactory.create(serializer));
//            //builder.addConverterFactory(XStreamXmlConverterFactory.create(new XStream(new DomDriver())));
//        }

        return builder.build();
    }


    /** Retrofit 2 parameter */
    public static class RetrofitParameter {
        private boolean enableGsonConver;
        private boolean enableRxJavaCallAdapter;
        private boolean enableSimpleXmlConverter;
        private String baseApiUrl;
        private OkHttpClient.Builder okBuilder;

        /** Enable Gson Converter */
        public RetrofitParameter setEnableGsonConverFlag (boolean flag) {
            enableGsonConver = flag;
            return this;
        }

        /** Enable RxJava Call Adapter */
        public RetrofitParameter setEnableRxJavaCallAdapterFlag (boolean flag) {
            enableRxJavaCallAdapter = flag;
            return this;
        }

        /** Enable Simple Xml Converter */
//        public RetrofitParameter setEnableSimpleXmlConverterFlag (boolean flag) {
//            enableSimpleXmlConverter = flag;
//            return this;
//        }

        /** set common api prefix url */
        public RetrofitParameter setBaseApiUrl (String url) {
            baseApiUrl = url;
            return this;
        }

        /** set OkHttpClient Builder */
        public RetrofitParameter setOkHttpClientBuilder (OkHttpClient.Builder builder) {
            okBuilder = builder;
            return this;
        }
    }

    /** Return a default OkHttpClient builder */
    public static OkHttpClient.Builder getOkHttpClientBuilder () {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .followRedirects(true)
                .followSslRedirects(true);
    }

    /** create a Retrofit 2 service representing a asynchronized api task.  */
    public static <S> S createService(Class<S> serviceClass, RetrofitParameter parameter) {
        return getInstance(parameter).create(serviceClass);
    }
}

