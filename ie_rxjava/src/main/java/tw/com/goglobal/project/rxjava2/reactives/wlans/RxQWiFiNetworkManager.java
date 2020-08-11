package tw.com.goglobal.project.rxjava2.reactives.wlans;

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
import androidx.annotation.Nullable;

import tw.com.goglobal.project.rxjava2.RxEventBus;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.app.RequireContextDelegate;
import tw.realtime.project.rtbaseframework.widgets.Mutexs;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.ConnectivityUtils;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeNetworkCallbackState;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeNetworkStateInfo;


public final class RxQWiFiNetworkManager {

    private static final String UNKNOWN = "_UNKNOWN";

    private volatile static RxQWiFiNetworkManager instance;

    private RxEventBus eventBus;

    @NonNull
    public final ConnectivityManager.NetworkCallback monitorWiFiNetworkCallback;

    private BindingWiFiNetworkCallback bindingWiFiNetworkCallback;

    public RequireContextDelegate requireContextCallback;

    //private final MutexNetworkWrapper networkWrapper;         // added in 2020/06/15
    private final Mutexs.DataWrapper<Network> networkWrapper;   // added in 2020/07/13

    /** Returns singleton class instance */
    public static RxQWiFiNetworkManager getInstance() {
        if (instance == null) {
            synchronized (RxQWiFiNetworkManager.class) {
                if (instance == null) {
                    instance = new RxQWiFiNetworkManager();
                }
            }
        }
        return instance;
    }

    private RxQWiFiNetworkManager() {
        this.monitorWiFiNetworkCallback = new MonitorWiFiNetworkCallback();
        //this.networkWrapper = new MutexNetworkWrapper();  // added in 2020/06/15
        this.networkWrapper = new Mutexs.DataWrapper<>();   // added in 2020/07/13
    }

    private static String getLogTag() { return RxQWiFiNetworkManager.class.getSimpleName(); }

    @NonNull
    public RxEventBus getEventBus() {
        if (null == eventBus) { eventBus = new RxEventBus(); }
        return eventBus;
    }

    private void postEventThroughBus(
            @NonNull final IeNetworkCallbackState callbackState,
            @Nullable final Network network,
            @Nullable final NetworkCapabilities networkCapabilities,
            @Nullable final LinkProperties linkProperties) {
        if (null == eventBus) { return; }
        eventBus.post(new IeNetworkStateInfo(callbackState, network, networkCapabilities, linkProperties));
        LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] postEventThroughBus");
    }

    // [start] added in 2020/06/15
    @Nullable
    public Network getCurrentNetwork() {
        //return networkWrapper.getCurrentNetwork();
        return networkWrapper.getCurrentData(); // revision in 2020/07/13
    }
    // [end] added in 2020/06/15

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class MonitorWiFiNetworkCallback extends ConnectivityManager.NetworkCallback {

        private String cachedSSID = UNKNOWN;

        @Override
        public void onLost(@NonNull Network network) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onLost - from " + cachedSSID);
            updateCachedSSID();
            // [start] revision in 2020/06/15
            //try { networkWrapper.updateCurrentNetwork(null); }
            try { networkWrapper.updateCurrentData(null); } // revision in 2020/07/13
            catch (InterruptedException interruptCause) { LogWrapper.showLog(Log.ERROR, getLogTag(), "[Monitor] onLost - InterruptedException on updateCurrentNetwork"); }
            // [end] revision in 2020/06/15
            postEventThroughBus(IeNetworkCallbackState.LOST, network, null, null);
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
        public void onAvailable(@NonNull final Network network) {
            updateCachedSSID();
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onAvailable - cachedSSID: " + cachedSSID);
            // [start] revision in 2020/06/15
            //try { networkWrapper.updateCurrentNetwork(network); }
            try { networkWrapper.updateCurrentData(network); } // revision in 2020/07/13
            catch (InterruptedException interruptCause) { LogWrapper.showLog(Log.ERROR, getLogTag(), "[Monitor] onAvailable - InterruptedException on updateCurrentNetwork"); }
            // [end] revision in 2020/06/15
            postEventThroughBus(IeNetworkCallbackState.AVAILABLE, network, null, null);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull final Network network,
                                          @NonNull final NetworkCapabilities networkCapabilities) {
            updateCachedSSID();
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onCapabilitiesChanged - for " + cachedSSID);
            // [start] revision in 2020/06/15
            //try { networkWrapper.updateCurrentNetwork(network); }
            try { networkWrapper.updateCurrentData(network); } // revision in 2020/07/13
            catch (InterruptedException interruptCause) { LogWrapper.showLog(Log.ERROR, getLogTag(), "[Monitor] onCapabilitiesChanged - InterruptedException on updateCurrentNetwork"); }
            // [end] revision in 2020/06/15
            postEventThroughBus(IeNetworkCallbackState.CAPABILITIES_CHANGED, network, networkCapabilities, null);
        }

        @Override
        public void onLinkPropertiesChanged(@NonNull final Network network,
                                            @NonNull final LinkProperties linkProperties) {
            updateCachedSSID();
            LogWrapper.showLog(Log.INFO, getLogTag(), "[Monitor] onLinkPropertiesChanged - for " + cachedSSID);
            // [start] revision in 2020/06/15
            //try { networkWrapper.updateCurrentNetwork(network); }
            try { networkWrapper.updateCurrentData(network); } // revision in 2020/07/13
            catch (InterruptedException interruptCause) { LogWrapper.showLog(Log.ERROR, getLogTag(), "[Monitor] onLinkPropertiesChanged - InterruptedException on updateCurrentNetwork"); }
            // [end] revision in 2020/06/15
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
