package tw.com.goglobal.project.httpstuff;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.utils.CodeUtils;


public final class IeHttpUserAgentInterceptor implements Interceptor {

    private final String userAgent;

    public IeHttpUserAgentInterceptor(@NonNull final String userAgent) { this.userAgent = userAgent; }

    @NonNull
    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        builder.header("User-Agent", userAgent);
        return chain.proceed(builder.build());
    }

    @NonNull
    public static String getDefaultHttpUserAgent() {
        final String userAgent = System.getProperty("http.agent");
        LogWrapper.showLog(Log.ERROR, "IeHttpUserAgentInterceptor", "getDefaultHttpUserAgent: " + ((null != userAgent) ? userAgent : ""));
        return (null != userAgent) ? userAgent : "";
    }

    @NonNull
    public static String buildUserAgent(@NonNull final String appName, @NonNull final String versionName) {
//            final String x = String.format(Locale.US,
//                    "%s/%s (Android %s; %s; %s %s; %s)",
//                    "Kessil WiFi",
//                    "1.3.7",
//                    getUserAgentOsVersion(Build.VERSION.RELEASE),
//                    Build.MODEL,
//                    Build.BRAND,
//                    Build.DEVICE,
//                    Locale.getDefault().getLanguage());
        final String userAgent = String.format(Locale.US,
                //"%s/%s (Linux; Android %s; %s %s Build/%s; %s)",
                "%s/%s (Android %s; %s %s Build/%s; %s)",
                appName,
                versionName,
                getUserAgentOsVersion(Build.VERSION.RELEASE),
                CodeUtils.replaceAllSpaceWithUnderscore(Build.MODEL.toUpperCase(Locale.US)),
                Build.BRAND.toUpperCase(Locale.US),
                Build.MODEL,
                getUserAngerBuildDisplay(Build.DISPLAY),
                Build.DEVICE);
        LogWrapper.showLog(Log.ERROR, "IeHttpUserAgentInterceptor", "buildUserAgent: " + userAgent);
        return userAgent;
    }

    @NonNull
    public static String getUserAgentOsVersion(@Nullable final String osVersion) {
        if ( (null == osVersion) || (osVersion.isEmpty())) { return ""; }
        LogWrapper.showLog(Log.INFO, "IeHttpUserAgentInterceptor", "getUserAgentOsVersion - osVersion: " + osVersion);
        final String[] osVersionArray = osVersion.split("\\.");
        final int osVersionArraySize = osVersionArray.length;
        if (osVersionArraySize == 0) { return osVersion; }
        for (final String digit: osVersionArray) {
            LogWrapper.showLog(Log.INFO, "IeHttpUserAgentInterceptor", "getUserAgentOsVersion - digit: " + digit);
        }
        return (osVersionArraySize > 1) ? osVersion : osVersionArray[0] + ".0";
    }

    @NonNull
    public static String getUserAngerBuildDisplay(@Nullable final String given) {
        if ( (null == given) || (given.isEmpty())) { return ""; }
        final String temp1 = given.replaceAll("\\s+", "");
        return temp1.replaceAll("release-keys", "");
    }

}