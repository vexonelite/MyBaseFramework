package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.delegates.ui.view.CellTypeDelegate;
import tw.realtime.project.rtbaseframework.delegates.ui.view.IdentifierDelegate;

public interface WiFiAccessPointDelegate extends IdentifierDelegate, CellTypeDelegate, WiFiSsidDelegate {
    @NonNull
    String theStrength();

    @NonNull
    String theCapabilities();
}
