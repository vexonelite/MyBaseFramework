package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import java.util.Comparator;

public final class WiFiAccessPointComparator implements Comparator<WiFiAccessPointDelegate> {
    @Override
    public int compare(WiFiAccessPointDelegate delegate1, WiFiAccessPointDelegate delegate2) {
        final String ssid1 = (null != delegate1) ? delegate1.theSSID() : "";
        final String ssid2 = (null != delegate2) ? delegate2.theSSID() : "";
        return ssid1.compareTo(ssid2);
    }
}
