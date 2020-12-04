package tw.com.goglobal.project.rxjava2.reactives.wlans;

import android.content.Context;
import android.net.LinkAddress;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.goglobal.project.rxjava2.AbstractRxTask;
import tw.com.goglobal.project.rxjava2.IeRxAbsConverter;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.models.IePair;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.ConnectivityUtils;


public final class SeancfoleyIPAddress {

    public static void main(String[] args) {
//        try { show("192.168.10.0/24"); }
//        catch (AddressStringException cause) { System.out.println("error: " + cause.fillInStackTrace()); }

        try { show("192.168.0.0/22"); }
        catch (AddressStringException cause) { System.out.println("error: " + cause.fillInStackTrace()); }

        try { show("192.168.3.1",22); }
        catch (AddressStringException cause) { System.out.println("error: " + cause.fillInStackTrace()); }

//        try { show("2001:db8:abcd:0012::/64"); }
//        catch (AddressStringException cause) { System.out.println("error: " + cause.fillInStackTrace()); }
    }

    /**
     * [Get all IP addresses from a given IP address and subnet mask](https://stackoverflow.com/questions/26738561/get-all-ip-addresses-from-a-given-ip-address-and-subnet-mask)
     * [seancfoley / IPAddress](https://github.com/seancfoley/IPAddress)
     */
    static void show(@NonNull final String subnet) throws AddressStringException {
        final IPAddressString ipAddressString = new IPAddressString(subnet);
        final IPAddress ipAddress = ipAddressString.toAddress();
        show(ipAddress);
    }

    static void show(@NonNull final String hostIpString, final int cidr) throws AddressStringException {
        final String subnet = hostIpString + "/" + cidr;
        final IPAddressString ipAddressString = new IPAddressString(subnet);
        final IPAddress ipAddress = ipAddressString.toAddress().toPrefixBlock();
        show(ipAddress);
    }

    static void show(@NonNull final IPAddress subnet) {
        final Integer rawPrefixLength = subnet.getNetworkPrefixLength();
        final int prefixLength = (null != rawPrefixLength) ? rawPrefixLength : subnet.getBitCount();
        final IPAddress mask = subnet.getNetwork().getNetworkMask(prefixLength, false);
        final BigInteger count = subnet.getCount();
        System.out.println("Subnet of size " + count + " with prefix length " + prefixLength + " and mask " + mask);
        System.out.println("Subnet ranges from " + subnet.getLower() + " to " + subnet.getUpper());
        final int edgeCount = 3;
        final BigInteger bigInteger256 = BigInteger.valueOf(256);
        if(count.compareTo(bigInteger256) <= 0) {
            iterateAll(subnet, edgeCount);
        } else {
            iterateEdges(subnet, edgeCount);
        }
    }


    /** Iterates through entire subnet, use with caution */
    static void iterateAll(@NonNull final IPAddress subnet, final int edgeCount) {
        final BigInteger count = subnet.getCount();
        final BigInteger bigEdge = BigInteger.valueOf(edgeCount);
        BigInteger currentCount = count;
        int i = 0;
        for(IPAddress ipAddress: subnet.getIterable()) {
            currentCount = currentCount.subtract(BigInteger.ONE);
            if(i < edgeCount) {
                //case 1
                //System.out.println(++i + ": " + ipAddress);
                i = i + 1;
                System.out.println(i + ": " + ipAddress);

                //case 2
//                //System.out.println(i++ + ": " + ipAddress);
//                System.out.println(i + ": " + ipAddress);
//                i = i + 1;
            }
            else if(currentCount.compareTo(bigEdge) < 0) {
                System.out.println(count.subtract(currentCount) + ": " + ipAddress);
            }
            else if(i == edgeCount) {
                System.out.println("...skipping...");
                i++;
            }
        }
    }

