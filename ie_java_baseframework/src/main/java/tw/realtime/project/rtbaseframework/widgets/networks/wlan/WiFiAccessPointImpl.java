package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.utils.CryptUtils;


public final class WiFiAccessPointImpl implements WiFiAccessPointDelegate {

    private final String identifier;
    private final int cellType;
    private final String ssid;
    private final String bSSID;
    private final String strength;
    private final String capabilities;

    public WiFiAccessPointImpl(
            @NonNull String identifier,
            final int cellType,
            @NonNull String ssid,
            @NonNull String bSSID,
            @NonNull String strength,
            @NonNull String capabilities) {
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
