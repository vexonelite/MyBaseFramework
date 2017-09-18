package tw.realtime.project.rtbaseframework.widgets;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Created by vexonelite on 2017/8/3.
 */

public class NotificationHelper {

    private Context nContext;
    private String nTitle;
    private String nBody;
    private String nGroup;
    private int nSmallIconResourceId = 0;
    private int nLargeIconResourceId = 0;
    private int nNotificationNumber = 0;
    private boolean nOngoing = false;
    private boolean nAutoCancel = true;
    private boolean nGroupSummary = false;

    private Uri nSoundUri;

    private PendingIntent nPendingIntent;

    private String nActionTitle;
    private int nActionIconResourceId;
    private PendingIntent nActionPendingIntent;


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }


    public NotificationHelper setContext (Context context) {
        nContext = context;
        return this;
    }

    public NotificationHelper setSmallIconResourceId (int resourceId) {
        nSmallIconResourceId = resourceId;
        return this;
    }

    public NotificationHelper setLargeIconResourceId (int resourceId) {
        nLargeIconResourceId = resourceId;
        return this;
    }

    public NotificationHelper setNotificationNumber (int number) {
        nNotificationNumber = number;
        return this;
    }

    public NotificationHelper setPendingIntent (PendingIntent pendingIntent) {
        nPendingIntent = pendingIntent;
        return this;
    }

    public NotificationHelper setTitle (String title) {
        nTitle = title;
        return this;
    }

    public NotificationHelper setBody (String body) {
        nBody = body;
        return this;
    }

    /**
     * @see <a href="http://stackoverflow.com/questions/6966216/disable-clearing-notification-for-my-application">Reference</a>
     * <p>
     * The setOngoing(true) call acheives this,
     * and setAutoCancel(false) stops the notification from going away
     * when the user taps the notification.
     * The notification will be cleared if the application is uninstalled or by calling Cancel or CancelAll:
     * <p>
     * @see <a href="http://developer.android.com/reference/android/app/NotificationManager.html#cancel(int)">Reference</a>
     * @param flag
     * @return
     */
    public NotificationHelper setOngoingFlag (boolean flag) {
        nOngoing = flag;
        return this;
    }

    /**
     * Set the notification to auto-cancel.
     * This means that the notification will disappear
     * after the user taps it, rather than remaining until it's explicitly dismissed.
     * @param flag
     * @return
     */
    public NotificationHelper setAutoCancelFlag (boolean flag) {
        nAutoCancel = flag;
        return this;
    }

    public NotificationHelper setGroupSummaryFlag (boolean flag) {
        nGroupSummary = flag;
        return this;
    }

    /**
     * Android N also allows you to bundle similar notifications to appear as a single notification.
     * To make this possible, Android N uses the existing NotificationCompat.Builder.setGroup() method.
     * Users can expand each of the notifications, and perform actions
     * such as reply and dismiss on each of the notifications, individually from the notification shade.
     * <p>
     * Meaning the setGroup will only make a difference if the device supports it.
     * <p>
     * Devices that support it are:
     * <p>
     * - Android Wear devices. when showing remote notifications, you can group together them.
     * <p>
     * - Android N. Devices running the Android N developer preview
     * (or in the future the official N release), will show a group of notifications together
     * <p>
     * The following blog post shows how those work on Android N:
     * https://medium.com/exploring-android/android-n-introducing-upgraded-notifications-d4dd98a7ca92
     * @param group
     * @return
     */
    public NotificationHelper setGroup (String group) {
        nGroup = group;
        return this;
    }

    public NotificationHelper setActionTitle (String title) {
        nActionTitle = title;
        return this;
    }

    public NotificationHelper setActionIconResourceId (int resourceId) {
        nActionIconResourceId = resourceId;
        return this;
    }

    public NotificationHelper setActionPendingIntent (PendingIntent pendingIntent) {
        nActionPendingIntent = pendingIntent;
        return this;
    }

    public NotificationHelper setSoundUri (Uri soundUri) {
        nSoundUri = soundUri;
        return this;
    }


    public Notification generate() {

        if ( (null == nContext) || (null == nTitle) || (null == nBody)) {
            return null;
        }

        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(nContext)
                    .setContentTitle(nTitle)
                    .setTicker(nTitle)
                    .setContentText(nBody)
                    .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                    .setSmallIcon(nSmallIconResourceId)
                    .setAutoCancel(nAutoCancel)
                    .setOngoing(nOngoing)
                    .setGroupSummary(nGroupSummary)
                    .setSound( (null != nSoundUri) ? nSoundUri : defaultSoundUri);

            Bitmap icon = generateLargeIconBitmap();
            if (null != icon) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(nBody));
            }
            if (null != nPendingIntent) {
                builder.setContentIntent(nPendingIntent);
            }
            if ( (null != nActionTitle) && (null != nActionPendingIntent) ) {
                builder.addAction(nActionIconResourceId, nActionTitle, nActionPendingIntent);
            }
            if ( (null != nGroup) && (!nGroup.isEmpty()) ) {
                builder.setGroup(nGroup);
            }
            if (nNotificationNumber > 0) {
                builder.setNumber(nNotificationNumber);
            }
//            if (nGroupSummary) {
//                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(nTitle));
//                builder.setStyle(new NotificationCompat.InboxStyle()
//                        .addLine("Line 1")
//                        .addLine("Line 2")
//                        .setSummaryText("Inbox summary text")
//                        .setBigContentTitle("Big content title"));
//            }

            //builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            //您的應用程式可控制安全鎖定螢幕上所顯示通知的詳細資料可見程度。
            // 您可以呼叫 setVisibility()，然後指定下列其中一個值：
            //VISIBILITY_PUBLIC：顯示通知的完整內容。
            //VISIBILITY_SECRET：不在鎖定螢幕上顯示此通知的任何部分。
            //VISIBILITY_PRIVATE：顯示基本資訊，例如通知的圖示與內容標題，但隱藏通知的完整內容。

            return builder.build();
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "exception on generate", e);
            return null;
        }
    }

    private Bitmap generateLargeIconBitmap () {
        try {
            return BitmapFactory.decodeResource(nContext.getResources(), nLargeIconResourceId);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static void updateNotification (Context context,
                                           Notification notification,
                                           int notificationId) {
        if ( (null == context) || (null == notification) ) {
            return;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notification);
    }

    public static void updateNotification (Context context,
                                           Notification notification,
                                           String tag,
                                           int notificationId) {
        if ( (null == context) || (null == tag) || (tag.isEmpty()) || (null == notification) ) {
            return;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        //notificationManager.notify(notificationId, notification);
        notificationManager.notify(tag, notificationId, notification);
    }

    public static void updateSummaryNotification (Context context,
                                                  NotificationHelper summary,
                                                  List<NotificationHelper> helperList,
                                                  int notificationId) {
        if ( (null == context) || (null == helperList) || (helperList.isEmpty()) ) {
            return;
        }
        for (NotificationHelper helper : helperList) {
            NotificationHelper.updateNotification(context, helper.generate(), ((int) System.currentTimeMillis()) );
        }
        NotificationHelper.updateNotification(context, summary.generate(), notificationId);
    }


    public static void cancelAllNotifications (Context context) {
        if (null == context) {
            return;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancelAll();
    }
}