    /** Iterates through subnet edges */
    static void iterateEdges(@NonNull final IPAddress subnet, final int edgeCount) {
        for(int increment = 0; increment < edgeCount; increment++) {
            System.out.println((increment + 1) + ": " + subnet.getLower().increment(increment));
        }
        System.out.println("...skipping...");
        final BigInteger count = subnet.getCount();
        for(int decrement = 1 - edgeCount; decrement <= 0; decrement++) {
            System.out.println(count.add(BigInteger.valueOf(decrement)) + ": " + subnet.getUpper().increment(decrement));
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public static final class GetHostIpAddressList extends AbstractRxTask<List<IPAddress>> {

        private final Context context;
        /**
         * 0 for does not exclude anything - the default case;
         * 1 for excluding the Lower;
         * 2 for excluding the Upper;
         * 3 for excluding both the Lower and the Upper.
         */
        private final int excludingFlag;

        public GetHostIpAddressList(@NonNull final Context context) {
            this.context = context;
            this.excludingFlag = 0;
        }

        public GetHostIpAddressList(@NonNull final Context context, final int excludingFlag) {
            this.context = context;
            this.excludingFlag = excludingFlag;
        }

        @Override
        public void runTask() {
            rxDisposeIfPossible();
            setDisposable(
                    Single.just(context)
                            .map(new ContextToIPAddress())
                            .map(new IPAddressToHostIpAddressList(excludingFlag))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ApiDisposableSingleObserver())
            );
        }
    }

    /**
     * Reference:
     * [Find netMask on Android device](https://stackoverflow.com/questions/40058690/find-netmask-on-android-device)
     * <p>
     * Android bug:
     * [netmask of WifiManager.getDhcpInfo() is always zero on Android 5.0](https://issuetracker.google.com/issues/37015180)
     */
    @RequiresApi(Build.VERSION_CODES.N)
    public static final class ContextToIPAddress extends IeRxAbsConverter<Context, IPAddress> {

        @NonNull
        @Override
        protected IPAddress doConversion(@NonNull final Context input) throws IeRuntimeException {

            LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "ContextToIPAddress");

            final ConnectivityUtils.NetEssentialData netEssentialData = ConnectivityUtils.verifyEssentialData(
                    input,
                    RxQWiFiNetworkManager.getInstance().getCurrentNetwork());

            try {
                final InetAddress hostAddress = ConnectivityUtils.integerToIpAddress(netEssentialData.dhcpInfo.ipAddress);
                LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "ContextToIPAddress - ipAddress - Int: " + netEssentialData.dhcpInfo.ipAddress + " to InetAddress: " + hostAddress.getHostAddress());

                final LinkAddress linkAddress = ConnectivityUtils.getLinkAddressFromNetwork(netEssentialData);
                if (null != linkAddress) {
                    final String subnet = linkAddress.getAddress().getHostAddress() + "/" + linkAddress.getPrefixLength();
                    final IPAddressString ipAddressString = new IPAddressString(subnet);
                    return ipAddressString.toAddress().toPrefixBlock();
                }
                else { LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "ContextToIPAddress - getLinkAddressFromNetwork() return null!!"); }

                final InterfaceAddress hostInterfaceAddress = ConnectivityUtils.getHostInterfaceAddressFrom(netEssentialData.dhcpInfo);
                if (null != hostInterfaceAddress) {
                    final String subnet = hostAddress.getHostAddress() + "/" + hostInterfaceAddress.getNetworkPrefixLength();
                    final IPAddressString ipAddressString = new IPAddressString(subnet);
                    return ipAddressString.toAddress().toPrefixBlock();
                }
                else { LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "ContextToIPAddress - getHostInterfaceAddressFrom() return null!!"); }

