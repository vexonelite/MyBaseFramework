package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.wifi.ScanResult;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;
import tw.realtime.project.rtbaseframework.apis.filters.IFilterFunction;


public final class WiFiScanResultConverterWithFilter
        extends IeBaseConverter<List<ScanResult>, List<WiFiAccessPointDelegate>> {

    private final int cellType;
    private final IFilterFunction<WiFiAccessPointDelegate> filter;

    public WiFiScanResultConverterWithFilter(
            final int cellType, @NonNull IFilterFunction<WiFiAccessPointDelegate> filter) {
        this.cellType = cellType;
        this.filter = filter;
    }

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
            if (filter.predicate(wifiAccessPoint)) { wiFiApList.add(wifiAccessPoint); }
        }
        return wiFiApList;
    }
}
