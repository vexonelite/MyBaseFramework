package tw.com.goglobal.project.rxjava2.reactives.wlans;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tw.com.goglobal.project.rxjava2.AbstractRxTask;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResult;
import tw.realtime.project.rtbaseframework.utils.CodeUtils;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.ConnectivityUtils;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.WiFiScans;

public final class NetTasks {

    public static final class ParseWiFiScanResult extends AbstractRxTask<List<WiFiScans.Delegate>> implements Callable<List<ScanResult>> {

        private final Context context;
        private final Function<List<ScanResult>, List<WiFiScans.Delegate>> converter;

        public ParseWiFiScanResult(@NonNull final Context context,
                                   @NonNull final Function<List<ScanResult>, List<WiFiScans.Delegate>> converter) {
            this.context = context;
            this.converter = converter;
        }

        @Override
        public void runTask() {
            //LogWrapper.showLog(Log.INFO, "NetTasks", "ParseWiFiScanResultTask - runTask - on Thread: " + Thread.currentThread().getName());
            rxDisposeIfPossible();
            setDisposable(
                    Single.fromCallable(this)
                            .map(converter)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ApiDisposableSingleObserver())
            );
        }

        @Override
        public List<ScanResult> call() throws IeRuntimeException {
            LogWrapper.showLog(Log.INFO, "NetTasks", "ParseWiFiScanResult - call - on Thread: " + Thread.currentThread().getName());
            try { return ConnectivityUtils.getWiFiScanResult(context); }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "NetTasks", "ParseWiFiScanResult - Error on call!!", cause);
                throw new IeRuntimeException(cause, "00000");
            }
        }
    }

