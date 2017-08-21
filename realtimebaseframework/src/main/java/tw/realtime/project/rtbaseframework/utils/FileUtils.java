package tw.realtime.project.rtbaseframework.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tw.realtime.project.rtbaseframework.LogWrapper;


public class FileUtils {

    /**
     *  Creates a media file in the {@code Environment.DIRECTORY_PICTURES} directory.
     *  The directory is persistent and available to other applications like gallery.
     *
     *  @return A file object pointing to the newly created file.
     */
    public static File getOutputImageFile (String fileName, String folderName) {

        // To be safe, you should check if the SDCard is mounted
        // by using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return  null;
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        if ( (null == folderName) || (folderName.isEmpty()) ) {
            folderName = "APP_CACHE";
        }
        // file path: storage/sdcard#/Pictures/folderName/
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folderName);

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()) {
                LogWrapper.showLog(Log.WARN, "FileUtils", "getOutputImageFile() - failed to create the directory!");
                return null;
            }
        }

        // Create a media file name
        if ( (null == fileName) || (fileName.isEmpty()) ) {
            fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ fileName + ".jpg");
        return mediaFile;
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

}
