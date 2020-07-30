package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public final class IeNetworkStateInfo {

    public final IeNetworkCallbackState callbackState;

    /**
     * The network being used by the device
     */
    @Nullable
    public final Network network;

    /**
     * Network Capabilities
     */
    @Nullable
    public final NetworkCapabilities networkCapabilities;

    /**
     * Link Properties
     */
    @Nullable
    public final LinkProperties linkProperties;

    public IeNetworkStateInfo(
            @NonNull final IeNetworkCallbackState callbackState,
            @Nullable final Network network,
            @Nullable final NetworkCapabilities networkCapabilities,
            @Nullable final LinkProperties linkProperties) {
        this.callbackState = callbackState;
        this.network = network;
        this.networkCapabilities = networkCapabilities;
        this.linkProperties = linkProperties;
    }
}
