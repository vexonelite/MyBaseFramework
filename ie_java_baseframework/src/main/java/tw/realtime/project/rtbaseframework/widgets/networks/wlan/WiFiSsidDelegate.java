package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import androidx.annotation.NonNull;

public interface WiFiSsidDelegate {
    @NonNull
    String theSSID();

    @NonNull
    String theBSSID();
}
