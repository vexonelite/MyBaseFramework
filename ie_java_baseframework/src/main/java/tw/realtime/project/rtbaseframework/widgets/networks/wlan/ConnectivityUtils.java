package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
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

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class ConnectivityUtils {

    @SuppressLint("MissingPermission")
    public static int isWifiEnabled(@NonNull Context context) {
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "isWifiEnabled - WifiManager is null!");
            return -1;
        }
        if (wifiManager.isWifiEnabled()) { return 1; }
        else { return 0; }
    }

    @SuppressLint("MissingPermission")
    public static void enableWiFi(@NonNull Activity activity, final int requestCode) {
        final WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "enableWiFi - WifiManager is null!");
            return;
        }
        if (wifiManager.isWifiEnabled()) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "enableWiFi - Wifi is Enabled now!");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            activity.startActivityForResult(panelIntent, requestCode);
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
            LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getSsidViaWifiInfo - WifiManager is null!");
            return "";
        }
        try {
            final WifiInfo info = wifiManager.getConnectionInfo();
            LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "getSsidViaWifiInfo - SSID: " + info.getSSID() + ", NetworkId: " + info.getNetworkId() + ", SupplicantState: " + info.getSupplicantState());
            if (info.getSupplicantState().equals(SupplicantState.COMPLETED)) {
                return info.getSSID().replace("\"", "");
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
        final ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connManager) {
            final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
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
//                final int result = wifiManager.removeNetworkSuggestions(suggestionsList);
//                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaWifiNetworkSuggestion - after remove: "  + result);
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
            LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "addNetwork for SSID: " + networkSSID);
            final boolean result = wifiManager.enableNetwork(networkId, true);
            if (result) {
                LogWrapper.showLog(Log.INFO, "ConnectivityUtils", "connect to configuredNetwork for SSID: " + networkSSID);
                return wifiConfiguration;
            }
        }
        else { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "fail to addNetwork for SSID: " + networkSSID + " -> networkId: " + networkId); }

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
    public static NetworkRequest getNetworkRequest(
            @NonNull NetworkSpecifier specifier, final int requiredTransportType) {
        final NetworkRequest.Builder builder = new NetworkRequest.Builder()
                .addTransportType(requiredTransportType)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .setNetworkSpecifier(specifier);
        return builder.build();
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
        if (internetIsNotRequired) { builder.removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET); }
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int connectivityManagerUnregisterNetworkCallback(
            @NonNull Context context, @NonNull ConnectivityManager.NetworkCallback callback) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaRequestNetwork - ConnectivityManager is null!");
            return -1;
        }
        try {
            connectivityManager.unregisterNetworkCallback(callback);
            return 1;
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectToSpecifiedRouterViaRequestNetwork - Error on ConnectivityManager#unregisterNetworkCallback is null!", cause);
            return 0;
        }
    }

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
                LogWrapper.showLog(Log.ERROR, "ConnectivityManager ktx", "unknown transport type!");
                return 0;
            }
        }
        else {
            LogWrapper.showLog(Log.ERROR, "ConnectivityManager ktx", "NetworkCapability is null!");
            return -1;
        }
    }

    ///

    @SuppressLint("MissingPermission")
    public static void connectivityManagerBindProcessToNetwork(@NonNull Context context, @Nullable Network network) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - ConnectivityManager is null!");
            return;
        }

        try { connectivityManager.bindProcessToNetwork(network); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "bindProcessToNetwork - ConnectivityManager#bindProcessToNetwork", cause); }
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

}

