package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.wifi.ScanResult;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;

public final class WiFiScanResultConverter
        extends IeBaseConverter<List<ScanResult>, List<WiFiAccessPointDelegate>> {

    private final int cellType;

    public WiFiScanResultConverter(final int cellType) { this.cellType = cellType; }

    @NonNull
    @Override
    protected List<WiFiAccessPointDelegate> doConversion(@NonNull List<ScanResult> input) throws Exception {
        final List<WiFiAccessPointDelegate> wiFiApList = new ArrayList<>();
        for (final ScanResult scanResult : input) {
            final WiFiAccessPointDelegate wifiAccessPoint = new WiFiAccessPointImpl(
                    "",
                    cellType,
                    scanResult.SSID,
                    scanResult.BSSID,
                    "" + scanResult.level,
                    scanResult.capabilities);
            wiFiApList.add(wifiAccessPoint);
        }
        return wiFiApList;
    }
}
