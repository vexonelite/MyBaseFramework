package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.app.RequireContextDelegate;


public final class QWiFiNetworkManager {

    private static final String UNKNOWN = "_UNKNOWN";

    private volatile static QWiFiNetworkManager instance;



    @NonNull
    public final ConnectivityManager.NetworkCallback monitorWiFiNetworkCallback;

    private BindingWiFiNetworkCallback bindingWiFiNetworkCallback;

    public RequireContextDelegate requireContextCallback;

    /** Returns singleton class instance */
    public static QWiFiNetworkManager getInstance() {
        if (instance == null) {
            synchronized (QWiFiNetworkManager.class) {
                if (instance == null) {
                    instance = new QWiFiNetworkManager();
                }
            }
        }
        return instance;
    }

    private QWiFiNetworkManager() {
        monitorWiFiNetworkCallback = new MonitorWiFiNetworkCallback();
    }

    public void onTerminate() {
        //networkCallbackList.clear();
    }

    private static String getLogTag() { return QWiFiNetworkManager.class.getSimpleName(); }

//    public void addIeNetworkDelegate(@NonNull IeNetworkDelegate callback) {
//        LogWrapper.showLog(Log.INFO, getLogTag(), "addIeNetworkDelegate - [pre] networkCallbackList.size: " + networkCallbackList.size());
//        if (!networkCallbackList.contains(callback)) {
//            networkCallbackList.add(callback);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "addIeNetworkDelegate - [post] networkCallbackList.size: " + networkCallbackList.size());
//        }
//    }

//    public void removeIeNetworkDelegate(@NonNull IeNetworkDelegate callback) {
//        LogWrapper.showLog(Log.INFO, getLogTag(), "removeIeNetworkDelegate - [pre] networkCallbackList.size: " + networkCallbackList.size());
//        if (networkCallbackList.contains(callback)) {
//            networkCallbackList.remove(callback);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "removeIeNetworkDelegate - [post] networkCallbackList.size: " + networkCallbackList.size());
//        }
//    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class MonitorWiFiNetworkCallback extends ConnectivityManager.NetworkCallback {

        private String cachedSSID = UNKNOWN;

        @Override
        public void onLost(@NonNull Network network) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onLost - from " + cachedSSID);
//            for (final IeNetworkDelegate callback : networkCallbackList) {
//                callback.onLost(network);
//            }
        }

//        @Override
//        public void onUnavailable() {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onUnavailable");
//        }
//
//        @Override
//        public void onLosing(@NonNull Network network, int maxMsToLive) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onLosing");
//        }

        @Override
        public void onAvailable(@NonNull Network network) {
            updateCachedSSID();
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onAvailable - cachedSSID: " + cachedSSID);
//            for (final IeNetworkDelegate callback : networkCallbackList) {
//                callback.onAvailable(network);
//            }
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network,
                                          @NonNull NetworkCapabilities networkCapabilities) {
            updateCachedSSID();
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onCapabilitiesChanged - for " + cachedSSID);
//            for (final IeNetworkDelegate callback : networkCallbackList) {
//                callback.onCapabilitiesChanged(network, networkCapabilities);
//            }
        }

        @Override
        public void onLinkPropertiesChanged(@NonNull Network network,
                                            @NonNull LinkProperties linkProperties) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onLinkPropertiesChanged - for " + cachedSSID);
        }

//        @Override
//        public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onBlockedStatusChanged - is blocked: " + blocked);
//        }

        private void updateCachedSSID() {
            final String ssid = (null != requireContextCallback)
                    ? ConnectivityUtils.getSsidViaWifiInfo(requireContextCallback.requireContext())
                    : "";
            if ( (ssid.length() > 0) && (!ssid.equals(cachedSSID)) ) { cachedSSID = ssid; }
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] updateCachedSSID - ssid: " + ssid + ", cachedSSID: " + cachedSSID);
        }
    }

    public void connectToSSID(
            @NonNull Context context,
            @NonNull String networkSSID,
            @NonNull String networkPassword) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            final WifiConfiguration configuration = ConnectivityUtils.connectToSpecifiedRouterIfPossible(context, networkSSID, networkPassword);
        }
        else {
            final WifiNetworkSpecifier specifier = ConnectivityUtils.getWifiNetworkSpecifier(networkSSID, networkPassword, false);
            final NetworkRequest request = ConnectivityUtils.getNetworkRequest(
                    specifier, NetworkCapabilities.TRANSPORT_WIFI);
            if (null != bindingWiFiNetworkCallback) {
                ConnectivityUtils.connectivityManagerUnregisterNetworkCallback(context, bindingWiFiNetworkCallback);
            }
            bindingWiFiNetworkCallback = new BindingWiFiNetworkCallback(networkSSID);
            ConnectivityUtils.connectToSpecifiedRouterViaRequestNetwork(context, request, bindingWiFiNetworkCallback);
        }
    }


//    @Nullable
//    public ConnectivityManager.NetworkCallback getBindingWiFiNetworkCallback() { return bindingWiFiNetworkCallback; }

    @NonNull
    public String getBindingWiFiNetworkSSID() {
        return (null != bindingWiFiNetworkCallback) ? bindingWiFiNetworkCallback.networkSSID : "";
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class BindingWiFiNetworkCallback extends ConnectivityManager.NetworkCallback {

        final String networkSSID;
        boolean hasBoundWithProcess = false;

        public BindingWiFiNetworkCallback(@NonNull String networkSSID) { this.networkSSID = networkSSID; }

        @Override
        public void onLost(@NonNull Network network) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onLost");
            if (null != requireContextCallback) {
                ConnectivityUtils.connectivityManagerBindProcessToNetwork(requireContextCallback.requireContext(), null);
            }
        }

//        @Override
//        public void onUnavailable() {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onUnavailable");
//        }
//
//        @Override
//        public void onLosing(@NonNull Network network, int maxMsToLive) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onLosing");
//        }

        @Override
        public void onAvailable(@NonNull Network network) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onAvailable");
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network,
                                          @NonNull NetworkCapabilities networkCapabilities) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onCapabilitiesChanged!");
            if (null != requireContextCallback) {
                final Context context = requireContextCallback.requireContext();
                final boolean isInterestedType = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                final String ssid = ConnectivityUtils.getSsidViaWifiInfo(context);
                LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onCapabilitiesChanged - isInterestedType: " + isInterestedType + ", ssid: " + ssid);
                if (isInterestedType && ssid.equals(networkSSID)) {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onCapabilitiesChanged - condition is matched!");
                    if (!hasBoundWithProcess) {
                        ConnectivityUtils.connectivityManagerBindProcessToNetwork(context, network);
                    }
                }
            }
        }

        @Override
        public void onLinkPropertiesChanged(
                @NonNull Network network, @NonNull LinkProperties linkProperties) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onLinkPropertiesChanged");
        }

//        @Override
//        public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "[Binding] onBlockedStatusChanged - is blocked: " + blocked);
//        }
    }
}