//    static final class ScanResultListToWiFiScansDelegateList implements Function<List<ScanResult>, List<WiFiScans.Delegate>> {
//        @NonNull
//        @Override
//        public List<WiFiScans.Delegate> apply(@NonNull final List<ScanResult> scanResults) throws IeRuntimeException {
//            LogWrapper.showLog(Log.INFO, "NetTasks", "ScanResultListToWiFiScansDelegateList - on Thread: " + Thread.currentThread().getName());
//
//            try { return doConversion(scanResults); }
//            catch (Exception cause) {
//                LogWrapper.showLog(Log.ERROR, "NetTasks", "Error on ScanResultListToWiFiScansDelegateList: " + cause.getLocalizedMessage());
//                if (cause instanceof IeRuntimeException) { throw (IeRuntimeException) cause; }
//                else { throw new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR); }
//            }
//        }
//
//        @NonNull
//        private List<WiFiScans.Delegate> doConversion(@NonNull final List<ScanResult> scanResults) {
//            final WiFiScans.Converter converter = new WiFiScans.Converter(AppConstants.CellTypes.ITEM);
//            final WiFiScans.WiFiAccessPointComparator apComparator = new WiFiScans.WiFiAccessPointComparator();
//
//            final List<WiFiScans.Delegate> convertedList = converter.convertIntoData(scanResults);
//            final List<WlanScanResult.AppWiFiApProxy> proxyList = new ArrayList<>();
//            for(final WiFiScans.Delegate scanDelegate : convertedList) {
//                proxyList.add(new WlanScanResult.AppWiFiApProxy(scanDelegate));
//            }
//
//            // [start] revision in 2020/07/30
////            final List<WiFiScans.Delegate> apList = new ArrayList<>();
////            final Map<String, List<WlanScanResult.AppWiFiApProxy>> apMap = FwVerAndProdSnDelegate.classifyProductList(proxyList);
////            final List<WlanScanResult.AppWiFiApProxy> ap9xList = apMap.get(AppConstants.AppKeys.PRODUCT_AP9X);
////            if (null != ap9xList) {
////                Collections.sort(ap9xList, apComparator);
////                apList.addAll(ap9xList);
////            }
////            final List<WlanScanResult.AppWiFiApProxy> dongleList = apMap.get(AppConstants.AppKeys.PRODUCT_DONGLE);
////            if (null != dongleList) {
////                Collections.sort(dongleList, apComparator);
////                apList.addAll(dongleList);
////            }
////            return apList;
//
//            final Map<String, List<WlanScanResult.AppWiFiApProxy>> apMap = FwVerAndProdSnDelegate.classifyProductList(proxyList);
//            final List<WlanScanResult.AppWiFiApProxy> apList = FwVerAndProdSnDelegate.productMapToProductList(apMap, apComparator);
//            return new ArrayList<>(apList);
//            // [end] revision in 2020/07/30
//        }
//    }

    ///

    /** 60 seconds */
    static final int WIFI_CONNECTION_TIMEOUT = 60;
    /** 3 times */
    static final int CONNECT_TO_SSID_RETRY_LIMIT = 3;

    public static abstract class BaseWiFiConnector {

        protected final Context context;
        protected final WiFiScans.Delegate wiFiAp;
        protected final IeApiResult<WiFiScans.Delegate> callback;
        protected final WifiManager wifiManager;
        protected final ConnectivityManager connectivityManager;

        public BaseWiFiConnector(
                @NonNull final Context context,
                @NonNull final WiFiScans.Delegate wiFiAp,
                @NonNull final IeApiResult<WiFiScans.Delegate> callback) {
            this.context = context.getApplicationContext();
            this.wiFiAp = wiFiAp;
            this.callback = callback;
            this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            this.connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        protected final String getLogTag() { return this.getClass().getSimpleName() + "[" + wiFiAp.theSSID() + "]"; }

        public void onCleared() { /* LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - onCleared"); */ }

        protected final void verifyEssentialComponents() {
            if (null == wifiManager) {
                final IeRuntimeException cause = new IeRuntimeException("wifiManager is null", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
                callback.onError(cause);
                return;
            }

            if (null == connectivityManager) {
                final IeRuntimeException cause = new IeRuntimeException("connectivityManager is null", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
                callback.onError(cause);
            }
        }

        public abstract void startConnectToSSID();
    }

    /**
     * Connect to the specific Wi-Fi Router for {@link Build.VERSION.SDK_INT} < {@link Build.VERSION_CODES.Q}
     */
    public static final class LegacyWiFiConnector extends BaseWiFiConnector {

        private int retryCount = 0;
        private Disposable networkDisconnectionEventDisposable;
        private Disposable networkConnectionEventDisposable;
        private Disposable wiFiConnectionTimeoutDisposable;

        public LegacyWiFiConnector(
                @NonNull final Context context,
                @NonNull final WiFiScans.Delegate wiFiAp,
                @NonNull final IeApiResult<WiFiScans.Delegate> callback) {
            super(context, wiFiAp, callback);
        }

        @Override
        public void onCleared() {
            LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - onCleared");
            networkDisconnectionEventDisposeIfPossible();
            networkConnectionEventDisposeIfPossible();
            wiFiConnectionTimeoutDisposableIfPossible();
        }

        @Override
        public void startConnectToSSID() {
            verifyEssentialComponents();

            if (wifiManager.isWifiEnabled()) {
                final String currentSSID = ConnectivityUtils.getSsidViaWifiInfo(context);
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - startConnectToSSID - " + currentSSID + " =? " + ConnectivityUtils.UNKNOWN_SSID);
                if (currentSSID.equals(WifiManager.UNKNOWN_SSID)) {
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - startConnectToSSID -> Wi-Fi is enabled but has not yet connected to any SSID --> connectToSSID");
                    connectToSSID();
                }
                else {
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - startConnectToSSID -> Wi-Fi is enabled and has connected to " + currentSSID + " --> disconnectAndConnectToSSID");
                    disconnectAndConnectToSSID();
                }
            }
            else {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - startConnectToSSID -> Wi-Fi is disabled --> connectToSSID");
                connectToSSID();
            }
        }

        private void disconnectAndConnectToSSID() {
            LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - disconnectAndConnectToSSID");
            subscribeNetworkDisconnectionEvent(new DisconnectEventCallback());
            ConnectivityUtils.disconnectToWiFi(context);
        }

        @SuppressLint("MissingPermission")
        private void connectToSSID() {
            LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - connectToSSID");
            subscribeNetworkConnectionEvent(new ConnectSsidEventCallback());
            wifiManager.setWifiEnabled(true);
            ConnectivityUtils.connectToSpecifiedRouterIfPossible(context, wiFiAp.theSSID(), wiFiAp.thePassword());
        }

        ///

        private void wiFiConnectionTimeoutDisposableIfPossible() {
            if (null != wiFiConnectionTimeoutDisposable) {
                if (!wiFiConnectionTimeoutDisposable.isDisposed()) {
                    wiFiConnectionTimeoutDisposable.dispose();
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - wiFiConnectionTimeoutDisposableIfPossible - wiFiConnectionTimeoutDisposable - dispose");
                }
                wiFiConnectionTimeoutDisposable = null;
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - wiFiConnectionTimeoutDisposableIfPossible - wiFiConnectionTimeoutDisposable - reset");
            }
        }

        public void setupWiFiConnectionTimeout() {
            wiFiConnectionTimeoutDisposableIfPossible();
            wiFiConnectionTimeoutDisposable = Observable.timer(WIFI_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new WiFiConnectionTimeoutTask());
        }

        private class WiFiConnectionTimeoutTask implements Consumer<Object> {
            @Override
            public void accept(final Object result) throws Exception {
                LogWrapper.showLog(Log.ERROR, "NetTasks", getLogTag() + " - WiFiConnectionTimeoutTask - Connecting to [" + wiFiAp.theSSID() + "]: Timeout");
                onCleared();
                final IeRuntimeException cause = new IeRuntimeException(wiFiAp.theSSID(), ErrorCodes.WiFi.TIMEOUT);
                callback.onError(cause);
            }
        }

        ///

        private void networkDisconnectionEventDisposeIfPossible() {
            if (null != networkDisconnectionEventDisposable) {
                if (!networkDisconnectionEventDisposable.isDisposed()) {
                    networkDisconnectionEventDisposable.dispose();
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - networkDisconnectionEventDisposeIfPossible - networkDisconnectionEventDisposable - dispose");
                }
                networkDisconnectionEventDisposable = null;
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - networkDisconnectionEventDisposeIfPossible - networkDisconnectionEventDisposable - reset");
            }
        }

        private void subscribeNetworkDisconnectionEvent(@NonNull Consumer<Object> callback) {
            networkDisconnectionEventDisposeIfPossible();
            networkDisconnectionEventDisposable = RxQWiFiNetworkManager.getInstance()
                    .getEventBus()
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
            LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - subscribeNetworkDisconnectionEvent");
        }

        private class DisconnectEventCallback extends AbsRxNetworkStateCallback {

            @Override
            public void onLost(@NonNull final Network network) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - DisconnectEventCallback - onLost");
                networkDisconnectionEventDisposeIfPossible();
                connectToSSID();
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - DisconnectEventCallback - onAvailable");
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - DisconnectEventCallback - onCapabilitiesChanged");
            }
        }

        ///

        private void networkConnectionEventDisposeIfPossible() {
            if (null != networkConnectionEventDisposable) {
                if (!networkConnectionEventDisposable.isDisposed()) {
                    networkConnectionEventDisposable.dispose();
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - networkConnectionEventDisposeIfPossible - networkConnectionEventDisposable - dispose");
                }
                networkConnectionEventDisposable = null;
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - networkConnectionEventDisposeIfPossible - networkConnectionEventDisposable - reset");
            }
        }

        private void subscribeNetworkConnectionEvent(@NonNull Consumer<Object> callback) {
            networkConnectionEventDisposeIfPossible();
            networkConnectionEventDisposable = RxQWiFiNetworkManager.getInstance()
                    .getEventBus()
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
            LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - subscribeNetworkConnectionEvent");
        }

        private class ConnectSsidEventCallback extends AbsRxNetworkStateCallback {

            @Override
            public void onLost(@NonNull Network network) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - onLost - retryCount: " + retryCount);
                networkConnectionEventDisposeIfPossible();
                failureHandler();
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - onAvailable");
                // devices before Android N might need to wait very long time to have the onCapabilitiesChanged() get called!!
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { readyToJoinSSID(); }
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - onCapabilitiesChanged");
                // devices before Android N might need to wait very long time to have the onCapabilitiesChanged() get called!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { readyToJoinSSID(); }
            }

            // added in 2020/07/16
            private void readyToJoinSSID() {
                final String currentSSID = ConnectivityUtils.getSsidViaWifiInfo(context);
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - readyToJoinSSID - currentSSID: " + currentSSID + "");
                networkConnectionEventDisposeIfPossible();
                if (currentSSID.equals(wiFiAp.theSSID())) {
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - readyToJoinSSID - connected");
                    callback.onSuccess(wiFiAp);
                }
                else {
                    LogWrapper.showLog(Log.ERROR, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - readyToJoinSSID - wrong ssid - retryCount: " + retryCount);
                    failureHandler();
                }
            }

            // added in 2020/04/29
            private void failureHandler() {
                if (retryCount < CONNECT_TO_SSID_RETRY_LIMIT) {
                    retryCount++;
                    startConnectToSSID();
                }
                else {
                    LogWrapper.showLog(Log.ERROR, "NetTasks", getLogTag() + " - ConnectSsidEventCallback - Connecting to [" + wiFiAp.theSSID() + "]: Reach Retry Limit!");
                    final IeRuntimeException cause = new IeRuntimeException(wiFiAp.theSSID(), ErrorCodes.WiFi.REACH_RETRY_LIMIT);
                    callback.onError(cause);
                }
            }
        }
    }

    /**
     * Connect to the specific Wi-Fi Router for {@link Build.VERSION.SDK_INT} >= {@link Build.VERSION_CODES.Q}
     */
    @TargetApi(Build.VERSION_CODES.Q)
    public static final class AqWiFiConnector extends BaseWiFiConnector {

        private ConnectivityManager.NetworkCallback networkCallback;

        public AqWiFiConnector(
                @NonNull final Context context,
                @NonNull final WiFiScans.Delegate wiFiAp,
                @NonNull final IeApiResult<WiFiScans.Delegate> callback) {
            super(context, wiFiAp, callback);
        }

        @Override
        public void startConnectToSSID() {
            verifyEssentialComponents();

            unregisterNetworkCallback();
            ConnectivityUtils.removeSsidSuggestion(context, wiFiAp.theSSID(), wiFiAp.thePassword());

            LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - startConnectToSSID");
            final NetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsid(wiFiAp.theSSID())
                    .setWpa2Passphrase(wiFiAp.thePassword())
                    .build();
            final NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build();
            if (null == networkCallback) {
                networkCallback = new RequestNetworkCallback();
                LogWrapper.showLog(Log.INFO, "NetTasks",getLogTag() + " - startConnectToSSID - use Built-in network callback [RequestNetworkCallback]!");
            }
            connectivityManager.requestNetwork(request, networkCallback);
        }

        public void unregisterNetworkCallback() {
            if ( (null != networkCallback) && (null != connectivityManager) ) {
                try {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                    networkCallback = null;
                }
                catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "NetTasks",getLogTag() + " - Error on connectivityManager.unregisterNetworkCallback()", cause); }
            }
            LogWrapper.showLog(Log.INFO, "NetTasks",getLogTag() + " - unregisterNetworkCallback");
        }

        /**
         * Does not involve {@link AqWiFiConnector#unregisterNetworkCallback} when the {@link ConnectivityManager.NetworkCallback}
         * gets called because the process of request might be in progress.
         * It is a right time to involve {@link AqWiFiConnector#unregisterNetworkCallback}
         * when {@link AqWiFiConnector#startConnectToSSID} gets involved.
         */
        private class RequestNetworkCallback extends ConnectivityManager.NetworkCallback {

            private boolean hasNotifiedCallback = false;

            @Override
            public void onAvailable(@NonNull final Network network) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - RequestNetworkCallback - onAvailable");
            }

            @Override
            public void onCapabilitiesChanged(@NonNull final Network network,
                                              @NonNull final NetworkCapabilities networkCapabilities) {
                final String currentSSID = ConnectivityUtils.getSsidViaWifiInfo(context);
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - RequestNetworkCallback - onCapabilitiesChanged - currentSSID: " + currentSSID + "");

                if (currentSSID.equals(wiFiAp.theSSID())) {
                    LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - RequestNetworkCallback - onCapabilitiesChanged - connected");
                    if (!hasNotifiedCallback) {
                        hasNotifiedCallback = true;
                        callback.onSuccess(wiFiAp);
                    }
                }
                else {
                    unregisterNetworkCallback();
                    if (!hasNotifiedCallback) {
                        hasNotifiedCallback = true;
                        final IeRuntimeException cause = new IeRuntimeException(wiFiAp.theSSID(), ErrorCodes.WiFi.CONNECTED_SSID_IS_UNEXPECTED);
                        callback.onError(cause);
                    }
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - RequestNetworkCallback - onLost");
                //unregisterNetworkCallback(); should not do this within the callback
            }

            @Override
            public void onUnavailable() {
                // the method gets called when user has cancel the connection process
                LogWrapper.showLog(Log.INFO, "NetTasks", getLogTag() + " - RequestNetworkCallback - onUnavailable");
                if (!hasNotifiedCallback) {
                    hasNotifiedCallback = true;
                    final IeRuntimeException cause = new IeRuntimeException(wiFiAp.theSSID(), ErrorCodes.WiFi.NETWORK_UNAVAILABLE);
                    callback.onError(cause);
                }
                //unregisterNetworkCallback(); should not do this within the callback
            }
        }
    }
}
