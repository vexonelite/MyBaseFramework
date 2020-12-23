package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public final class ConnectivityUtils {

    /**
     * Please use {@link WifiManager.UNKNOWN_SSID} instead!!
     */
    @Deprecated
    public static final String UNKNOWN_SSID = "<unknown ssid>";

    @SuppressLint("MissingPermission")
    public static int isWifiEnabled(@NonNull final Context context) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "isWifiEnabled - WifiManager is null!");
            return -1;
        }
        if (wifiManager.isWifiEnabled()) { return 1; }
        else { return 0; }
    }

    @SuppressLint("MissingPermission")
    public static int isWifiEnabled2(@NonNull final Context context) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "isWifiEnabled2 - WifiManager is null!");
            return -1;
        }
        if (wifiManager.getWifiState() == android.net.wifi.WifiManager.WIFI_STATE_ENABLED) { return 1; }
        else { return 0; }
    }

    @SuppressLint("MissingPermission")
    public static void enableWiFi(@NonNull final Activity activity, final int requestCode) {
        final WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "enableWiFi[Activity] - WifiManager is null!");
            return;
        }
        if (wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "enableWiFi[Activity] - Wifi is Enabled now!");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            activity.startActivityForResult(panelIntent, requestCode);
        }
        else { wifiManager.setWifiEnabled(true); }
    }

    @SuppressLint("MissingPermission")
    public static void enableWiFi(@NonNull final Context context, @NonNull final Fragment fragment, final int requestCode) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "enableWiFi[Fragment] - WifiManager is null!");
            return;
        }
        if (wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "enableWiFi[Fragment] - Wifi is Enabled now!");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            fragment.startActivityForResult(panelIntent, requestCode);
        }
        else { wifiManager.setWifiEnabled(true); }
    }


    ///

    @SuppressLint("MissingPermission")
    public static boolean preAndroidMInternetCheck(
            @NonNull ConnectivityManager connectivityManager,
            @NonNull List<Integer> networkTypeList) {
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork) {
//        return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
//                activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            for(final Integer networkType : networkTypeList) {
                if (activeNetwork.getType() == networkType) { return true; }
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    public static boolean postAndroidMInternetCheck(
            @NonNull ConnectivityManager connectivityManager,
            @NonNull List<Integer> networkTypeList) {
        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities connection = connectivityManager.getNetworkCapabilities(network);
        if (null != connection) {
//        return (activeNetwork.type == NetworkCapabilities.TRANSPORT_WIFI ||
//                activeNetwork.type == NetworkCapabilities.TRANSPORT_CELLULAR)
            for(final Integer networkType : networkTypeList) {
                if (connection.hasTransport(networkType)) { return true; }
            }
        }

        return false;
    }

    public static boolean internetCheck(
            @NonNull ConnectivityManager connectivityManager,
            @NonNull List<Integer> networkTypeList) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return postAndroidMInternetCheck(connectivityManager, networkTypeList);
        } else { return preAndroidMInternetCheck(connectivityManager, networkTypeList); }
    }

    ///

    @NonNull
    @SuppressLint("MissingPermission")
    public static String getSsidViaWifiInfo(@NonNull Context context) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getSsidViaWifiInfo - WifiManager is null!");
            return "";
        }
        try {
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            final String ssid = wifiInfo.getSSID();
            LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getSsidViaWifiInfo - SSID: " + ssid + ", NetworkId: " + wifiInfo.getNetworkId() + ", SupplicantState: " + wifiInfo.getSupplicantState());
            switch (wifiInfo.getSupplicantState()) {
                case COMPLETED: {
                    // [start] revision in 2020/11/23
                    //return wifiInfo.getSSID().replace("\"", "");
                    final int firstIndex = ssid.indexOf("\"");
                    final int lastIndex = ssid.lastIndexOf("\"");
                    if (lastIndex > firstIndex) {
                        return ssid.substring(firstIndex + 1, lastIndex);
                    }
                    else { return ssid; }
                    // [end] revision in 2020/11/23
                }
                default: { return wifiInfo.getSSID(); }
            }
        }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getSsidViaWifiInfo - Error on WifiManager.getConnectionInfo()", cause); }
        return "";
    }

    /**
     * The method can only be involved for the case (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
     */
    @NonNull
    @SuppressLint("MissingPermission")
    public static String getSsidViaNetworkInfo(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { return ""; }
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo) {
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getSsidViaNetworkInfo - isConnected: " + networkInfo.isConnected() + ", ExtraInfo: " + networkInfo.getExtraInfo());
                if ( (networkInfo.isConnected()) && (null != networkInfo.getExtraInfo()) ) {
                    return networkInfo.getExtraInfo().replace("\"","");
                }
            }
            else { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getSsidViaNetworkInfo - NetworkInfo is null!"); }
        }
        else { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getSsidViaNetworkInfo - ConnectivityManager is null!"); }

        return "";
    }

    ///

    @SuppressLint("MissingPermission")
    public static int conductWiFiScan(
            @NonNull Activity activity, @NonNull BroadcastReceiver wiFiScanResultReceiver) {
        final WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            Log.e("ConnectivityUtils", "conductWiFiScan - WifiManager is null!");
            return -1;
        }

        if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            Log.e("ConnectivityUtils", "conductWiFiScan - WifiState != WIFI_STATE_ENABLED!");
            return -1;
        }

        final IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        // register WiFi scan results receiver
        activity.registerReceiver(wiFiScanResultReceiver, filter);

        try {
            // start WiFi Scan
            // This method was deprecated in API level 28.
            // The ability for apps to trigger scan requests will be removed in a future release.
            // https://developer.android.com/reference/android/net/wifi/WifiManager#startScan()
            final boolean result = wifiManager.startScan();
            Log.i("ConnectivityUtils", "start WiFi Scan - result: " + result);
            return 1;
        }
        catch (Exception cause) {
            Log.e("ConnectivityUtils", "error on WifiManager.startScan()", cause);
            return 0;
        }
    }

    @NonNull
    @SuppressLint("MissingPermission")
    public static List<ScanResult> getWiFiScanResult(@NonNull Context context) {
        final List<ScanResult> resultList = new ArrayList<>();
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            Log.e("ConnectivityUtils", "getWiFiScanResult - WifiManager is null!");
            return resultList;
        }
        try {
            final List<ScanResult> list = wifiManager.getScanResults();
            if (null != list) {
                Log.d("ConnectivityUtils", "getWiFiScanResult - WifiManager#getScanResults().size: " + list.size());
                resultList.addAll(list);
            }
            else { Log.e("ConnectivityUtils", "getWiFiScanResult - WifiManager#getScanResults() is null!"); }
        }
        catch (Exception cause) { Log.e("ConnectivityUtils", "getWiFiScanResult - error on WifiManager#getScanResults()!"); }

        return resultList;
    }

    ///

    /**
     * The method can only be involved for the case (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
     */
    @Nullable
    @SuppressLint("MissingPermission")
    public static WifiConfiguration connectToSpecifiedRouterIfPossible(
            @NonNull Context context, @NonNull String networkSSID, @NonNull String networkPassword) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - WifiManager is null!");
            return null;
        }
        if (!wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - Wifi is disabled!");
            return null;
        }

        final String wrappedNetworkSSID = String.format("\"%s\"", networkSSID);
        final String wrappedNetworkPasswordSSID = String.format("\"%s\"", networkPassword);
        LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - wrappedNetworkSSID: " + wrappedNetworkSSID + ", wrappedNetworkPasswordSSID: " + wrappedNetworkPasswordSSID);
        final List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();


        for (final WifiConfiguration configuredNetwork : configuredNetworks) {
            LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - configuredNetwork SSID: " + configuredNetwork.SSID);
            if (configuredNetwork.SSID.equals(wrappedNetworkSSID) ) {
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - found configuredNetwork for SSID: " + networkSSID);
                configuredNetwork.preSharedKey = wrappedNetworkPasswordSSID;
                final int networkId = wifiManager.updateNetwork(configuredNetwork);
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - updateNetwork - networkId: " + networkId);
                final boolean result = wifiManager.enableNetwork(configuredNetwork.networkId, true);
                if (result) {
                    LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connect to configuredNetwork for SSID: " + networkSSID);
                    return configuredNetwork;
                }
            }
        }

        final WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = wrappedNetworkSSID;
        wifiConfiguration.preSharedKey = wrappedNetworkPasswordSSID;
        final int networkId = wifiManager.addNetwork(wifiConfiguration);
        if (networkId != -1) {
            LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - addNetwork for SSID: " + networkSSID);
            final boolean result = wifiManager.enableNetwork(networkId, true);
            if (result) {
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - connect to configuredNetwork for SSID: " + networkSSID);
                return wifiConfiguration;
            }
        }
        else { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterIfPossible - fail to addNetwork for SSID: " + networkSSID + " -> networkId: " + networkId); }

        return null;
    }

    @SuppressLint("MissingPermission")
    public static boolean disconnectToWiFi(@NonNull Context context) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "disconnectToWiFi - WifiManager is null!");
            return false;
        }
        if (!wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "disconnectToWiFi - Wifi is disabled!");
            return false;
        }

        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "disconnectToWiFi - current networkId: " + wifiInfo.getNetworkId());
        wifiManager.disableNetwork(wifiInfo.getNetworkId());
        wifiManager.disconnect();
        return true;
    }

    ///

    /**
     * I have tested to involve the method, but it does not work out at all.
     * And I always got the status [WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE]
     * <p>
     * The method can only be involved for the case (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
     */
    @SuppressLint("MissingPermission")
    public static int connectToSpecifiedRouterViaWifiNetworkSuggestion(
            @NonNull Context context, @NonNull String networkSSID, @NonNull String networkPassword) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { return -1; }
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - WifiManager is null!");
            return -1;
        }
        if (!wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - Wifi is disabled!");
            return -1;
        }

