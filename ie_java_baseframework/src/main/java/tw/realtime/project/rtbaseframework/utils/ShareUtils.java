package tw.realtime.project.rtbaseframework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import tw.realtime.project.rtbaseframework.LogWrapper;

public class ShareUtils {

    public interface AppPackageName {
        String LINE = "jp.naver.line.android";
        String FACEBOOK_MESSENGER = "com.facebook.orca";
    }

    public static boolean hasPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            LogWrapper.showLog(Log.INFO, "ShareUtils", "hasPackageInstalled", e);
            return false;
        }
    }

    public static void shareText(@NonNull Context context,
                                 @NonNull Activity launchingActivity,
                                 @NonNull String shareTitle,
                                 @NonNull String shareContent) {
        final Intent shareIntent = ShareCompat.IntentBuilder.from(launchingActivity)
                .setType("text/plain")
                .setText(shareContent)
                .getIntent();
        if ( (null != context.getPackageManager()) &&
                (null != shareIntent.resolveActivity(context.getPackageManager())) ) {
            //launchingActivity.startActivity(shareIntent);
            launchingActivity.startActivity(Intent.createChooser(shareIntent, shareTitle));
        }
    }

    public static void shareToAppByPackageName(@NonNull Activity activity,
                                               @NonNull String message,
                                               @NonNull String packageName) {
        // work
        try {
            final Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage(packageName);
            activity.startActivity(sendIntent);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.WARN, "ShareUtils", "Exception shareToAppByPackageName", e);
        }
    }

}
