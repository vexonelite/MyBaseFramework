package tw.com.goglobal.project.rxjava2.reactives.wlans;

import android.content.Context;
import android.net.DhcpInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.SocketException;

import inet.ipaddr.IPAddress;
import tw.com.goglobal.project.rxjava2.AppRxTask;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.apis.errors.IeSocketException;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResponse;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.ConnectivityUtils;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.SocketDefinitions;


/**
 * Created in 2020/12/04
 */
public class SocketTasks {

    public static final class CreateUdpSocketTask extends AppRxTask.WithRxIo<IeApiResponse<DatagramSocket>> {

        private final SocketDefinitions.UdpConfiguration udpConfiguration;

        public CreateUdpSocketTask(@NonNull final SocketDefinitions.UdpConfiguration udpConfiguration) {
            this.udpConfiguration = udpConfiguration;
        }

        @NonNull
        @Override
        public IeApiResponse<DatagramSocket> call() throws IeRuntimeException {
            try {
                final DatagramSocket udpSocket = this.udpConfiguration.createUdpSocket();
                return new IeApiResponse<>(udpSocket, null);
            } catch (IeSocketException cause) {
                LogWrapper.showLog(Log.ERROR, "SocketTasks_CreateUdpSocketTask", "error on DatagramSocket.close(): " + cause.getLocalizedMessage());
                return new IeApiResponse<>(null, cause);
            }
        }
    }

    public static final class CloseUdpSocketTask extends AppRxTask.WithRxIo<IeApiResponse<Boolean>> {

        private final DatagramSocket udpSocket;

        public CloseUdpSocketTask(@NonNull final DatagramSocket udpSocket) {
            this.udpSocket = udpSocket;
        }

        @NonNull
        @Override
        public IeApiResponse<Boolean> call() throws IeRuntimeException {
            try {
                udpSocket.close();
                return new IeApiResponse<>(true, null);
            } catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "SocketTasks_CloseUdpSocketTask", "error on DatagramSocket.close(): " + cause.getLocalizedMessage());
                return new IeApiResponse<>(false, new IeSocketException(cause, ErrorCodes.Socket.CLOSE_FAILURE));
            }
        }
    }

    public static final class SendUdpBroadcast extends AppRxTask.WithRxIo<IeApiResponse<Boolean>> {

        private final Context context;
        private final SocketDefinitions.UdpConfiguration udpConfig;
        private final DatagramSocket udpSocket;
        private final String request;

        public SendUdpBroadcast(
                @NonNull final Context context,
                @NonNull final SocketDefinitions.UdpConfiguration udpConfig,
                @NonNull final DatagramSocket udpSocket,
                @NonNull final String request) {
            this.context = context;
            this.udpConfig = udpConfig;
            this.udpSocket = udpSocket;
            this.request = request;
        }

        @NonNull
        @Override
        public IeApiResponse<Boolean> call() throws IeRuntimeException {
            final DhcpInfo dhcpInfo = ConnectivityUtils.getDhcpInfo(context);
            if (null == dhcpInfo) {
                final IeRuntimeException error = new IeRuntimeException("cannot get a DhcpInfo object (null)!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
                return new IeApiResponse<>(false, error);
            }

            // [start] revision by elite_lin - 2021/05/28
            // version 1
//            final IPAddress netIPAddress;
//            try {
//                netIPAddress = new SeancfoleyIPAddress.ContextToIPAddress().convertIntoData(context);
//            } catch (IeRuntimeException cause) {
//                LogWrapper.showLog(Log.ERROR, getLogTag(), "error on getAllHostsViaDhcpInfo: " + cause.getLocalizedMessage());
//                return new IeApiResponse<>(false, cause);
//            }
//
//            final InetAddress broadcastAddress;
//            try {
//                final String hostMax = netIPAddress.getUpper().toAddressString().getHostAddress().toString();
//                broadcastAddress = InetAddress.getByName(hostMax);
//            } catch (Exception cause) {
//                LogWrapper.showLog(Log.ERROR, getLogTag(), "error on InetAddress.getByName(): " + cause.getLocalizedMessage());
//                final IeRuntimeException error = new IeRuntimeException("error on InetAddress.getByName()!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
//                return new IeApiResponse<>(false, error);
//            }

            // version 2
            final InetAddress broadcastAddress;
            try {
                final InterfaceAddress hostInterfaceAddress = ConnectivityUtils.getHostInterfaceAddressFrom(dhcpInfo);
                if (null != hostInterfaceAddress) {
                    broadcastAddress = hostInterfaceAddress.getBroadcast();
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "SendUdpBroadcast - broadcastAddress: [" + broadcastAddress + "]");
                }
                else {
                    LogWrapper.showLog(Log.ERROR, getLogTag(), "SendUdpBroadcast - ConnectivityUtils.getHostInterfaceAddressFrom() returns null!!");
                    final IeRuntimeException error = new IeRuntimeException("ConnectivityUtils.getHostInterfaceAddressFrom() returns null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
                    return new IeApiResponse<>(false, error);
                }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "SendUdpBroadcast - Error on ConnectivityUtils.getHostInterfaceAddressFrom()!!");
                final IeRuntimeException error = new IeRuntimeException("Error on ConnectivityUtils.getHostInterfaceAddressFrom()!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
                return new IeApiResponse<>(false, error);
            }

            // [end] revision by elite_lin - 2021/05/28

            try {
                LogWrapper.showLog(Log.INFO, getLogTag(), "SendUdpBroadcast - request: [" + request + "], request.length(): [" + request.length() + "], broadcastAddress: [" + broadcastAddress + "], port #: " + udpSocket.getPort());

                final DatagramPacket udpPacket = new DatagramPacket(request.getBytes(), request.length(), broadcastAddress, udpConfig.portNumber);
                udpSocket.send(udpPacket);
                return new IeApiResponse<>(true, null);
            } catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "SendUdpBroadcast - error on DatagramSocket.send(): " + cause.getLocalizedMessage());
                final IeSocketException error = new IeSocketException("error on DatagramSocket.send()!!", ErrorCodes.Socket.FAIL_TO_SEND_OUT_PACKET);
                return new IeApiResponse<>(false, error);
            }
        }
    }
}