//        final WifiNetworkSuggestion suggestion1 = new WifiNetworkSuggestion.Builder()
//                .setSsid(networkSSID)
//                .setWpa2Passphrase(networkPassword)
//                .setIsAppInteractionRequired(true) // Optional (Needs location permission)
//                .build();
//
//        final WifiNetworkSuggestion suggestion2 = new WifiNetworkSuggestion.Builder()
//                .setSsid(networkSSID)
//                .setWpa3Passphrase(networkPassword)
//                .setIsAppInteractionRequired(true) // Optional (Needs location permission)
//                .build();

        final WifiNetworkSuggestion suggestion = new WifiNetworkSuggestion.Builder()
                .setSsid(networkSSID)
                //.setWpa2Passphrase(networkPassword)
                //.setWpa3Passphrase(networkPassword)
                .setIsAppInteractionRequired(false) // Optional (Needs location permission)
                .build();

        final List<WifiNetworkSuggestion> suggestionsList = new ArrayList<>();
//        suggestionsList.add(suggestion1);
//        suggestionsList.add(suggestion2);
        suggestionsList.add(suggestion);

        wifiManager.addNetworkSuggestions(suggestionsList);
        final int status = wifiManager.addNetworkSuggestions(suggestionsList);
        switch (status) {
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL!");
                return 1;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL!");
                return 0;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED!");
                return 0;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE!");
                final int result = wifiManager.removeNetworkSuggestions(suggestionsList);
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - after remove: "  + result);
                return 0;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP!");
                return 0;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID!");
                return 0;
            default:
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID!");
                return -1;
        }
    }

    @SuppressLint("MissingPermission")
    public static int removeSsidSuggestion(
            @NonNull final Context context, @NonNull final String ssid, @NonNull final String pwd) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { return -1; }
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "removeSsidSuggestion - WifiManager is null!");
            return -1;
        }
        final WifiNetworkSuggestion suggestion = new WifiNetworkSuggestion.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(pwd)
                .build();
        final List<WifiNetworkSuggestion> suggestedList = new ArrayList<>();
        suggestedList.add(suggestion);
        try {
            wifiManager.removeNetworkSuggestions(suggestedList);
            return 1;
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", " removeSsidSuggestion - Error on WifiManager.removeNetworkSuggestions()", cause);
            return 0;
        }
    }

    /**
     * The method can only be involved for the case (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
     */
    public static void registerWiFiSuggestionBroadcast(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { return; }
        final IntentFilter intentFilter =
                new IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);
        context.registerReceiver(receiver, intentFilter);
    }

    ///

    @NonNull
    @TargetApi(Build.VERSION_CODES.Q)
    public static WifiNetworkSpecifier getWifiNetworkSpecifier(
            @NonNull String networkSSID, @NonNull String networkPassword, final boolean doesUseWPA3) {
        final WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder()
                .setSsid(networkSSID);
        if (doesUseWPA3) { builder.setWpa3Passphrase(networkPassword); }
        else { builder.setWpa2Passphrase(networkPassword); }
        return builder.build();
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.Q)
    public static NetworkRequest getNetworkRequest(final int requiredTransportType) {
        return new NetworkRequest.Builder()
                .addTransportType(requiredTransportType)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .build();
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.Q)
    public static NetworkRequest getNetworkRequest(
            @NonNull NetworkSpecifier specifier, final int requiredTransportType) {
        return new NetworkRequest.Builder()
                .addTransportType(requiredTransportType)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .setNetworkSpecifier(specifier)
                .build();
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.Q)
    public static NetworkRequest getNetworkRequest(
            @NonNull NetworkSpecifier specifier,
            final int requiredTransportType,
            final boolean doesNeedToExclude,
            final int excludedTransportType,
            final boolean internetIsNotRequired) {
        final NetworkRequest.Builder builder = new NetworkRequest.Builder()
//                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                .removeTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(requiredTransportType)
                .setNetworkSpecifier(specifier);
        if (doesNeedToExclude) { builder.removeTransportType(excludedTransportType); }
        if (internetIsNotRequired) {
            //builder.removeCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        //builder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
        return builder.build();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int connectToSpecifiedRouterViaRequestNetwork(
            @NonNull Context context,
            @NonNull NetworkRequest request,
            @NonNull ConnectivityManager.NetworkCallback callback) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaRequestNetwork - ConnectivityManager is null!");
            return -1;
        }
        try {
            connectivityManager.requestNetwork(request, callback);
            return 1;
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaRequestNetwork - Error on ConnectivityManager#requestNetwork is null!", cause);
            return 0;
        }
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int connectivityManagerRegisterNetworkCallback(
            @NonNull Context context,
            @NonNull NetworkRequest request,
            @NonNull ConnectivityManager.NetworkCallback callback) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerRegisterNetworkCallback - ConnectivityManager is null!");
            return -1;
        }
        try {
            connectivityManager.registerNetworkCallback(request, callback);
            return 1;
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerRegisterNetworkCallback - Error on ConnectivityManager#unregisterNetworkCallback is null!", cause);
            return 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int connectivityManagerUnregisterNetworkCallback(
            @NonNull Context context, @NonNull ConnectivityManager.NetworkCallback callback) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerUnregisterNetworkCallback - ConnectivityManager is null!");
            return -1;
        }
        try {
            connectivityManager.unregisterNetworkCallback(callback);
            return 1;
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerUnregisterNetworkCallback - Error on ConnectivityManager#unregisterNetworkCallback is null!", cause);
            return 0;
        }
    }

    ///

    @SuppressLint("MissingPermission")
    public static void connectivityManagerBindProcessToNetwork(@NonNull final Context context, @Nullable final Network network) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - ConnectivityManager is null!");
            return;
        }

        try {
            connectivityManager.bindProcessToNetwork(network);
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - network is null?: " + (null == network));
        }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - error on ConnectivityManager#bindProcessToNetwork", cause); }
    }

