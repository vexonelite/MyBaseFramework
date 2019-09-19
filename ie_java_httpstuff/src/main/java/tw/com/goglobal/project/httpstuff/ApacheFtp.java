package tw.com.goglobal.project.httpstuff;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.factories.FactoryDelegate;


public final class ApacheFtp {

    private volatile static FTPClient instance;

    public static final class ClientFactory implements FactoryDelegate<FTPClient> {
        @NonNull
        @Override
        public FTPClient create() {
            if (instance == null) {
                synchronized (FTPClient.class) {
                    if (instance == null) {
                        instance = new FTPClient();
                    }
                }
            }
            return instance;
        }
    }

    @Nullable
    public static String getFileEncodingString() {
        return System.getProperty("file.encoding");
    }

    ///

    public enum ConnectionState {
        IDLE,
        CONNECTED,
        LOGGED_IN
    }

    ///

    public static final class Configuration {
        public final String hostName;
        public final int portNumber;
        public final String controlEncoding;
        public final int connectTimeout;
        public final String username;
        public final String password;
        public final String remotePath;
        public final String localPath;
        public final List<String> fileNameList;

        public Configuration(
                @NonNull String hostName,
                int portNumber,
                @NonNull  String controlEncoding,
                int connectTimeout,
                @NonNull String username,
                @NonNull String password,
                @NonNull String remotePath,
                @NonNull String localPath,
                @NonNull List<String> fileNameList
        ) {
            this.hostName = hostName;
            this.portNumber = portNumber;
            this.controlEncoding = controlEncoding;
            this.connectTimeout = connectTimeout;
            this.username = username;
            this.password = password;
            this.remotePath = remotePath;
            this.localPath = localPath;
            this.fileNameList = fileNameList;
        }

        @NonNull
        public String getWorkingDirectory() throws IeRuntimeException {
            try {
                return new String(remotePath.getBytes(controlEncoding), StandardCharsets.ISO_8859_1);
            }
            catch (Exception cause) {
                throw new IeRuntimeException("Error on getWorkingDirectory", ErrorCodes.Base.INTERNAL_CONVERSION_ERROR);
            }
        }
    }

    ///

    public static final class Helper {

        private ConnectionState connectionState = ConnectionState.IDLE;
        private final String tag = Helper.class.getSimpleName();

        private final FTPClient ftpClient;
        private final Configuration config;

        public Helper(@NonNull FTPClient ftpClient, @NonNull Configuration config) {
            this.ftpClient = ftpClient;
            this.config = config;
        }

        @NonNull
        public ConnectionState getConnectionState() {
            return connectionState;
        }

        public void connectToRemote() throws IeRuntimeException {
            try {
                ftpClient.setControlEncoding(config.controlEncoding);
                if (config.connectTimeout > 0) {
                    ftpClient.setConnectTimeout(config.connectTimeout);
                }
                else {
                    throw new IllegalArgumentException("connectTimeout cannot be negative!");
                }
                ftpClient.connect(config.hostName, config.portNumber);
                //connectionState = ConnectionState.CONNECTED;
                LogWrapper.showLog(Log.INFO, tag, "connect to remote successfully!");
            }
            catch (Exception cause) {
                throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.CONNECTION_FAILURE);
            }
        }

