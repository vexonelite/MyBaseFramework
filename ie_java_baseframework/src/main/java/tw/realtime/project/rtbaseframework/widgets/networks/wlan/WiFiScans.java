package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.net.wifi.ScanResult;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;
import tw.realtime.project.rtbaseframework.apis.filters.IFilterFunction;
import tw.realtime.project.rtbaseframework.delegates.ui.view.CellTypeDelegate;
import tw.realtime.project.rtbaseframework.delegates.ui.view.IdentifierDelegate;
import tw.realtime.project.rtbaseframework.utils.CryptUtils;


public final class WiFiScans {

    public interface Delegate extends IdentifierDelegate, CellTypeDelegate, WiFiSsidDelegate {
        @NonNull
        String theStrength();

        @NonNull
        String theCapabilities();
    }

    public static final class AccessPointImpl implements Delegate {

        private final String identifier;
        private final int cellType;
        private final String ssid;
        private final String bSSID;
        private final String strength;
        private final String capabilities;

        public AccessPointImpl(
                @NonNull final String identifier,
                final int cellType,
                @NonNull final String ssid,
                @NonNull final String bSSID,
                @NonNull final String strength,
                @NonNull final String capabilities) {
            this.identifier = (identifier.isEmpty()) ? CryptUtils.generateRandomStringViaUuid() : identifier;
            this.cellType = cellType;
            this.ssid = ssid;
            this.bSSID = bSSID;
            this.strength = strength;
            this.capabilities = capabilities;
        }

        @Override
        public String toString() {
            return "WiFiAccessPointImpl { SSID: " + ssid + ", BSSID: " + bSSID + ", Strength: " + strength + ", Capabilities: " + capabilities + " }";
        }

        @NonNull
        @Override
        public String theIdentifier() { return identifier; }

        @Override
        public int theCellType() { return cellType; }

        @NonNull
        @Override
        public String theSSID() { return ssid; }

        @NonNull
        @Override
        public String theBSSID() { return bSSID; }

        @NonNull
        @Override
        public String theStrength() { return strength; }

        @NonNull
        @Override
        public String theCapabilities() { return capabilities; }

    }

    @NonNull
    public static List<Delegate> getDefaultListByCellType(final int cellType) {
        final Delegate delegate = new AccessPointImpl(
                "", cellType, "", "", "", "");
        final List<Delegate> list = new ArrayList<>();
        list.add(delegate);
        return list;
    }

    ///

    public static final class DiffCallback extends DiffUtil.ItemCallback<Delegate> {

        @Override
        public boolean areItemsTheSame(@NonNull final Delegate oldItem, @NonNull final Delegate newItem) {
            return oldItem.theIdentifier().equals(newItem.theIdentifier());
        }

        @Override
        public boolean areContentsTheSame(@NonNull final Delegate oldItem, @NonNull final Delegate newItem) {
            return  (oldItem.theSSID().equals(newItem.theSSID()) ) &&
                    (oldItem.theBSSID().equals(newItem.theBSSID()) );
        }
    }

    ///

    public static final class WiFiAccessPointComparator implements Comparator<Delegate> {
        @Override
        public int compare(final Delegate delegate1, final Delegate delegate2) {
            final String ssid1 = (null != delegate1) ? delegate1.theSSID() : "";
            final String ssid2 = (null != delegate2) ? delegate2.theSSID() : "";
            return ssid1.compareTo(ssid2);
        }
    }

    ///

    public static final class Converter extends IeBaseConverter<List<ScanResult>, List<Delegate>> {

        private final int cellType;

        public Converter(final int cellType) { this.cellType = cellType; }

        @NonNull
        @Override
        protected List<Delegate> doConversion(@NonNull final List<ScanResult> input) throws Exception {
            final List<Delegate> wiFiApList = new ArrayList<>();
            for (final ScanResult scanResult : input) {
                final Delegate wifiAccessPoint = new AccessPointImpl(
                        "",
                        cellType,
                        scanResult.SSID,
                        scanResult.BSSID,
                        "" + scanResult.level,
                        scanResult.capabilities);
                wiFiApList.add(wifiAccessPoint);
            }
            LogWrapper.showLog(Log.INFO, "WiFiScans", getLogTag() + " - scanResult.size: " + input.size() + ", wiFiApList.size: " + wiFiApList.size() + " on Thread: " + Thread.currentThread().getName());
            return wiFiApList;
        }
    }

    public static final class ConverterWithFilter extends IeBaseConverter<List<ScanResult>, List<Delegate>> {

        private final int cellType;
        private final IFilterFunction<Delegate> filter;

        public ConverterWithFilter(final int cellType, @NonNull final IFilterFunction<Delegate> filter) {
            this.cellType = cellType;
            this.filter = filter;
        }

        @NonNull
        @Override
        protected List<Delegate> doConversion(@NonNull List<ScanResult> input) throws Exception {
            final List<Delegate> wiFiApList = new ArrayList<>();
            for (final ScanResult scanResult : input) {
                final Delegate wifiAccessPoint = new AccessPointImpl(
                        "",
                        cellType,
                        scanResult.SSID,
                        scanResult.BSSID,
                        "" + scanResult.level,
                        scanResult.capabilities);
                if (filter.predicate(wifiAccessPoint)) { wiFiApList.add(wifiAccessPoint); }
            }
            LogWrapper.showLog(Log.INFO, "WiFiScans", getLogTag() + " - scanResult.size: " + input.size() + ", wiFiApList.size: " + wiFiApList.size() + " on Thread: " + Thread.currentThread().getName());
            return wiFiApList;
        }
    }
}