//    public static void connectivityManagerClearProcessBinding(@NonNull Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
//        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (null == connectivityManager) {
//            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerClearProcessBinding - ConnectivityManager is null!");
//            return;
//        }
//        try { connectivityManager.bindProcessToNetwork(null); }
//        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerClearProcessBinding - ConnectivityManager#bindProcessToNetwork", cause); }
//    }

    ///

    @SuppressLint("MissingPermission")
    public static int isNetworkInterestedTransportType(
            @NonNull Context context, @NonNull Network network, final int transportType) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { return -1; }
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "isNetworkInterestedTransportType - ConnectivityManager is null!");
            return -1;
        }
        final NetworkCapabilities connection = connectivityManager.getNetworkCapabilities(network);
        if (null != connection) {
//            if (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { return 1; }
//            if (connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {return 0; }
            if (connection.hasCapability(transportType)) { return 1; }
            else {
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "isNetworkInterestedTransportType - unknown transport type!");
                return 0;
            }
        }
        else {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "isNetworkInterestedTransportType - NetworkCapability is null!");
            return -1;
        }
    }

    ///

    // [start] added in 2020/04/23
    @SuppressLint("MissingPermission")
    @NonNull
    public static Socket createSocketViaFactory(@NonNull final Context context) throws IOException {
        Socket socket = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            final ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connManager) {
                final Network network = connManager.getBoundNetworkForProcess();
                if (null != network) {
                    final NetworkCapabilities connection = connManager.getNetworkCapabilities(network);
                    if (null != connection) {
                        if (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "createSocketViaFactory - network is WIFI");
                        }
                        if (connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "createSocketViaFactory - network is CELLULAR");
                        }
                    }
                    socket = network.getSocketFactory().createSocket();
                    LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "socket created by network.getSocketFactory().createSocket()");
                }
                if (null == socket) {

                }
            }
        }
        if (null == socket) {
            socket = SocketFactory.getDefault().createSocket();
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "socket created by SocketFactory.getDefault().createSocket()");
        }
        return socket;
    }

    public static final class IeSocketFactory {

        @NonNull
        public Socket create(@NonNull final Context context) throws IOException {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (null != connectivityManager) {
                    return createViaNetworkIfPossible(connectivityManager);
                }
                else {
                    LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - connectivityManager is null!!");
                    return createViaDefaultFactory();
                }
            }
            else { return createViaDefaultFactory(); }
        }

        @NonNull
        public Socket createViaDefaultFactory() throws IOException {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - socket created by SocketFactory.getDefault().createSocket()");
            return SocketFactory.getDefault().createSocket();
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.M)
        private Socket createViaNetworkIfPossible(final ConnectivityManager connectivityManager) throws IOException {
            Socket socket = null;
            final Network boundNetwork = connectivityManager.getBoundNetworkForProcess();
            if (null != boundNetwork) {
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - createViaNetworkIfPossible: get bound Network!!");
                socket = createViaNetwork(connectivityManager, boundNetwork);
            }
            if (null != socket) { return socket; }

            final Network activeNetwork = connectivityManager.getActiveNetwork();
            if (null != activeNetwork) {
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - createViaNetworkIfPossible: get Active Network!!");
                socket = createViaNetwork(connectivityManager, activeNetwork);
            }
            if (null != socket) { return socket; }

            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - createViaNetworkIfPossible - Network is unavailable!!");
            return createViaDefaultFactory();
        }

        @SuppressLint("MissingPermission")
        @Nullable
        private Socket createViaNetwork(
                final ConnectivityManager connectivityManager, @NonNull final Network network) throws IOException {
            final NetworkCapabilities connection = connectivityManager.getNetworkCapabilities(network);
            if (null != connection) {
                if (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - createViaNetwork - network is WIFI!!");
                    LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - createViaNetwork - socket created by network.getSocketFactory().createSocket()");
                    return network.getSocketFactory().createSocket();
                }
                if (connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "IeSocketFactory - createViaNetwork - network is CELLULAR!!");
                }
            }
            return null;
        }
    }
    // [end] added in 2020/04/23

    // [start] added in 2020/07/08
    @SuppressLint("MissingPermission")
    @Nullable
    public static DhcpInfo getDhcpInfo(@NonNull final Context context) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getDhcpInfo - WifiManager is null!");
            return null;
        }
        return wifiManager.getDhcpInfo();
    }

    /**
     * https://en.it1352.com/article/3a9d64293f3a4c5fa5d9aeb514c128db.html
     */
    @NonNull
    public static InetAddress integerToIpAddress(final int value) throws java.io.IOException {
        final int reversedValue = Integer.reverseBytes(value);
        final byte[] intToByteArray = BigInteger.valueOf(reversedValue).toByteArray();
        return InetAddress.getByAddress(intToByteArray);
    }

    @Nullable
    public static InterfaceAddress getHostInterfaceAddressFrom(@Nullable final DhcpInfo dhcpInfo) throws java.io.IOException {
        if (null == dhcpInfo) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getHostInterfaceAddressFrom - dhcpInfo is null!!");
            return null;
        }
        final InetAddress hostAddress = integerToIpAddress(dhcpInfo.ipAddress);
        final String hostIpAddress = hostAddress.getHostAddress();
        LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getHostInterfaceAddressFrom - ipAddress - Int: " + dhcpInfo.ipAddress + " to InetAddress: " + hostIpAddress);

        final NetworkInterface hostNetworkInterface = NetworkInterface.getByInetAddress(hostAddress);
        if (null == hostNetworkInterface) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getHostInterfaceAddressFrom - hostNetworkInterface is null!!");
            return null;
        }

        for (final InterfaceAddress netAddress : hostNetworkInterface.getInterfaceAddresses()) {
            final InetAddress iNetAddress = netAddress.getAddress();
            if (null != iNetAddress) {
                final String iNetHostAddress = (null != iNetAddress.getHostAddress()) ? iNetAddress.getHostAddress() : "NULL";
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getHostInterfaceAddressFrom - iNetAddress: " +  iNetHostAddress + ", prefixLength: " + netAddress.getNetworkPrefixLength());
                if (hostIpAddress.equals(iNetHostAddress) ) {
                    return netAddress;
                }
            }
            else {
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getHostInterfaceAddressFrom - iNetAddress is null! - prefixLength: " + netAddress.getNetworkPrefixLength());
            }
        }

        return null;
    }

    /**
     * Reference:
     * [Find netMask on Android device](https://stackoverflow.com/questions/40058690/find-netmask-on-android-device)
     * <p>
     * Android bug:
     * [netmask of WifiManager.getDhcpInfo() is always zero on Android 5.0](https://issuetracker.google.com/issues/37015180)
     */
    @SuppressLint("MissingPermission")
    @Nullable
    public static LinkAddress getLinkAddressFromNetwork(
            @Nullable final NetEssentialData netEssentialData) throws java.io.IOException {
        if (null == netEssentialData) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - netEssentialData is null!");
            return null;
        }
        if (null == netEssentialData.connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - connectivityManager is null!");
            return null;
        }
        if (null == netEssentialData.network) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - network is null!");
            return null;
        }
        if (null == netEssentialData.dhcpInfo) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - dhcpInfo is null!");
            return null;
        }

        LinkProperties linkProperties = null;
        try { linkProperties = netEssentialData.connectivityManager.getLinkProperties(netEssentialData.network); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - error on connectivityManager.getLinkProperties(network)", cause); }
        if (null == linkProperties) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - linkProperties is null!");
            return null;
        }

        final InetAddress hostAddress = integerToIpAddress(netEssentialData.dhcpInfo.ipAddress);
        final String hostIpAddress = hostAddress.getHostAddress();
        LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getHostInterfaceAddressFrom - ipAddress - Int: " + netEssentialData.dhcpInfo.ipAddress + " to InetAddress: " + hostIpAddress);

        for(final LinkAddress linkAddress : linkProperties.getLinkAddresses()) {
            final InetAddress linkHostAddress = linkAddress.getAddress();
            if (null != linkHostAddress) {
                final String linkHostIpAddress = (null != linkHostAddress.getHostAddress()) ? linkHostAddress.getHostAddress() : "NULL";
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getLinkAddressFromNetwork - linkHostAddress: " + linkHostIpAddress + ", prefixLength: " + linkAddress.getPrefixLength());
                if (hostIpAddress.equals(linkHostIpAddress)) {
                    return linkAddress;
                }
            }
            else { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "getLinkAddressFromNetwork - linkAddress.getAddress() return null!, PrefixLength: " + linkAddress.getPrefixLength()); }
        }

        return null;
    }
    // [end] added in 2020/07/08

    // [start] added in 2020/07/15

    public static final class NetEssentialData {
        public final DhcpInfo dhcpInfo;
        public final ConnectivityManager connectivityManager;
        public final Network network;

        public NetEssentialData(
                @NonNull final DhcpInfo dhcpInfo,
                @NonNull final ConnectivityManager connectivityManager,
                @NonNull final Network network) {
            this.dhcpInfo = dhcpInfo;
            this.connectivityManager = connectivityManager;
            this.network = network;
        }
    }

    @NonNull
    public static NetEssentialData verifyEssentialData(
            @NonNull final Context context, @Nullable final Network network) throws IeRuntimeException {
        final DhcpInfo dhcpInfo = ConnectivityUtils.getDhcpInfo(context);
        if (null == dhcpInfo) {
            throw new IeRuntimeException("ConnectivityUtils.getDhcpInfo() returns null!!", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
        }

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            throw new IeRuntimeException("Cannot get null ConnectivityManager instance!!", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
        }

        //final Network network = QWiFiNetworkManager.getInstance().getCurrentNetwork();
        if (null == network) {
            throw new IeRuntimeException("QWiFiNetworkManager.getCurrentNetwork() returns null!!", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
        }

        return new NetEssentialData(dhcpInfo, connectivityManager, network);
    }

    // [end] added in 2020/07/15
}

