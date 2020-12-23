package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.apis.errors.IeSocketException;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResponse;
import tw.realtime.project.rtbaseframework.delegates.ui.view.IdentifierDelegate;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.ConnectivityUtils;


public final class IeSocketManager {

    private volatile static IeSocketManager instance;

    private final byte[] dataLock = new byte[0];
    private final Map<String, SocketHelper<IdentifierDelegate>> socketInstanceMap = new HashMap<>();

    /** Returns singleton class instance */
    public static IeSocketManager getInstance() {
        if (null == instance) {
            synchronized (IeSocketManager.class) {
                if (null == instance) {
                    instance = new IeSocketManager();
                }
            }
        }
        return instance;
    }

    private IeSocketManager() { }

    private String getLogTag() { return this.getClass().getSimpleName(); }


    public boolean createConnectedSocketIfNeeded(
            @Nullable final IdentifierDelegate item,
            @NonNull final String ipAddress,
            final int portNumber) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "createConnectedSocketIfNeeded - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("createConnectedSocketIfNeeded - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        if (hasItemExisted(item)) { return false; }

        final SocketHelper<IdentifierDelegate> socketHelp = createConnectedSocket(item, ipAddress, portNumber);
        putItemWithSocket(item, socketHelp);
        return true;
    }

    public void putItemWithSocket(
            @Nullable final IdentifierDelegate item,
            @Nullable final SocketHelper<IdentifierDelegate> socketHelper) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "putItemWithSocket - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("putItemWithSocket - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        if (null == socketHelper) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "putItemWithSocket - SocketHelper is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("putItemWithSocket - socketHelper is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        synchronized (dataLock) {
            socketInstanceMap.put(item.theIdentifier(), socketHelper);
            LogWrapper.showLog(Log.INFO, getLogTag(), "putItemWithSocket - add item and socket for ["
                    + item.theIdentifier() + "] " + "[On Thread: " + Thread.currentThread().getName() + "]");
        }
    }

    public boolean hasItemExisted(@Nullable final IdentifierDelegate item) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "hasItemExisted - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("hasItemExisted - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        final boolean result = socketInstanceMap.containsKey(item.theIdentifier());
        LogWrapper.showLog(Log.INFO, getLogTag(), "hasItemExisted - [" + result +
                "] for [" + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
        return result;
    }

    @NonNull
    public SocketHelper<IdentifierDelegate> getConnectedSocket(@Nullable final IdentifierDelegate item) {
        if (null == item) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "getConnectedSocket - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("hasItemExisted - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        final SocketHelper<IdentifierDelegate> socketHelper = socketInstanceMap.get(item.theIdentifier());
        if (null != socketHelper) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "getConnectedSocket - get socket for ["
                    + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            return socketHelper;
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "getConnectedSocket - no socket available for ["
                    + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("getConnectedSocket - no socket available for [" +
                    item.theIdentifier() + "]", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }
    }

    @NonNull
    public static SocketHelper<IdentifierDelegate> createConnectedSocket(
            @Nullable final IdentifierDelegate item,
            @NonNull final String ipAddress,
            final int portNumber) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.WARN, "IeSocketManager", "createConnectedSocket - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("createConnectedSocket - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        try {
            final int connectTimeout = 10000;
            final InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, portNumber);
            final Socket socket = new ConnectivityUtils.IeSocketFactory().createViaDefaultFactory();
            LogWrapper.showLog(Log.INFO, "IeSocketManager", "createConnectedSocket - create a new socket for [" +
                    item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");

            socket.setTcpNoDelay(true);
            socket.setOOBInline(true);
            socket.connect(socketAddress, connectTimeout);
            LogWrapper.showLog(Log.INFO, "IeSocketManager", "createConnectedSocket - socket has connected to [" +
                    ipAddress + " : " + portNumber + "], [On Thread: " + Thread.currentThread().getName() + "]");

            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            LogWrapper.showLog(Log.INFO, "IeSocketManager", "createConnectedSocket - yield bufferedOutputStream for [" +
                    ipAddress + " : " + portNumber + "], [On Thread: " + Thread.currentThread().getName() + "]");

            final BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            LogWrapper.showLog(Log.INFO, "IeSocketManager", "createConnectedSocket - yield bufferedInputStream for [" +
                    ipAddress + " : " + portNumber + "], [On Thread: " + Thread.currentThread().getName() + "]");

            socket.setKeepAlive(true);
            socket.setSendBufferSize(1);
            LogWrapper.showLog(Log.INFO, "IeSocketManager", "createConnectedSocket - done for [" +
                    ipAddress + " : " + portNumber + "], [On Thread: " + Thread.currentThread().getName() + "]");

            return new SocketHelper<>(item, socket, bufferedInputStream, bufferedOutputStream);
        }
        catch (IOException cause) {
            LogWrapper.showLog(Log.WARN, "IeSocketManager", "createConnectedSocket - IOException: [" +
                    cause.getLocalizedMessage() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeSocketException(
                    "getSocketConnected - IOException: [" + cause.getLocalizedMessage() + "]", ErrorCodes.Socket.CREATION_FAILURE);
        }
    }

    public static final class SocketHelper<T extends IdentifierDelegate> {

        private final T theItem;
        private final Socket theSocket;
        private final BufferedInputStream bufferedInputStream;
        private final BufferedOutputStream bufferedOutputStream;

        public SocketHelper(
                @NonNull final T item,
                @NonNull final Socket socket,
                @NonNull final BufferedInputStream bufferedInputStream,
                @NonNull final BufferedOutputStream bufferedOutputStream) {
            this.theItem = item;
            this.theSocket = socket;
            this.bufferedInputStream = bufferedInputStream;
            this.bufferedOutputStream = bufferedOutputStream;
        }

        public void releaseIfNeeded() {
            LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "releaseIfNeeded - for [" +
                    theItem.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            if (null != bufferedInputStream) {
                try { bufferedInputStream.close(); }
                catch (IOException cause) { LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "releaseIfNeeded - error on BufferedInputStream.close(): " + cause.getLocalizedMessage()); }
            }
            if (null != bufferedOutputStream) {
                try {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
                catch (IOException cause) { LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "releaseIfNeeded - error on BufferedOutputStream.close(): " + cause.getLocalizedMessage()); }
            }
            if (null != theSocket) {
                try {
                    theSocket.shutdownInput();
                    theSocket.shutdownOutput();
                    theSocket.close();
                }
                catch (IOException cause) { LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "releaseIfNeeded- error on socket.close(): " + cause.getLocalizedMessage()); }
            }
        }

        public void sendOutDataToRemote(@NonNull final String outData) {
            try {
                bufferedOutputStream.write(outData.getBytes());
                //flush buffer
                bufferedOutputStream.flush();
                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "sendOutDataToRemote ["
                        + outData + "] for [" + theItem.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "error on sendOutDataToRemote ["
                        + outData + "] for [" + theItem.theIdentifier() + "], [On Thread: "
                        + Thread.currentThread().getName() + "], message: " + cause.getLocalizedMessage());
                throw new IeSocketException(cause, ErrorCodes.Socket.FAIL_TO_SEND_OUT_PACKET);
            }
        }

        public static boolean isJSONValid(@NonNull final String json) {
            try {
                final Gson gson = new Gson();
                gson.fromJson(json, Object.class);
                return true;
            }
            catch(com.google.gson.JsonSyntaxException cause) { return false; }
        }

        @NonNull
        public IeApiResponse<String> readDataFromRemote(
                @NonNull final StringBuilder stringBuilder, final int waitingTime, final int retryLimit, final int retryCount) {

            try {
                while(bufferedInputStream.available() > 0) {
                    final char readChar = (char) bufferedInputStream.read();
                    stringBuilder.append(readChar);
                }

                final String json = stringBuilder.toString();
                if (json.isEmpty()) {
                    return readDataFromRemote(stringBuilder, waitingTime, retryLimit, retryCount + 1);
                }

                LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "readDataFromRemote - current json ["
                        + json + "], [On Thread: " + Thread.currentThread().getName() + "]");
                if (isJSONValid(json)) {
                    LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - " +
                            "valid Json OK , no need retry read");
                    return new IeApiResponse<>(json, null);
                }

                try { Thread.sleep(waitingTime); } catch (InterruptedException cause) {}

                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - retryLimit: ["
                        + retryLimit + "], retryCount: [" + retryCount + "], [On Thread: " + Thread.currentThread().getName() + "]");
                if (retryCount + 1 <= retryLimit) {
                    return readDataFromRemote(stringBuilder, waitingTime, retryLimit, retryCount + 1);
                }
                else { return new IeApiResponse<>(json, null); }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "error on readDataFromRemote for ["
                        + theItem.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "], message: " + cause.getLocalizedMessage());
                return new IeApiResponse<>(null, new IeSocketException(cause, ErrorCodes.Socket.FAIL_TO_READ_FROM_OUT_PACKET));
            }
        }
    }

}
