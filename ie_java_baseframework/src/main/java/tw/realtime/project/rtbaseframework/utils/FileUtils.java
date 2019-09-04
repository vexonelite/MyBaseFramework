package tw.realtime.project.rtbaseframework.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import tw.realtime.project.rtbaseframework.LogWrapper;


public class FileUtils {

    /**
     *  Creates a media file in the {@code Environment.DIRECTORY_PICTURES} directory.
     *  The directory is persistent and available to other applications like gallery.
     *
     *  @return A file object pointing to the newly created file.
     */
    @Nullable
    public static String getOutputFileName(
            @NonNull Context context,
            @NonNull String givenFileName,
            @NonNull String givenFileExtension,
            @Nullable String givenFolderName,
            @Nullable String givenEnvironmentFolder) {

        final File mediaStorageDir = getOutputFolder(context, givenFolderName, givenEnvironmentFolder);
        if (null == mediaStorageDir) {
            return null;
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.w("FileUtils", "getOutputFile() - failed to create the directory!");
                return null;
            }
        }

        final String fileName = (givenFileName.length() > 0)
                ? givenFileName
                : new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        return (givenFileExtension.isEmpty())
                ? mediaStorageDir.getPath() + File.separator + fileName
                : mediaStorageDir.getPath() + File.separator + fileName + "." + givenFileExtension;
    }

    @Nullable
    public static File getOutputFolder(
            @NonNull Context context,
            @Nullable String givenFolderName,
            @Nullable String givenEnvironmentFolder) {

        // To be safe, you should check if the SDCard is mounted
        // by using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        final String folderName =
                ((null != givenFolderName) && (givenFolderName.length() > 0))
                        ? givenFolderName : "APP_CACHE";

        final String environmentFolder =
                ((null != givenEnvironmentFolder) && (givenEnvironmentFolder.length() > 0))
                        ? givenEnvironmentFolder : Environment.DIRECTORY_DOWNLOADS;

        // file path: storage/sdcard#/Pictures/folderName/
        /*
         * getExternalStoragePublicDirectory()
         * This method was deprecated in API level 29.
         * To improve user privacy, direct access to shared/external storage devices is deprecated.
         * When an app targets ``Build.VERSION_CODES.Q``,
         * the path returned from this method is no longer directly accessible to apps.
         * Apps can continue to access content stored on shared/external storage by migrating to alternatives such as
         * ``Context#getExternalFilesDir(String)``, ``MediaStore``, or ``Intent#ACTION_OPEN_DOCUMENT``.
         */
        // final File mediaStorageDir = ...
        return (Build.VERSION.SDK_INT >= 29)
                ? new File(context.getExternalFilesDir(environmentFolder), folderName)
                : new File(Environment.getExternalStoragePublicDirectory(environmentFolder), folderName);
    }

    public static String getRealImageFilePath (Context context, final Uri imageUri) {

        Cursor cursor = null;
        String filePath = "";
        String[] projection = { MediaStore.Images.Media.DATA };
        try {
            cursor = context.getContentResolver()
                    .query(imageUri, projection, null, null, null);
            if (null != cursor) {
                try {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String result = cursor.getString(column_index);
                    if ( (null != result) && (!result.isEmpty()) ) {
                        filePath = result;
                    }
                } catch (IllegalArgumentException iae) {
                    LogWrapper.showLog(Log.WARN, "FileUtils", "IllegalArgumentException on getRealImageFilePath !", iae);
                }
            }
        } catch (RuntimeException re) {
            LogWrapper.showLog(Log.WARN, "FileUtils", "RuntimeException on getRealImageFilePath!", re);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return filePath;
    }

    /**
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    public static String getPathAlternative (final Context context, Uri uri)
            throws URISyntaxException {

        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4),
        // we need to deal with different Uris.

        if (needToCheckUri) {
            boolean isDocumentUri = DocumentsContract.isDocumentUri(context, uri);
            if (isDocumentUri) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                }
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("image".equals(type)) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    selection = "_id=?";
                    selectionArgs = new String[] {
                            split[1]
                    };
                }
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String path = null;
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                if ( (null != cursor) && (cursor.moveToFirst()) ) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(column_index);
                }
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, "FileUtils", "Exception on getPathAlternative!", e);
            }
            finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return path;
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Make the newly saved file visible by other apps.
     * @see <a href="http://stackoverflow.com/questions/2170214/image-saved-to-sdcard-doesnt-appear-in-androids-gallery-app">The reference</a>
     */
    public static void makeImageFileVisibleByOthers (Context context, Uri mediaFileUri) {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mediaFileUri);
            context.sendBroadcast(mediaScanIntent);
        }
        else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    // https://www.mkyong.com/java/how-to-convert-file-into-an-array-of-bytes/
    @NonNull
    public static byte[] fileToByteArray(@NonNull File file) throws Exception {
        final FileInputStream fileInputStream = new FileInputStream(file);
        //init array with file length
        final byte[] bytesArray = new byte[(int) file.length()];
        //read file into bytes[]
        fileInputStream.read(bytesArray);
        fileInputStream.close();
        return bytesArray;
    }

    public static void writeResponseBodyToDisk(@NonNull ResponseBody responseBody,
                                               @NonNull File savedFile) throws IOException {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];

            final long fileSize = responseBody.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = responseBody.byteStream();
            outputStream = new FileOutputStream(savedFile);

            while (true) {
                final int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                Log.d("FileUtils", "file download: " + fileSizeDownloaded + " of " + fileSize);
            }
            outputStream.flush();
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

        /*
        try {
            // todo change the file location/name according to your needs

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = responseBody.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = responseBody.byteStream();
                outputStream = new FileOutputStream(savedFile);

                while (true) {
                    final int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("IeUtils", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            }
            catch (IOException e) {
                Log.e("IeUtils", "IOException on save downloaded file", e);
                return false;
            }
            finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.e("IeUtils", "IOException on inputStream | outputStream#close()", e);
            return false;
        }
        */
    }

    public static void unzipFile(@NonNull File zipFile, @NonNull File targetDirectory) throws IOException {
        final FileInputStream zipFileInputStream = new FileInputStream(zipFile);
        final BufferedInputStream zipBufferedInputStream = new BufferedInputStream(zipFileInputStream);
        final ZipInputStream zipInputStream = new ZipInputStream(zipBufferedInputStream);

        try {
            ZipEntry zipEntry;
            int count;
            byte[] buffer = new byte[8192];
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                final File file = new File(targetDirectory, zipEntry.getName());
                final File dir = zipEntry.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (zipEntry.isDirectory())
                    continue;
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    while ((count = zipInputStream.read(buffer)) != -1)
                        fileOutputStream.write(buffer, 0, count);
                } finally {
                    fileOutputStream.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zipInputStream.close();
        }
    }
}