                throw new IeRuntimeException("ContextToIPAddress - cannot get IPAddress!!", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "ContextToIPAddress - error caught!");
                throw new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
        }
    }

    public static final class ContextToNumberOfHosts extends IeRxAbsConverter<Context, IePair<Integer, Integer>> {

        private final int ipV4;

        public ContextToNumberOfHosts(final int ipV4) { this.ipV4 = ipV4; }

        @NonNull
        @Override
        protected IePair<Integer, Integer> doConversion(@NonNull final Context input) throws IeRuntimeException {

            LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "ContextToNumberOfHosts");

            final ConnectivityUtils.NetEssentialData netEssentialData = ConnectivityUtils.verifyEssentialData(
                    input,
                    RxQWiFiNetworkManager.getInstance().getCurrentNetwork());

            try {
                final InetAddress hostAddress = ConnectivityUtils.integerToIpAddress(netEssentialData.dhcpInfo.ipAddress);
                LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "ContextToNumberOfHosts - ipAddress - Int: " + netEssentialData.dhcpInfo.ipAddress + " to InetAddress: " + hostAddress.getHostAddress());

                final LinkAddress linkAddress = ConnectivityUtils.getLinkAddressFromNetwork(netEssentialData);
                if (null != linkAddress) {
                    return getNumberOfHosts(linkAddress.getPrefixLength(), ipV4);
                }
                else { LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "ContextToNumberOfHosts - getLinkAddressFromNetwork() return null!!"); }

                final InterfaceAddress hostInterfaceAddress = ConnectivityUtils.getHostInterfaceAddressFrom(netEssentialData.dhcpInfo);
                if (null != hostInterfaceAddress) {
                    return getNumberOfHosts(hostInterfaceAddress.getNetworkPrefixLength(), ipV4);
                }
                else { LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "ContextToNumberOfHosts - getHostInterfaceAddressFrom() return null!!"); }

                throw new IeRuntimeException("ContextToNumberOfHosts - cannot get IPAddress!!", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "ContextToNumberOfHosts - error caught!");
                throw new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
        }
    }

    public static IePair<Integer, Integer> getNumberOfHosts(final int prefixLength, final int ipV4) {
        final double hostBits = 32.0d - ((double)prefixLength); // How many bits do we have for the hosts.
        final int netmask = (0xffffffff >> (32 - prefixLength)) << (32 - prefixLength); // How many bits for the netmask.
        final int numberOfHosts = (int) Math.pow(2.0d, hostBits) - 2; // 2 ^ hostbits = number of hosts in integer.
        final int firstAddr = (ipV4 & netmask) + 1; // AND the bits we care about, then first addr.
        LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "ContextToNumberOfHosts - " +
                "getNumberOfHosts {prefixLength: " + prefixLength + ", hostBits: " + hostBits +
                ", netmask: " + netmask + ", firstAddr: " + firstAddr);
        return new IePair<>(numberOfHosts, firstAddr) ;
    }

    public static final class IPAddressToHostIpAddressList extends IeRxAbsConverter<IPAddress, List<IPAddress>> {

        /**
         * 0 for does not exclude anything - the default case;
         * 1 for excluding the Lower;
         * 2 for excluding the Upper;
         * 3 for excluding both the Lower and the Upper.
         */
        private final int excludingFlag;

        public IPAddressToHostIpAddressList() { this.excludingFlag = 0; }

        public IPAddressToHostIpAddressList(final int excludingFlag) { this.excludingFlag = excludingFlag; }

        @NonNull
        @Override
        protected List<IPAddress> doConversion(@NonNull final IPAddress input) throws IeRuntimeException {
            try {
                final String hostMin = input.getLower().toAddressString().getHostAddress().toString();
                final String hostMax = input.getUpper().toAddressString().getHostAddress().toString();
                LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - excludingFlag: " + excludingFlag);
                LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - Host min: " + hostMin);
                LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - Host max: " + hostMax);
                final List<IPAddress> resultList = new ArrayList<>();
                int i = 0;
                for (final IPAddress address : input.getIterable()) {
                    //LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - [" + i + ": " + address.toAddressString().getHostAddress());
                    // only accept the IPv4 IP addresses
                    if ( (address.isIPv4()) && (address.getSegmentCount() == 4) ) {
                        final String hostAddress = address.toAddressString().getHostAddress().toString();
                        switch (excludingFlag) {
                            case 1: {
                                if (!hostMin.equals(hostAddress)) {
                                    resultList.add(address);
                                    showAdditionLog(address, hostAddress, i);
                                }
                                break;
                            }
                            case 2: {
                                if (!hostMax.equals(hostAddress)) {
                                    resultList.add(address);
                                    showAdditionLog(address, hostAddress, i);
                                }
                                break;
                            }
                            case 3: {
                                if ((!hostMin.equals(hostAddress)) && (!hostMax.equals(hostAddress))) {
                                    resultList.add(address);
                                    showAdditionLog(address, hostAddress, i);
                                }
                                break;
                            }
                            default: {
                                resultList.add(address);
                                showAdditionLog(address, hostAddress, i);
                                break;
                            }
                        }
                    }
                    else { LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - Non IPv4: [" + i + ": " + address.toAddressString().getHostAddress().toString() + ", " + address.getSegment(3).toString() + "]"); }

                    i += 1;
                }
                return resultList;
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - error caught!");
                throw new IeRuntimeException(cause, ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
        }

        private void showAdditionLog(@NonNull final IPAddress ipAddress, @NonNull final String hostAddress, final int index) {
            // 5.3.1:
            //LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "getAllHosts - " + i + ": " + hostAddress + ", " + ipAddress.getSegment(3).segmentValue}")
            // 4.3.3:
            //LogWrapper.showLog(Log.INFO, "SeancfoleyIPAddress", "IPAddressToHostIpAddressList - IPv4: [" + index + ": " + hostAddress + ", " + ipAddress.getSegment(3).toString() + "]");
        }
    }
}
