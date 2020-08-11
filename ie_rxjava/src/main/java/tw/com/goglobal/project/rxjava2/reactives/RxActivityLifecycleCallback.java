package tw.com.goglobal.project.rxjava2.reactives;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tw.com.goglobal.project.rxjava2.RxEventBus;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.enumerations.LifeCycleState;


public final class RxActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    public static final class Wrapper {
        public final int activityDisplayCountPre;
        public final int activityDisplayCountPost;
        public final LifeCycleState lifeCycleState;

        private Wrapper(final int activityDisplayCountPre,
                        final int activityDisplayCountPost,
                        @NonNull final LifeCycleState lifeCycleState) {
            this.activityDisplayCountPre = activityDisplayCountPre;
            this.activityDisplayCountPost = activityDisplayCountPost;
            this.lifeCycleState = lifeCycleState;
        }

        @Override
        public String toString() {
            return "Wrapper { Pre: " + activityDisplayCountPre + ", Post: " + activityDisplayCountPost + ", LifeCycleState: " + lifeCycleState + " }";
        }
    }

    private final RxEventBus eventBus;

    private int activityDisplayCount = 0;

    public RxActivityLifecycleCallback(@NonNull final RxEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityCreated: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        final int activityDisplayCountPre = activityDisplayCount;
        if (activityDisplayCount == 0) {
            LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityStarted - activityDisplayCount = 0 --> Go to foreground!");
        }
        activityDisplayCount++;
        final int activityDisplayCountPost = activityDisplayCount;
        eventBus.post(new Wrapper(activityDisplayCountPre, activityDisplayCountPost, LifeCycleState.ON_START));
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityStopped - activityDisplayCount: " + activityDisplayCount);
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityStarted: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityResumed: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityPaused: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @Nullable Bundle outState) {
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivitySaveInstanceState: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        final int activityDisplayCountPre = activityDisplayCount;
        if (activityDisplayCount > 0) {
            activityDisplayCount--;
        }
        final int activityDisplayCountPost = activityDisplayCount;
        eventBus.post(new Wrapper(activityDisplayCountPre, activityDisplayCountPost, LifeCycleState.ON_STOP));
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityStopped - activityDisplayCount: " + activityDisplayCount);
        if (activityDisplayCount == 0) {
            LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityStopped - activityDisplayCount = 0 --> Go to background!");
        }
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityStopped: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        LogWrapper.showLog(Log.INFO, "MyApp", "AppActivityLifecycleCallbacks - onActivityDestroyed: " + activity.getClass().getSimpleName());
    }
}
