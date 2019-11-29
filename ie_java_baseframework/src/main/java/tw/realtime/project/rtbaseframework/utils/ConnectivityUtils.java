package tw.realtime.project.rtbaseframework.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class ConnectivityUtils {

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
    public static void connectivityManagerBindProcessToNetwork(@NonNull Context context, @NonNull Network network) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
        final ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - ConnectivityManager is null!");
            return;
        }

        final NetworkCapabilities connection = connManager.getNetworkCapabilities(network);
        if (null != connection) {
            if (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - WIFI");
            }
            if (connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerBindProcessToNetwork - CELLULAR");
            }
        }

        try { connManager.bindProcessToNetwork(network); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "bindProcessToNetwork - ConnectivityManager#bindProcessToNetwork", cause); }
    }

    public static void connectivityManagerClearProcessBinding(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
        final ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connManager) {
            LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerClearProcessBinding - ConnectivityManager is null!");
            return;
        }
        try { connManager.bindProcessToNetwork(null); }
        catch (Exception cause) { LogWrapper.showLog(Log.ERROR, "ConnectivityUtils", "connectivityManagerClearProcessBinding - ConnectivityManager#bindProcessToNetwork", cause); }
    }
}

