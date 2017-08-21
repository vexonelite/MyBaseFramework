package tw.realtime.project.rtbaseframework.widgets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import tw.com.kingshield.baseframework.LogWrapper;

/**
 * Created by vexonelite on 2017/8/3.
 */

public class PendingIntentBuilder {

    private String bAction;
    @Deprecated
    private String bMediaUrl;
    private String bBundleKey;
    private Bundle bBundle;
    private int bIntentFlag = Intent.FLAG_ACTIVITY_SINGLE_TOP;
    private int bPendingIntentFlag = PendingIntent.FLAG_CANCEL_CURRENT;
    private int bPendingRequestCode = 0;
    @Deprecated
    private boolean enableFlag;
    private boolean enableService = false;
    private Class<?> bTarget;
    private Context bContext;


    public PendingIntentBuilder setIntentContextSource (Context context) {
        bContext = context;
        return this;
    }

    public PendingIntentBuilder setIntentTargetClass (Class<?> target) {
        bTarget = target;
        return this;
    }

    public PendingIntentBuilder setBundleKey (String key) {
        bBundleKey = key;
        return this;
    }

    public PendingIntentBuilder setBundle (Bundle bundle) {
        bBundle = bundle;
        return this;
    }

    public PendingIntentBuilder setAction (String action) {
        bAction = action;
        return this;
    }

    @Deprecated
    public PendingIntentBuilder setMediaUrl (String mediaUrl) {
        bMediaUrl = mediaUrl;
        return this;
    }

    /**
     * Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK,
     * FLAG_ACTIVITY_SINGLE_TOP, and Intent.FLAG_ACTIVITY_CLEAR_TOP
     * <p>
     * Intent.FLAG_ACTIVITY_CLEAR_TOP:
     * If set, and the activity being launched is already running in the current task,
     * then instead of launching a new instance of that activity,
     * all of the other activities on top of it will be closed and
     * this Intent will be delivered to the (now on top) old activity as a new Intent.
     * <p>
     * In short, destroy all the current activity with its parent and re-create new one!!
     *
     * For example, consider a task consisting of the activities: A, B, C, D.
     * If D calls startActivity() with an Intent that resolves to the component of activity B,
     * then C and D will be finished and B receive the given Intent, resulting in the stack now being: A, B.
     *
     * The currently running instance of activity B in the above example
     * will either receive the new intent you are starting here in its onNewIntent() method,
     * or be itself finished and restarted with the new intent.
     * If it has declared its launch mode to be "multiple" (the default) and
     * you have not set FLAG_ACTIVITY_SINGLE_TOP in the same intent,
     * then it will be finished and re-created;
     * for all other launch modes or if FLAG_ACTIVITY_SINGLE_TOP is set
     * then this Intent will be delivered to the current instance's onNewIntent().
     *
     * This launch mode can also be used to good effect in conjunction with FLAG_ACTIVITY_NEW_TASK:
     * if used to start the root activity of a task,
     * it will bring any currently running instance of that task to the foreground,
     * and then clear it to its root state. This is especially useful,
     * for example, when launching an activity from the notification manager.
     */
    public PendingIntentBuilder setIntentFlag (int flag) {
        bIntentFlag = flag;
        return this;
    }

    /**
     * PendingIntent.FLAG_ONE_SHOT, FLAG_CANCEL_CURRENT, FLAG_UPDATE_CURRENT
     * <p>
     * PendingIntent.FLAG_UPDATE_CURRENT:
     * Flag indicating that if the described PendingIntent already exists,
     * then keep it but replace its extra data with what is in this new Intent.
     * For use with getActivity(Context, int, Intent, int), getBroadcast(Context, int, Intent, int),
     * and getService(Context, int, Intent, int).
     *
     * This can be used if you are creating intents where only the extras change,
     * and don't care that any entities that received your previous PendingIntent
     * will be able to launch it with your new extras even if they are not explicitly given to it
     * <p>
     * PendingIntent.FLAG_ONE_SHOT:
     * Flag indicating that this PendingIntent can be used only once.
     * For use with getActivity(Context, int, Intent, int), getBroadcast(Context, int, Intent, int),
     * and getService(Context, int, Intent, int).
     * If set, after send() is called on it, it will be automatically canceled for you
     * and any future attempt to send through it will fail.
     */
    public PendingIntentBuilder setPendinIntentFlag (int flag) {
        bPendingIntentFlag = flag;
        return this;
    }

    public PendingIntentBuilder setEnableServiceFlag (boolean flag) {
        enableService = flag;
        return this;
    }

    public PendingIntentBuilder setPendingIntentRequestCode (int requestCode) {
        bPendingRequestCode = requestCode;
        return this;
    }

    public PendingIntent build () {
        Intent intent = new Intent(bContext, bTarget)
                            .setFlags(bIntentFlag);
        if ( (null != bBundleKey) && (!bBundleKey.isEmpty()) && (null != bBundle) ) {
            LogWrapper.showLog(Log.INFO, "*** PendingIntentBuilder", "build - bBundleKey: " + bBundleKey);
            intent.putExtra(bBundleKey, bBundle);
        }
        if (null != bAction) {
            intent.setAction(bAction);
            LogWrapper.showLog(Log.INFO, "*** PendingIntentBuilder", "build - bAction: " + bAction);
        }
        return (enableService)
                ? PendingIntent.getService(bContext, bPendingRequestCode, intent, bPendingIntentFlag)
                : PendingIntent.getActivity(bContext, bPendingRequestCode, intent, bPendingIntentFlag);
    }
}
