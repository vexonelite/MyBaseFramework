package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.Network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tw.realtime.project.rtbaseframework.enumerations.WiFiState;


public final class WiFiNetworkEvent {
    @Nullable
    public final Network network;

    @NonNull
    public final WiFiState wiFiState;

    public WiFiNetworkEvent(@Nullable Network network, @NonNull WiFiState wiFiState) {
        this.network = network;
        this.wiFiState = wiFiState;
    }
}
