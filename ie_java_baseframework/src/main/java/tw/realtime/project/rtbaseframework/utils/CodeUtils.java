package tw.realtime.project.rtbaseframework.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Created by vexonelite on 2017/6/5.
 */

public final class CodeUtils {

    @NonNull
    public static String getDecimalFormatString(@NonNull String inputPrice) {
        final DecimalFormat fmt = new DecimalFormat();
        final DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);
        try {
            final float fPrice = Float.parseFloat(inputPrice);
            return fmt.format(fPrice);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on getDecimalFormatString", e);
            return inputPrice;
        }
    }

    /**
     * Deprecated!
     * <p>
     * Please use "ContextCompat.getColor(@NonNull Context context, @ColorRes int colorResId)" instead
     * <p>
     * 將 R.color.resID 轉成 int color
     * @param context
     * @param colorResId
     * @return
     */
    @Deprecated
    public static int getColorFromResourceId (@NonNull Context context, @ColorRes int colorResId) {
        return ContextCompat.getColor(context, colorResId);
    }

    /**
     * 將 R.drawable.resID 轉成 Drawable drawable
     * @param context
     * @param drawableResID
     * @return
     *
     * Ref: https://stackoverflow.com/questions/29041027/android-getresources-getdrawable-deprecated-api-22
     */
    @Deprecated
    public static Drawable getDrawableFromResourceId (@NonNull Context context, @DrawableRes int drawableResID) {
        return ContextCompat.getDrawable(context, drawableResID);
//        if (Build.VERSION.SDK_INT >= 21) {
//            return Resources.getDrawable(resID, Theme);
//        }
    }

    /**
     * get the size of device's screen by excluding embedding control panel
     * (such as back button, home button, etc.)
     * <p> 取得螢幕的寬與高
     */
    @Nullable
    public static Point getRealScreenSize (@NonNull Context context) {
        try {
            final Point size = new Point();
            final WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getRealSize(size);
            return size;
        } catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "error", "Exception on getRealScreenSize!", e);
            return null;
        }
    }

    // get the size of device's screen.
    @Nullable
    public static Point getScreenSize (@NonNull Context context) {
        try {
            final DisplayMetrics metrics = new DisplayMetrics();
            final WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(metrics);
            final Point size = new Point();
            size.x = metrics.widthPixels;
            size.y = metrics.heightPixels;
            return size;
        } catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "error", "Exception on getScreenSize!", e);
            return null;
        }
    }


    /**
     * ZXing 套件將字串產生 Qr 碼的影像
     * @param context
     * @param qrCodeContent 卻轉換的 Qr碼字串
     * @return Qr 碼影像; 若過程中出現例外，會回傳 Null
     */
    public static Bitmap generateQrCode (Context context, String qrCodeContent) {
        if ((null == qrCodeContent) || (qrCodeContent.isEmpty())) {
            LogWrapper.showLog(Log.WARN, "CodeUtil", "generateQrCode: qrCodeContent is invalid!");
            return null;
        }

        final Point screenSize = getRealScreenSize(context);
        // QR code 寬度
        final int qrImageSize = ((screenSize.x - 100) > 200) ? (screenSize.x - 100) : 200;
        // QR code Content codec
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        // 文字內容為 UTF-8
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 設定 QR code 容錯率為 H
        // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            // 建立 QR code 的資料矩陣
            BitMatrix bitMatrix = writer.encode(
                    qrCodeContent, BarcodeFormat.QR_CODE, qrImageSize, qrImageSize, hints);
            // ZXing 還可以生成其他形式條碼，如：BarcodeFormat.CODE_39、BarcodeFormat.CODE_93、BarcodeFormat.CODE_128、BarcodeFormat.EAN_8、BarcodeFormat.EAN_13...

            //建立點陣圖
            Bitmap bitmap = Bitmap.createBitmap(qrImageSize, qrImageSize, Bitmap.Config.ARGB_8888);
            // 將 QR code 資料矩陣繪製到點陣圖上
            for (int y = 0; y < qrImageSize; y++) {
                for (int x = 0; x < qrImageSize; x++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on generateQrCode", e);
            return null;
        }
    }

    /**
     * Ref: https://magiclen.org/android-text-length/
     * @param charSequence
     * @return
     */
    public static int calculateCharSequenceLength(@NonNull CharSequence charSequence) {
        int len = 0;
        final int length = charSequence.length();
        for (int i = 0; i < length; i++) {
//            final char tmp = charSequence.charAt(i);
//            if (tmp >= 0x20 && tmp <= 0x7E) {
//                // 字元值 32~126 是 ASCII 半形字元的範圍
//                len++;
//            } else {
//                // 非半形字元
//                len += 2;
//            }
            len++;
        }
        return len;
    }

    public static void sendEmailByIntent (Context context, String emailAddress) {
        if ( (null == context) || (null == emailAddress) ) {
            return;
        }
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", emailAddress, null));
            context.startActivity(Intent.createChooser(emailIntent, null));
            //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            //emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            //context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//            intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
//            context.startActivity(Intent.createChooser(intent, "Send Email"));
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on sendEmailByIntent", e);
        }
    }

    public static void makePhoneCallByIntent (Context context, String phoneNumber) {
        if ( (null == context) || (null == phoneNumber) ) {
            return;
        }
        try {
            String phoneNum = "tel:" + phoneNumber;
            Uri uri = Uri.parse(phoneNum);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on makePhoneCallByIntent", e);
        }
    }

    public static void openBrowser(Context context, String url) {
        LogWrapper.showLog(Log.INFO, "CodeUtils", "openBrowser - given url: " + url);
        if ( (!url.startsWith("http://")) && (!url.startsWith("https://")) ) {
            url = "http://" + url;
            LogWrapper.showLog(Log.INFO,"CodeUtil", "openBrowser - append 'http': " + url);
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    @SuppressWarnings({"MissingPermission"})
    public static boolean hasInterNet (Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean hasInterNet = false;
        if ( (null != networkInfo) && (networkInfo.isConnected()) ) {
            try {
                //make a URL to a known source
                URL url = new URL("https://www.google.com");
                //open a connection to that source
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                //trying to retrieve data from the source. If there
                //is no connection, this line will fail
                Object objData = urlConnect.getContent();
                hasInterNet = true;
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, "CodeUtils", "Exception on hasInterNet", e);
                //return false;
            }
        }
        //LogWrapper.showLog(Log.INFO, "CodeUtils", "hasInterNet: " + hasInterNet);
        return hasInterNet;
    }

    @SuppressWarnings({"MissingPermission"})
    public static String getNetworkAccessType (Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String ret = "u";
        if ( (null != networkInfo) && (networkInfo.isConnected()) ) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                ret = "wifi";
            }
            else if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
                ret = "cell";
            }
        }
        return ret;
    }

    /**
     * 檢查給予的 source 字串中有否有包含顏文字
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i+1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50|| hs == 0x231a ) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() -1) {
                    char ls = source.charAt(i+1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return  isEmoji;
    }

    public static int compareAppVersion (@NonNull String currentVersion,
                                         @NonNull String apiVersion) throws IllegalArgumentException {
        if (currentVersion.isEmpty()) {
            throw new IllegalArgumentException("currentVersion cannot be empty");
        }
        if (apiVersion.isEmpty()) {
            throw new IllegalArgumentException("apiVersion cannot be empty");
        }
        LogWrapper.showLog(Log.INFO,"CodeUtil", "compareAppVersion - currentVersion: "
                + currentVersion + ", apiVersion: " + apiVersion);

        final String[] currentArray = currentVersion.split("\\.");
        final String[] apiArray = apiVersion.split("\\.");
        final int currentSize = currentArray.length;
        if (currentSize == 0) {
            throw new IllegalArgumentException("currentSize is 0");
        }
        final int apiSize =  apiArray.length;
        if (apiSize == 0) {
            throw new IllegalArgumentException("apiSize is 0");
        }
        LogWrapper.showLog(Log.INFO,"CodeUtil", "compareAppVersion - currentSize: "
                + currentSize + ", apiSize: " + apiSize);

        final ArrayList<String> currentList = new ArrayList<>();
        final ArrayList<String> apiList = new ArrayList<>();
        final int size = Math.max(currentSize, apiSize);
        for (int i = 0; i < size; i++) {
            if (i < currentArray.length) {
                currentList.add(currentArray[i]);
            }
            else {
                currentList.add("0");
            }

            if (i < apiArray.length) {
                apiList.add(apiArray[i]);
            }
            else {
                apiList.add("0");
            }
        }

        LogWrapper.showLog(Log.INFO,"CodeUtil", "compareAppVersion - currentList.size: "
                + currentList.size() + ", apiList.size: " + apiList.size());

        int result = 0;
        for (int i = 0; i < size; i++) {
            final int currentIndex = integerParseInt(currentList.get(i));
            final int apiIndex = integerParseInt(apiList.get(i));
            LogWrapper.showLog(Log.INFO,"CodeUtil", "compareAppVersion - currentIndex: "
                    + currentIndex + ", apiIndex: " + apiIndex);
            if (currentIndex != apiIndex) {
                result = currentIndex > apiIndex ? 1 : -1;
                break;
            }
        }

        return result;
    }


    public static int integerParseInt(@Nullable String input) {
        try {
            if (null == input) {
                return 0;
            }
            else {
                return Integer.parseInt(input);
            }
        }
        catch (Exception e) {
            return 0;
        }
    }

    ///

    @NonNull
    public static String readJsonStringFromResRawFile(@NonNull Context context, @RawRes int resId) {
        InputStream inputStream = null;
        try {
            final int bufferSize = 1024 * 8;
            final char[] buffer = new char[bufferSize];
            final Writer writer = new StringWriter();
            inputStream = context.getResources().openRawResource(resId);
            final Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int length;
            while ((length = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, length);
            }
            return writer.toString();
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil","Error on readJsonStringFromResRawFile", cause.fillInStackTrace());
            return "";
        }
        finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "CodeUtil","Error on inputStream.close()");
            }
        }
    }

    /// Log:

    @NonNull
    public static String byteArrayToHexString(@NonNull byte[] byteArray) {
        String result = "";
        for (byte byteData : byteArray) {
            final String hexStr = byteToHexString(byteData);
            result += hexStr + "-";
        }
        return result;
    }

    @NonNull
    public static String byteToHexString(@NonNull byte byteData) {
        return String.format("%02X", byteData);
    }


    @NonNull
    public static <T> String elementsToString(@NonNull List<T> itemList, @Nullable String newLine) {
        final String theNewLine = (null != newLine) ? newLine : "\n";
        String message = "";
        for (int i = 0; i < itemList.size(); i++) {
            message += itemList.get(i).toString();
            if (i < (itemList.size() - 1)) {
                message += ", " + theNewLine;
            }
        }
        return "[" + message + "]";
    }


    @NonNull
    public static String intItemsToString(@NonNull List<Integer> itemList, @Nullable String newLine) {
        final String theNewLine = (null != newLine) ? newLine : "";
        String message = "";
        for (int i = 0; i < itemList.size(); i++) {
            message += integerToDigitFormat(itemList.get(i), 4);
            if (i < (itemList.size() - 1)) {
                message += ", " + theNewLine;
            }
        }
        return "[" + message + "]";
    }

    @NonNull
    public static String integerToDigitFormat(int value, int digit) {
        return String.format("%0" + digit + "d", value);
    }

    // End of Log
}
