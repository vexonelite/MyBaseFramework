package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;


public interface IeNetworkDelegate {

    void onLost(@NonNull Network network);

    //void onUnavailable();

    //void onLosing(@NonNull Network network, int maxMsToLive);

    void onAvailable(@NonNull Network network);

    void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities);

    //void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties);
}
