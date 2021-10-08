package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private final byte[] executorLock = new byte[0];
    private final Map<String, SocketHelper<IdentifierDelegate>> socketInstanceMap = new HashMap<>();
    private final Map<String, ExecutorService> executorServiceMap = new HashMap<>();

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


    public void shutDownAllExecutors() {
        synchronized (executorLock) {
            final Set<String> keySet = executorServiceMap.keySet();
            for(final String theKey : keySet) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "shutDownAllExecutors - for: [" + theKey + "]");
                final ExecutorService executorService = executorServiceMap.get(theKey);
                shutDownExecutor(executorService);
            }
            executorServiceMap.clear();
        }
    }

    public void shutDownExecutor(@Nullable final ExecutorService executorService) {
        if (null == executorService) { return; }
        try {
            LogWrapper.showLog(Log.INFO, getLogTag(), "shutDownExecutor - attempt to shutdown ExecutorService!!");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "shutDownExecutor - shutdown tasks interrupted!!");
        }
        finally {
            if (!executorService.isTerminated()) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "shutDownExecutor - cancel non-finished tasks!!");
            }
            executorService.shutdownNow();
            LogWrapper.showLog(Log.ERROR, getLogTag(), "shutDownExecutor - shutdown finished!!");
        }
    }

    ///

    public boolean createExecutorServiceIfNeeded(@Nullable final IdentifierDelegate item) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "createExecutorServiceIfNeeded - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("createExecutorServiceIfNeeded - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        if (hasExecutorServiceExistedForItem(item)) { return false; }

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        putItemWithExecutorService(item, executorService);
        return true;
    }

    public void putItemWithExecutorService(
            @Nullable final IdentifierDelegate item,
            @Nullable final ExecutorService executorService) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "putItemWithExecutorService - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("putItemWithExecutorService - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        if (null == executorService) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "putItemWithExecutorService - executorService is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("putItemWithExecutorService - executorService is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        synchronized (executorLock) {
            executorServiceMap.put(item.theIdentifier(), executorService);
            LogWrapper.showLog(Log.INFO, getLogTag(), "putItemWithExecutorService - add item and executorService for ["
                    + item.theIdentifier() + "] " + "[On Thread: " + Thread.currentThread().getName() + "]");
        }
    }

    // [start] added by elite_lin - 2021/10/08
    public void removeExecutorServiceForItemIfPossible(@Nullable final IdentifierDelegate item) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "removeExecutorServiceForItem - item is null!! [On Thread: [" + Thread.currentThread().getName() + "]");
            return;
        }

        if (!hasExecutorServiceExistedForItem(item)) { return; }

        synchronized (executorLock) {
            final ExecutorService executorService = executorServiceMap.remove(item.theIdentifier());
            if (null != executorService) {
                shutDownExecutor(executorService);
            }
            LogWrapper.showLog(Log.ERROR, getLogTag(), "removeExecutorServiceForItem - done!!");
        }
    }
    // [end] added by elite_lin - 2021/10/08

    public boolean hasExecutorServiceExistedForItem(@Nullable final IdentifierDelegate item) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "hasExecutorServiceExistedForItem - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("hasExecutorServiceExistedForItem - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        final boolean result = executorServiceMap.containsKey(item.theIdentifier());
        LogWrapper.showLog(Log.INFO, getLogTag(), "hasExecutorServiceExistedForItem - [" + result +
                "] for [" + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
        return result;
    }

    @NonNull
    public ExecutorService getExecutorService(@Nullable final IdentifierDelegate item) {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getExecutorService - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("getExecutorService - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        final ExecutorService executorService = executorServiceMap.get(item.theIdentifier());
        if (null != executorService) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "getExecutorService - get socket for ["
                    + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            return executorService;
        }
        else {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getExecutorService - no socket available for ["
                    + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("getExecutorService - no socket available for [" +
                    item.theIdentifier() + "]", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }
    }

    ///

    public boolean createConnectedSocketIfNeeded(
            @Nullable final IdentifierDelegate item,
            @NonNull final String ipAddress,
            final int portNumber) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "createConnectedSocketIfNeeded - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("createConnectedSocketIfNeeded - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        if (hasItemExisted(item)) { return false; }

        final SocketHelper<IdentifierDelegate> socketHelp = new SocketHelperFactory().create(item, ipAddress, portNumber);
        putItemWithSocket(item, socketHelp);
        return true;
    }

    public void putItemWithSocket(
            @Nullable final IdentifierDelegate item,
            @Nullable final SocketHelper<IdentifierDelegate> socketHelper) throws IeRuntimeException {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "putItemWithSocket - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("putItemWithSocket - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        if (null == socketHelper) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "putItemWithSocket - SocketHelper is null!! " +
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
            LogWrapper.showLog(Log.ERROR, getLogTag(), "hasItemExisted - item is null!! " +
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
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getConnectedSocket - item is null!! " +
                    "[On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("getConnectedSocket - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }

        final SocketHelper<IdentifierDelegate> socketHelper = socketInstanceMap.get(item.theIdentifier());
        if (null != socketHelper) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "getConnectedSocket - get socket for ["
                    + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            return socketHelper;
        }
        else {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "getConnectedSocket - no socket available for ["
                    + item.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "]");
            throw new IeRuntimeException("getConnectedSocket - no socket available for [" +
                    item.theIdentifier() + "]", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
        }
    }

    @Nullable
    public SocketHelper<IdentifierDelegate> removeSocketInstanceFromMap(@Nullable final IdentifierDelegate item) {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "removeSocketInstanceFromMap - item is null!!");
            return null;
        }
        return socketInstanceMap.remove(item.theIdentifier());
    }

    public void destroySocketWith(@Nullable final IdentifierDelegate item) {
        if (null == item) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "destroySocketWith - item is null!!");
            return;
        }

        final SocketHelper<IdentifierDelegate> socketHelper = removeSocketInstanceFromMap(item);
        if (null != socketHelper) {
            socketHelper.releaseIfNeeded();
            LogWrapper.showLog(Log.INFO, getLogTag(), "destroySocketWith - for: [" + item.theIdentifier() + "]");
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "destroySocketWith - socketHelper is null for: [" + item.theIdentifier() + "]");
        }
    }

    public void destroyAllSockets() {
        synchronized (dataLock) {
            final Set<String> keySet = socketInstanceMap.keySet();
            for(final String theKey : keySet) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "destroyAllSockets - for: [" + theKey + "]");
                final SocketHelper<IdentifierDelegate> socketHelper = socketInstanceMap.get(theKey);
                socketHelper.releaseIfNeeded();
            }
            socketInstanceMap.clear();
        }
    }

    ///

    public static final class SocketHelperFactory {

        @NonNull
        public SocketHelper<IdentifierDelegate> create(
                @Nullable final IdentifierDelegate item,
                @NonNull final String ipAddress,
                final int portNumber) throws IeRuntimeException {
            return this.create(item, ipAddress, portNumber, 10000);
        }

        @NonNull
        public SocketHelper<IdentifierDelegate> create(
                @Nullable final IdentifierDelegate item,
                @NonNull final String ipAddress,
                final int portNumber,
                final int connectTimeout) throws IeRuntimeException {

            if (null == item) {
                LogWrapper.showLog(Log.ERROR, "IeSocketManager", "createConnectedSocket - item is null!! " +
                        "[On Thread: " + Thread.currentThread().getName() + "]");
                throw new IeRuntimeException("createConnectedSocket - item is null!!", ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
            }

            try {
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
                LogWrapper.showLog(Log.ERROR, "IeSocketManager", "createConnectedSocket - IOException: [" +
                        cause.getLocalizedMessage() + "], [On Thread: " + Thread.currentThread().getName() + "]");
                throw new IeSocketException(
                        "getSocketConnected - IOException: [" + cause.getLocalizedMessage() + "]", ErrorCodes.Socket.CREATION_FAILURE);
            }
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
//            LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - retryCount: [" + retryCount + "], [On Thread: " + Thread.currentThread().getName() + "]");

            try { Thread.sleep(waitingTime); } catch (InterruptedException cause) {}

            try {
                final int incomingSize = bufferedInputStream.available();
//                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - incomingSize ["
//                        + incomingSize + "], [On Thread: " + Thread.currentThread().getName() + "]");
                if (incomingSize <= 0) {
                    if (retryCount + 1 <= retryLimit) {
                        return readDataFromRemote(stringBuilder, waitingTime, retryLimit, retryCount + 1);
                    }
                    else {
                        return readDataFromRemote(stringBuilder, waitingTime, retryLimit, retryCount + 1);
                    }
                }

                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - " +
                        "retryCount: [" + retryCount + "], incomingSize [" + incomingSize + "], [On Thread: " + Thread.currentThread().getName() + "]");

                //final byte[] buffer = new byte[8196];
                final byte[] buffer = new byte[incomingSize];
                // This method blocks until some input is available!!
                final int numberOfReadBytes = bufferedInputStream.read(buffer);
                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - numberOfReadBytes ["
                        + numberOfReadBytes + "], [On Thread: " + Thread.currentThread().getName() + "]");
                if (numberOfReadBytes > 0) {
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    baos.write(buffer,0, numberOfReadBytes);
                    final byte[] dataArray = baos.toByteArray();
                    final String readStr = new String(dataArray, 0, dataArray.length, StandardCharsets.UTF_8);
                    LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - readStr ["
                            + readStr + "], [On Thread: " + Thread.currentThread().getName() + "]");
                    stringBuilder.append(readStr);
                }

                final String json = stringBuilder.toString();
                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - current json ["
                        + json + "], [On Thread: " + Thread.currentThread().getName() + "]");
                if (isJSONValid(json.trim())) {
                    LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - " +
                            "valid Json OK , no need retry read");
                    return new IeApiResponse<>(json, null);
                }

                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - retryLimit: ["
                        + retryLimit + "], retryCount: [" + retryCount + "], [On Thread: " + Thread.currentThread().getName() + "]");

                if (retryCount + 1 <= retryLimit) {
                    LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - retry -> readDataFromRemote");
                    return readDataFromRemote(stringBuilder, waitingTime, retryLimit, retryCount + 1);
                }
                else {
                    LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataFromRemote - return");
                    return new IeApiResponse<>(json, null);
                }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "error on readDataFromRemote for ["
                        + theItem.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "], message: " + cause.getLocalizedMessage());
                return new IeApiResponse<>(null, new IeSocketException(cause, ErrorCodes.Socket.FAIL_TO_READ_FROM_OUT_PACKET));
            }
        }

        public void readDataAndNoReturn() {
            try {
                final byte[] buffer = new byte[8196];
                final int numberOfReadBytes = bufferedInputStream.read(buffer);
                LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataAndNoReturn - numberOfReadBytes ["
                        + numberOfReadBytes + "], [On Thread: " + Thread.currentThread().getName() + "]");
                if (numberOfReadBytes > 0) {
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    baos.write(buffer,0, numberOfReadBytes);
                    final byte[] dataArray = baos.toByteArray();
                    final String readStr = new String(dataArray, 0, dataArray.length, StandardCharsets.UTF_8);
                    LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataAndNoReturn - readStr ["
                            + readStr + "], [On Thread: " + Thread.currentThread().getName() + "]");

                    if (numberOfReadBytes == 8196) {
                        LogWrapper.showLog(Log.INFO, "IeSocketManager_SocketHelper", "readDataAndNoReturn - read more -> readDataAndNoReturn!!");
                        readDataAndNoReturn();
                    }
                }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "IeSocketManager_SocketHelper", "error on readDataAndNoReturn for ["
                        + theItem.theIdentifier() + "], [On Thread: " + Thread.currentThread().getName() + "], message: " + cause.getLocalizedMessage());
            }
        }
    }

}
