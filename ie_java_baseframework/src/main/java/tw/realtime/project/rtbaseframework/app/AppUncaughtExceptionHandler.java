package tw.realtime.project.rtbaseframework.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


public final class AppUncaughtExceptionHandler<T> implements Thread.UncaughtExceptionHandler {

    private final Activity activity;
    private final Class<T> activityClass;

    public AppUncaughtExceptionHandler(@NonNull final Activity activity,
                                       @NonNull final Class<T> activityClass) {
        this.activity = activity;
        this.activityClass = activityClass;
    }

    @Override public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        final Intent intent = new Intent(activity, activityClass);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                // [start] revision by elite_lin - 2021/11/03
                //activity.getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                activity.getBaseContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                // [end] revision by elite_lin - 2021/11/03
        final AlarmManager mgr = (AlarmManager) activity.getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }
}