        public void disconnect() {
            //if (connectionState == ConnectionState.CONNECTED) { }
            try {
                ftpClient.disconnect();
                //connectionState = ConnectionState.IDLE;
                LogWrapper.showLog(Log.INFO, tag, "disconnect successfully!");
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, tag, "Error on disconnect", cause);
            }
        }

        public void login() throws IeRuntimeException {
//            if (connectionState != ConnectionState.CONNECTED) {
//                final IllegalStateException cause = new IllegalStateException("connectionState != ConnectionState.CONNECTED");
//                throw new IeRuntimeException(cause, ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
//            }

            try {
                ftpClient.login(config.username, config.password);
                // 設定檔案傳輸型別為二進位制
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 獲取ftp登入應答程式碼
                final int reply = ftpClient.getReplyCode();
                // 驗證是否登陸成功
                if (!FTPReply.isPositiveCompletion(reply)) {
                    throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.LOGIN_FAILURE);
                }
                //connectionState = ConnectionState.LOGGED_IN;
                LogWrapper.showLog(Log.INFO, tag, "login successfully!");
            }
            catch (Exception cause) {
                throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.LOGIN_FAILURE);
            }
        }

        public void logout() throws IeRuntimeException {
//            if (connectionState != ConnectionState.LOGGED_IN) {
//                final IllegalStateException cause = new IllegalStateException("connectionState != ConnectionState.LOGGED_IN");
//                throw new IeRuntimeException(cause, ErrorCodes.Base.ILLEGAL_ARGUMENT_ERROR);
//            }

            try {
                ftpClient.logout();
                //connectionState = ConnectionState.CONNECTED;
                LogWrapper.showLog(Log.INFO, tag, "logout successfully!");
            }
            catch (Exception cause) {
                throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.LOGOUT_FAILURE);
            }
        }

        public void changeWorkingDirectory() throws IeRuntimeException {
            final String workingDirectory = config.getWorkingDirectory();
            try {
                // 轉移到FTP伺服器目錄至指定的目錄下
                ftpClient.changeWorkingDirectory(workingDirectory);
            }
            catch (Exception cause) {
                throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.CHANGE_DIRECTORY_FAILURE);
            }
        }

        @NonNull
        private FTPFile[] getFileList() throws IeRuntimeException {
            try {
                // 獲取檔案列表
                return ftpClient.listFiles();
            }
            catch (Exception cause) {
                throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.FAIL_TO_GET_FILE_LIST);
            }
        }

        @NonNull
        public List<File> bulkDownloadToList() throws IeRuntimeException {
            final List<File> downloadedFileList = new ArrayList<>();
            final FTPFile[] ftpFileList = getFileList();
            for (final FTPFile ftpFile : ftpFileList) {
                final String ftpFileName = ftpFile.getName();
                if (config.fileNameList.contains(ftpFileName)) {
                    final File downloadedFile = downloadSingleFile(ftpFileName);
                    downloadedFileList.add(downloadedFile);
                }
            }
            LogWrapper.showLog(Log.INFO, tag, "fileNameList.size: " + config.fileNameList.size() + ", downloadedFileList.size: " + downloadedFileList.size());
            return downloadedFileList;
        }

        @NonNull
        public Map<String, File> bulkDownloadToMap() throws IeRuntimeException {
            final Map<String, File> downloadedFileMap = new HashMap<>();
            final FTPFile[] ftpFileList = getFileList();
            for (final FTPFile ftpFile : ftpFileList) {
                final String ftpFileName = ftpFile.getName();
                LogWrapper.showLog(Log.INFO, tag, "ftpFileName: " + ftpFileName);
                if (config.fileNameList.contains(ftpFileName)) {
                    final File downloadedFile = downloadSingleFile(ftpFileName);
                    downloadedFileMap.put(ftpFileName, downloadedFile);
                    LogWrapper.showLog(Log.INFO, tag, "get ftpFile for " + ftpFileName);
                }
            }
            LogWrapper.showLog(Log.INFO, tag, "fileNameList.size: " + config.fileNameList.size() + ", downloadedFileMap.size: " + downloadedFileMap.size());
            return downloadedFileMap;
        }

        private File downloadSingleFile(@NonNull String ftpFileName) throws IeRuntimeException {
            OutputStream outputStream = null;
            try {
                final File localFile = new File(config.localPath + "/" + ftpFileName);
                LogWrapper.showLog(Log.INFO, tag, "downloadSingleFile - localFile: " + localFile.getPath());
                outputStream = new FileOutputStream(localFile);
                ftpClient.retrieveFile(ftpFileName, outputStream);
                return localFile;
            }
            catch (Exception cause) {
                throw new IeRuntimeException("FTP connection failure!", ErrorCodes.FTP.DOWNLOAD_FAILURE);
            }
            finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    }
                    catch (Exception cause) {
                        LogWrapper.showLog(Log.ERROR, tag, "Error on outputStream.close()!");
                    }

                }
            }
        }
    }
}
