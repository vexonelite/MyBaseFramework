package tw.com.goglobal.project.httpstuff;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        /** used for download */
        public final List<String> fileNameList = new ArrayList<>();
        /** used for upload */
        public final Map<String, File> fileMap = new HashMap<>();
        /** used for delete */
        public final List<String> deleteFileNameList = new ArrayList<>();

        /**
         * used for download
         * In order to save different filename on the local storage
         */
        public final Map<String, String> explicitFileNameMap = new HashMap<>();

        public Configuration(
                @NonNull String hostName,
                int portNumber,
                @NonNull  String controlEncoding,
                int connectTimeout,
                @NonNull String username,
                @NonNull String password,
                @NonNull String remotePath,
                @NonNull String localPath
        ) {
            this.hostName = hostName;
            this.portNumber = portNumber;
            this.controlEncoding = controlEncoding;
            this.connectTimeout = connectTimeout;
            this.username = username;
            this.password = password;
            this.remotePath = remotePath;
            this.localPath = localPath;
        }

        @NonNull public Configuration setExplicitFileNameList(
                @NonNull final List<String> fileNameList, @NonNull final List<String> explicitFileNameList) {
            if (fileNameList.isEmpty()) {
                LogWrapper.showLog(Log.ERROR, "ApacheFtp", "Configuration - setExplicitFileNameList - fileNameList is empty!!");
                return this;
            }
            if (explicitFileNameList.isEmpty()) {
                LogWrapper.showLog(Log.ERROR, "ApacheFtp", "Configuration - setExplicitFileNameList - explicitFileNameList is empty!!");
                return this;
            }
            if (explicitFileNameList.size() != fileNameList.size()) {
                LogWrapper.showLog(Log.ERROR, "ApacheFtp", "Configuration - setExplicitFileNameList - explicitFileNameList.size != fileNameList.size!!");
                return this;
            }

            final Map<String, String> fileNameMap = new HashMap<>();
            for(int i = 0; i < fileNameList.size(); i++) {
                final String fileName1 = fileNameList.get(i);
                final String fileName2 = explicitFileNameList.get(i);
                fileNameMap.put(fileName1, fileName2);
                LogWrapper.showLog(Log.INFO, "ApacheFtp", "Configuration - setExplicitFileNameList - put [" + fileName2 + "] for [" + fileName1 + "]");
            }

            this.explicitFileNameMap.clear();
            this.explicitFileNameMap.putAll(fileNameMap);
            return this;
        }

        @NonNull public Configuration setFileNameList(@NonNull List<String> fileNameList) {
            if (!fileNameList.isEmpty()) {
                this.fileNameList.clear();
                this.fileNameList.addAll(fileNameList);
            }
            return this;
        }

        @NonNull public Configuration setFileMap(@NonNull Map<String, File> fileMap) {
            if (!fileMap.isEmpty()) {
                this.fileMap.clear();
                this.fileMap.putAll(fileMap);
            }
            return this;
        }

        @NonNull public Configuration setDeleteFileNameList(@NonNull final List<String> deleteFileNameList) {
            if (!deleteFileNameList.isEmpty()) {
                this.deleteFileNameList.clear();
                this.deleteFileNameList.addAll(deleteFileNameList);
            }
            return this;
        }

        @NonNull public String getWorkingDirectory() throws IeRuntimeException {
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
        public final Configuration config;

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
                LogWrapper.showLog(Log.ERROR, tag, "Error on connectToRemote!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.CONNECTION_FAILURE);
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
                LogWrapper.showLog(Log.ERROR, tag, "Error on login!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.LOGIN_FAILURE);
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
                LogWrapper.showLog(Log.ERROR, tag, "Error on logout!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.LOGOUT_FAILURE);
            }
        }

        public void changeWorkingDirectory() throws IeRuntimeException {
            final String workingDirectory = config.getWorkingDirectory();
            try {
                // 轉移到FTP伺服器目錄至指定的目錄下
                final boolean result = ftpClient.changeWorkingDirectory(workingDirectory);
                if (!result) {
                    throw new IeRuntimeException("Fail to change working directory!", ErrorCodes.FTP.CHANGE_DIRECTORY_FAILURE);
                }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, tag, "Error on changeWorkingDirectory!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.CHANGE_DIRECTORY_FAILURE);
            }
        }

        @NonNull
        private FTPFile[] getFileList() throws IeRuntimeException {
            try {
                // 獲取檔案列表
                return ftpClient.listFiles();
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, tag, "Error on getFileList!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.FAIL_TO_GET_FILE_LIST);
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
                LogWrapper.showLog(Log.ERROR, tag, "Error on downloadSingleFile!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.DOWNLOAD_FAILURE);
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

        ///

        @NonNull
        public Map<String, Boolean> bulkUploadToMap() throws IeRuntimeException {
            final Map<String, Boolean> uploadResultFileMap = new HashMap<>();
            final Set<String> keySet = config.fileMap.keySet();
            for (final String fileName : keySet) {
                final File file = config.fileMap.get(fileName);
                final boolean result = uploadSingleFile(fileName, file);
                uploadResultFileMap.put(fileName, result);
                LogWrapper.showLog(Log.INFO, tag, "upload result: " + result + " for fileName: " + fileName);
            }
            LogWrapper.showLog(Log.INFO, tag, "fileMap.size: " + config.fileMap.size() + ", uploadResultFileMap.size: " + uploadResultFileMap.size());
            return uploadResultFileMap;
        }

        private boolean uploadSingleFile(@NonNull String fileName, @NonNull File file) throws IeRuntimeException {
            InputStream inputStream = null;
            try {
                LogWrapper.showLog(Log.INFO, tag, "uploadSingleFile - file: " + file.getPath());
                inputStream = new FileInputStream(file);
                final String remoteFileName = new String(fileName.getBytes(config.controlEncoding), StandardCharsets.ISO_8859_1);
                return ftpClient.storeFile(remoteFileName, inputStream);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, tag, "Error on uploadSingleFile!");
                throw new IeRuntimeException(cause, ErrorCodes.FTP.UPLOAD_FAILURE);
            }
            finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    }
                    catch (Exception cause) {
                        LogWrapper.showLog(Log.ERROR, tag, "Error on inputStream.close()!");
                    }
                }
            }
        }

        ///

        @NonNull
        public Map<String, Boolean> bulkDeleteMap() throws IeRuntimeException {
            final Map<String, Boolean> deletedFileMap = new HashMap<>();

            if (config.deleteFileNameList.isEmpty()) {
                LogWrapper.showLog(Log.INFO, tag, "bulkDeleteMap - deleteFileNameList is empty - return empty map!!");
                return deletedFileMap;
            }

            final FTPFile[] ftpFileList = getFileList();
            for (final FTPFile ftpFile : ftpFileList) {
                final String ftpFileName = ftpFile.getName();
                LogWrapper.showLog(Log.INFO, tag, "bulkDeleteMap - ftpFileName: " + ftpFileName);
                if (config.deleteFileNameList.contains(ftpFileName)) {
                    final boolean hasDeleted = deleteSingleFile(ftpFileName);
                    deletedFileMap.put(ftpFileName, hasDeleted);
                    LogWrapper.showLog(Log.INFO, tag, "bulkDeleteMap - delete ftpFile for " + ftpFileName + " - result: " + hasDeleted);
                }
            }
            LogWrapper.showLog(Log.INFO, tag, "bulkDeleteMap - deleteFileNameList.size: " + config.deleteFileNameList.size() + ", deletedFileMap.size: " + deletedFileMap.size());
            return deletedFileMap;
        }

        private boolean deleteSingleFile(@NonNull final String ftpFileName) throws IeRuntimeException {
            try { return ftpClient.deleteFile(ftpFileName); }
            catch (Exception cause) { throw new IeRuntimeException(cause, ErrorCodes.FTP.DELETE_FAILURE); }
        }
    }
}
