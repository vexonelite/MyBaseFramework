package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface IeNetworkStateInfoDelegate {

    @NonNull IeNetworkCallbackState theCallbackState();

    /**
     * The network being used by the device
     */
    @Nullable Network theNetwork();

    /**
     * Network Capabilities
     */
    @Nullable NetworkCapabilities theNetworkCapabilities();

    /**
     * Link Properties
     */
    @Nullable LinkProperties theLinkProperties();

    final class Impl implements IeNetworkStateInfoDelegate {

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

        public Impl(
                @NonNull final IeNetworkCallbackState callbackState,
                @Nullable final Network network,
                @Nullable final NetworkCapabilities networkCapabilities,
                @Nullable final LinkProperties linkProperties) {
            this.callbackState = callbackState;
            this.network = network;
            this.networkCapabilities = networkCapabilities;
            this.linkProperties = linkProperties;
        }

        @NonNull
        @Override
        public IeNetworkCallbackState theCallbackState() { return this.callbackState; }

        @Nullable
        @Override
        public Network theNetwork() { return this.network; }

        @Nullable
        @Override
        public NetworkCapabilities theNetworkCapabilities() { return this.networkCapabilities; }

        @Nullable
        @Override
        public LinkProperties theLinkProperties() { return this.linkProperties; }
    }
}
