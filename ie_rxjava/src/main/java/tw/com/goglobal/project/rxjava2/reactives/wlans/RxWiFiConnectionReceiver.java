package tw.com.goglobal.project.rxjava2.reactives.wlans;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import tw.com.goglobal.project.rxjava2.RxEventBus;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeWiFiEnableState;


//private void registerWiFiBroadcastReceiver() {
//    if (null == wiFiConnectionReceiver) {
//        wiFiConnectionReceiver = new IeWiFiConnectionReceiver();
//    }
//    final IntentFilter intentFilter = new IntentFilter();
//    intentFilter.setPriority(100);
//    //intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//    intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//    registerReceiver(wiFiConnectionReceiver, intentFilter);
//}
//
//private void unregisterWiFiBroadcastReceiver() {
//    if (null != wiFiConnectionReceiver) {
//        unregisterReceiver(wiFiConnectionReceiver);
//        wiFiConnectionReceiver = null;
//    }
//}

public final class RxWiFiConnectionReceiver extends BroadcastReceiver {

    private final RxEventBus eventBus;

    public RxWiFiConnectionReceiver(@NonNull final RxEventBus eventBus) {
        this.eventBus = eventBus;
    }

    private String getLogTag() { return this.getClass().getSimpleName(); }

    @Override
    @SuppressLint("MissingPermission")
    public void onReceive(Context context, Intent intent) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "onReceive");

        if (null == context) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - context is null!");
            return;
        }
        if (null == intent) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - intent is null!");
            return;
        }

        final String intentAction = intent.getAction();
        if (null == intentAction) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - intent Action is null!");
            return;
        }
        if (!WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intentAction)) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - Non WIFI_STATE_CHANGED_ACTION: " + intentAction);
            return;
        }

        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - WifiManager is null!");
            return;
        }

        if (!wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - Wifi is disabled!");
            eventBus.post(IeWiFiEnableState.DISABLED);
            return;
        }
        LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - Wifi is enabled!");
        eventBus.post(IeWiFiEnableState.ENABLED);

        //cannot require relevant connection info at the moment, e.g., ssid!!
//        String ssid = "";
//        try {
//            final WifiInfo info = wifiManager.getConnectionInfo();
//            LogWrapper.showLog(Log.INFO, getLogTag(), "onReceive - SSID: " + info.getSSID() + ", NetworkId: " + info.getNetworkId() + ", SupplicantState: " + info.getSupplicantState());
//            if (info.getSupplicantState().equals(SupplicantState.COMPLETED)) {
//                ssid = info.getSSID().replace("\"", "");
//            }
//        }
//        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, getLogTag(), "onReceive - Error on WifiManager.getConnectionInfo()", cause); }
//
//        LogWrapper.showLog(Log.INFO, getLogTag(), "onReceive - current SSID: " + ssid);
    }
}
