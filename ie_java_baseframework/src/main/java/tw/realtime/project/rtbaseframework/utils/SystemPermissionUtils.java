package tw.realtime.project.rtbaseframework.utils;

import android.Manifest;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * A helper class used to access system permissions
 */
public final class SystemPermissionUtils {

    public static final int REQUEST_CODE_INTERNET_PERMISSION = 101;
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 102;
    public static final int REQUEST_CODE_MICROPHONE_PERMISSION = 103;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 104;
    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 105;
    public static final int REQUEST_CODE_TAKE_SHOT_PERMISSIONS = 106;
    public static final int REQUEST_CODE_VIDEO_REC_PERMISSIONS = 107;
    public static final int REQUEST_CODE_COARSE_LOCATION_PERMISSION = 108;
    public static final int REQUEST_CODE_FINE_LOCATION_PERMISSION = 109;
    public static final int REQUEST_CODE_LOCATION_PERMISSIONS = 110;


    /** ask if the permission INTERNET has been granted. */
    public static boolean hasInternetPermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permission CAMERA has been granted. */
    public static boolean hasCameraPermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permission RECORD_AUDIO has been granted. */
    public static boolean hasMicrophonePermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permission READ_EXTERNAL_STORAGE has been granted. */
    public static boolean hasReadExternalStoragePermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permission WRITE_EXTERNAL_STORAGE has been granted. */
    public static boolean hasWriteExternalStoragePermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permission ACCESS_COARSE_LOCATION has been granted. */
    public static boolean hasCoarseLocationPermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permission ACCESS_FINE_LOCATION has been granted. */
    public static boolean hasFineLocationPermissionBeenGranted(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** ask if the permissions CAMERA, RECORD_AUDIO, and WRITE_EXTERNAL_STORAGE have been granted. */
    public static boolean hasEnoughVideoRecPermission(@NonNull Context context) {
        boolean camera = hasCameraPermissionBeenGranted(context);
        boolean mic = hasMicrophonePermissionBeenGranted(context);
        boolean read = hasReadExternalStoragePermissionBeenGranted(context);
        boolean write = hasWriteExternalStoragePermissionBeenGranted(context);
        return (camera && mic && read && write);
    }

    /** ask if the permissions CAMERA and WRITE_EXTERNAL_STORAGE have been granted. */
    public static boolean hasEnoughTakeShotPermission(@NonNull Context context) {
        boolean camera = hasCameraPermissionBeenGranted(context);
        boolean read = hasReadExternalStoragePermissionBeenGranted(context);
        boolean write = hasWriteExternalStoragePermissionBeenGranted(context);
        return (camera && read && write);
    }

    /** ask if the permissions ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION have been granted. */
    public static boolean hasEnoughLocationPermission(@NonNull Context context) {
        boolean fine = hasFineLocationPermissionBeenGranted(context);
        boolean coarse = hasCoarseLocationPermissionBeenGranted(context);
        return (fine && coarse);
    }

    /** appeal user for the permission INTERNET. */
    public static void requestInternetPermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.INTERNET},
                REQUEST_CODE_INTERNET_PERMISSION);
    }

    /** appeal user for the permission CAMERA. */
    public static void requestCameraPermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.CAMERA },
                REQUEST_CODE_CAMERA_PERMISSION);
    }

    /** appeal user for the permission CAMERA. */
    public static void requestCameraPermission(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] { Manifest.permission.CAMERA },
                REQUEST_CODE_CAMERA_PERMISSION);
    }

    /** appeal for the permission RECORD_AUDIO */
    public static void requestMicrophonePermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.RECORD_AUDIO },
                REQUEST_CODE_MICROPHONE_PERMISSION);
    }

    /** appeal for the permission RECORD_AUDIO */
    public static void requestMicrophonePermission(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] { Manifest.permission.RECORD_AUDIO },
                REQUEST_CODE_MICROPHONE_PERMISSION);
    }

    /** appeal user for the permission READ_EXTERNAL_STORAGE. */
    public static void requestReadExternalStoragePermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    /** appeal user for the permission READ_EXTERNAL_STORAGE. */
    public static void requestReadExternalStoragePermission(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    /** appeal user for the permission WRITE_EXTERNAL_STORAGE. */
    public static void requestWriteExternalStoragePermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    /** appeal user for the permission WRITE_EXTERNAL_STORAGE. */
    public static void requestWriteExternalStoragePermission(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    /** appeal user for the permission ACCESS_FINE_LOCATION. */
    public static void requestFineLocationPermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_CODE_FINE_LOCATION_PERMISSION);
    }

    /** appeal user for the permission ACCESS_FINE_LOCATION. */
    public static void requestFineLocationPermission(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_CODE_FINE_LOCATION_PERMISSION);
    }

    /** appeal user for the permission ACCESS_COARSE_LOCATION. */
    public static void requestCoarseLocationPermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                REQUEST_CODE_COARSE_LOCATION_PERMISSION);
    }

    /** appeal user for the permission ACCESS_COARSE_LOCATION. */
    public static void requestCoarseLocationPermission(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                REQUEST_CODE_COARSE_LOCATION_PERMISSION);
    }

    /** appeal user for the permission ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION. */
    public static void requestLocationPermissions(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_CODE_LOCATION_PERMISSIONS);
    }

    /** appeal user for the permission ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION. */
    public static void requestLocationPermissions(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_CODE_LOCATION_PERMISSIONS);
    }

    /** appeal user for the permissions CAMERA and WRITE_EXTERNAL_STORAGE. */
    public static void requestTakeShotPermissions(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_CODE_TAKE_SHOT_PERMISSIONS);
    }

    /** appeal user for the permissions CAMERA and WRITE_EXTERNAL_STORAGE. */
    public static void requestTakeShotPermissions(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_CODE_TAKE_SHOT_PERMISSIONS);
    }

    /** appeal user for the permissions CAMERA, RECORD_AUDIO, and WRITE_EXTERNAL_STORAGE. */
    public static void requestVideoRecPermissions(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_CODE_VIDEO_REC_PERMISSIONS);
    }

    /** appeal user for the permissions CAMERA, RECORD_AUDIO, and WRITE_EXTERNAL_STORAGE. */
    public static void requestVideoRecPermissions(@NonNull Fragment fragment) {
        fragment.requestPermissions(
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_CODE_VIDEO_REC_PERMISSIONS);
    }


    public static boolean verifyGrantResults(@Nullable int[] grantResults, @Nullable String logTag) {
        if (null == logTag) {
            logTag = "SystemPermissionUtils";
        }
        boolean result = false;
        // If request is cancelled, the result arrays are empty.
        if ( (null != grantResults) && (grantResults.length > 0)) {
            int count = 0;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    count = count + 1;
                }
            }
            if (count == grantResults.length) {
                result = true;
            }
            LogWrapper.showLog(Log.INFO, logTag, "grantResults length: " + grantResults.length + ", count: " + count);
        }
        else {
            LogWrapper.showLog(Log.WARN, logTag, "grantResults is either null or empty!");
        }
        return result;
    }

    /** template of implementation for the callback of permission appeal. */
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_INTERNET_PERMISSION:
            case REQUEST_CODE_CAMERA_PERMISSION:
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION:
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
            case REQUEST_CODE_MICROPHONE_PERMISSION:
            case REQUEST_CODE_VIDEO_REC_PERMISSIONS:
            case REQUEST_CODE_TAKE_SHOT_PERMISSIONS:
                if (verifyGrantResults(grantResults, null)) {
                    // permission was granted, yay!
                    // Do the contacts-related task you need to do.
                }
                else {
                    // permission denied, boo!
                    // Disable the functionality that depends on this permission.
                }
                break;
        }
    }
}
