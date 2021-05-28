package tw.realtime.project.rtbaseframework.widgets.networks.wlan;

import android.util.Log;

import androidx.annotation.NonNull;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeSocketException;


/**
 * Created in 2020/12/04
 */
public class SocketDefinitions {

    public interface Parameters {
        /** 2 Seconds */
        int CONNECTION_TIMEOUT = 2000;
        /** 2 Seconds */
        int READ_TIMEOUT = 2000;
        /** 2 Seconds */
        int WRITE_TIMEOUT = 2000;
        /** 50 MilliSeconds */
        long READ_DELAY = 50;
        /** 20 MilliSeconds */
        long WRITE_DELAY = 20;
        /** 64 Bytes */
        int READ_BUFFER_SIZE = 64;
        /** around 1.5KB (Kilobytes) */
        int WIFI_SCAN_BUFFER_SIZE = 1600;
        /** 15 Seconds */
        int WIFI_SCAN_READ_TIMEOUT = 15 * 1000;
        /** 50 MilliSeconds */
        long SELECTOR_TIME_OUT = 1000;
        /** 1024 Bytes */
        int UDP_BUFFER_SIZE = 1024;
    }

    public static final class UdpConfiguration {

        public final int bufferSize;//  SocketParameters.UDP_BUFFER_SIZE,
        public final int portNumber;
        public final int timeout;
        public final boolean broadcastFlag; //  false

        public UdpConfiguration(final int bufferSize,
                                final int portNumber,
                                final int timeout,
                                final boolean broadcastFlag) {
            this.bufferSize = bufferSize;
            this.portNumber = portNumber;
            this.timeout = timeout;
            this.broadcastFlag = broadcastFlag;
        }

        public UdpConfiguration(final int portNumber) {
            this(Parameters.UDP_BUFFER_SIZE, portNumber, 0, false);
        }

        public UdpConfiguration(final int portNumber, final boolean broadcastFlag) {
            this(Parameters.UDP_BUFFER_SIZE, portNumber, 0, broadcastFlag);
        }

        @NonNull
        public DatagramSocket createUdpSocket() throws IeSocketException {
            try {
                // [reference](https://www.itread01.com/p/1350249.html)
                // [start] revision in 2020/12/08
//                final DatagramSocket socket = new DatagramSocket(this.portNumber);
//                socket.setSoTimeout(this.timeout);
//                socket.setBroadcast(this.broadcastFlag);

                final DatagramSocket socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.setBroadcast(this.broadcastFlag);
                socket.bind(new InetSocketAddress(this.portNumber));
                LogWrapper.showLog(Log.INFO, "SocketDefinitions_UdpConfiguration", "createUdpSocket [*** Done ***]");
                // [end] revision in 2020/12/08
                return socket;
            }
            catch (SocketException cause) {
                LogWrapper.showLog(Log.ERROR, "SocketDefinitions_UdpConfiguration", "error on createUdpSocket: " + cause.getLocalizedMessage());
                throw new IeSocketException(cause, ErrorCodes.Socket.CREATION_FAILURE);
            }
        }
    }

    /** Indicate the state of a {@link DatagramSocket} */
    public enum State {
        DEFAULT,
        CREATED,
        CLOSED
    }
}
