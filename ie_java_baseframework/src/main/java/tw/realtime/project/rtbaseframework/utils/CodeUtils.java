package tw.realtime.project.rtbaseframework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import tw.realtime.project.rtbaseframework.LogWrapper;

import static android.content.Context.INPUT_METHOD_SERVICE;


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
    public static int getColorFromResourceId(@NonNull Context context, @ColorRes int colorResId) {
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
    public static Drawable getDrawableFromResourceId(@NonNull Context context, @DrawableRes int drawableResID) {
        return ContextCompat.getDrawable(context, drawableResID);
    }

    /**
     * get the size of device's screen by excluding embedding control panel
     * (such as back button, home button, etc.)
     * <p> 取得螢幕的寬與高
     */
    @Nullable
    public static Point getRealScreenSize(@NonNull Context context) {
        try {
            final Point size = new Point();
            final WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            if (null != manager) {
                manager.getDefaultDisplay().getRealSize(size);
                return size;
            }
            else {
                LogWrapper.showLog(Log.ERROR, "CodeUtil", "getRealScreenSize - WindowManager is null!");
                return null;
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on getRealScreenSize!", cause);
            return null;
        }
    }

    // get the size of device's screen.
    @Nullable
    public static Point getScreenSize(@NonNull Context context) {
        try {
            final DisplayMetrics metrics = new DisplayMetrics();
            final WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            if (null != manager) {
                manager.getDefaultDisplay().getMetrics(metrics);
                final Point size = new Point();
                size.x = metrics.widthPixels;
                size.y = metrics.heightPixels;
                return size;
            }
            else {
                LogWrapper.showLog(Log.ERROR, "CodeUtil", "getScreenSize - WindowManager is null!");
                return null;
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on getScreenSize!", cause);
            return null;
        }
    }


    /**
     * ZXing 套件將字串產生 Qr 碼的影像
     * @param context
     * @param qrCodeContent 卻轉換的 Qr碼字串
     * @return Qr 碼影像; 若過程中出現例外，會回傳 Null
     */
    public static Bitmap generateQrCode(@NonNull Context context, @NonNull String qrCodeContent) {
        final Point screenSize = getRealScreenSize(context);
        // QR code 寬度
        final int qrImageSize = ((screenSize.x - 100) > 200) ? (screenSize.x - 100) : 200;
        // QR code Content codec
        final Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        // 文字內容為 UTF-8
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 設定 QR code 容錯率為 H
        // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        final MultiFormatWriter writer = new MultiFormatWriter();
        try {
            // 建立 QR code 的資料矩陣
            final BitMatrix bitMatrix = writer.encode(
                    qrCodeContent, BarcodeFormat.QR_CODE, qrImageSize, qrImageSize, hints);
            // ZXing 還可以生成其他形式條碼，如：BarcodeFormat.CODE_39、BarcodeFormat.CODE_93、BarcodeFormat.CODE_128、BarcodeFormat.EAN_8、BarcodeFormat.EAN_13...

            //建立點陣圖
            final Bitmap bitmap = Bitmap.createBitmap(qrImageSize, qrImageSize, Bitmap.Config.ARGB_8888);
            // 將 QR code 資料矩陣繪製到點陣圖上
            for (int y = 0; y < qrImageSize; y++) {
                for (int x = 0; x < qrImageSize; x++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on generateQrCode", cause);
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

    public static void sendEmailByIntent(@NonNull final Context context, @NonNull String emailAddress) {
        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
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
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on sendEmailByIntent", cause);
        }
    }

    /**
     * You should declare and setup your own FileProvider properly before involving the method!
     */
    public static void sendEmailWithSingleAttachment(
            @NonNull final Context context,
            @NonNull final String[] emailArray,
            @NonNull final String title,
            @NonNull final String message,
            @NonNull final String fileProviderAuthority,
            @NonNull final File attachmentFile) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, emailArray /* new String[] { "user1234@gmail.com" } */ );
        intent.putExtra(Intent.EXTRA_SUBJECT, title /* "Test Subject" */);
        intent.putExtra(Intent.EXTRA_TEXT, message /* "Test Message" */ );

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        final Uri attachmentUri = FileProvider.getUriForFile(context, fileProviderAuthority, attachmentFile);
        intent.putExtra(Intent.EXTRA_STREAM, attachmentUri);

        //Intent new_intent = Intent.createChooser(emailIntent, "Send email...");
        context.startActivity(intent);
    }

    /**
     *
     * [Attached Multiple images/PDF with Email](http://codeofandroid.blogspot.com/2016/02/email-with-attachment-in-android.html)
     * [Android multiple email attachments using Intent](https://stackoverflow.com/questions/2264622/android-multiple-email-attachments-using-intent)
     * [Sending email with attachment using FileProvider](https://stackoverflow.com/questions/53889141/sending-email-with-attachment-using-fileprovider)
     * [Cant send email with multiple attachment in android programmatically](https://stackoverflow.com/questions/27126220/cant-send-email-with-multiple-attachment-in-android-programmatically/27126664)
     * @param context
     * @param emailArray
     * @param title
     * @param message
     * @param fileProviderAuthority
     * @param fileList
     */
    public static void sendEmailWithMultipleAttachments(
            @NonNull final Context context,
            @NonNull final String[] emailArray,
            @NonNull final String title,
            @NonNull final String message,
            @NonNull final String fileProviderAuthority,
            @NonNull final List<File> fileList) {
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_EMAIL, emailArray /* new String[] { "user1234@gmail.com" } */ );
        intent.putExtra(Intent.EXTRA_SUBJECT, title /* "Test Subject" */);

        final ArrayList<String> messageList = new ArrayList<>();
        messageList.add(message);
        intent.putStringArrayListExtra(Intent.EXTRA_TEXT, messageList); // must use ArrayList; otherwise, Error log ==> Bundle: Key android.intent.extra.TEXT expected ArrayList<CharSequence> but value was a java.lang.String

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        final ArrayList<Uri> attachmentUriList = new ArrayList<>();
        for(final File attachment : fileList) {
            attachmentUriList.add(Uri.fromFile(attachment));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentUriList); // must use ArrayList!!

        //Intent new_intent = Intent.createChooser(emailIntent, "Send email...");
        context.startActivity(intent);
    }

    public static void makePhoneCallByIntent(@NonNull Context context, @NonNull String phoneNumber) {
        try {
            final String phoneNum = "tel:" + phoneNumber;
            final Uri uri = Uri.parse(phoneNum);
            final Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on makePhoneCallByIntent", cause);
        }
    }

    public static void openBrowser(@NonNull Context context, @NonNull String url) {
        LogWrapper.showLog(Log.INFO, "CodeUtils", "openBrowser - given url: " + url);
        if ( (!url.startsWith("http://")) && (!url.startsWith("https://")) ) {
            url = "http://" + url;
            LogWrapper.showLog(Log.INFO,"CodeUtil", "openBrowser - append 'http': " + url);
        }
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    @SuppressWarnings({"MissingPermission"})
    public static boolean hasInterNet(@NonNull Context context) {
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
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, "CodeUtils", "Exception on hasInterNet", cause);
                //return false;
            }
        }
        //LogWrapper.showLog(Log.INFO, "CodeUtils", "hasInterNet: " + hasInterNet);
        return hasInterNet;
    }

    @SuppressWarnings({"MissingPermission"})
    public static String getNetworkAccessType(@NonNull Context context) {
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
    public static boolean containsEmoji(@NonNull String source) {
        if (source.isEmpty()) { return false; }

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
        return isEmoji;
    }

    public static int compareAppVersion(
            @Nullable final String currentVersion,
            @Nullable final String apiVersion) throws IllegalArgumentException {
        if (null == currentVersion) { throw new IllegalArgumentException("currentVersion cannot be null"); }
        if (currentVersion.isEmpty()) { throw new IllegalArgumentException("currentVersion cannot be empty"); }
        if (null == apiVersion) { throw new IllegalArgumentException("apiVersion cannot be null"); }
        if (apiVersion.isEmpty()) { throw new IllegalArgumentException("apiVersion cannot be empty"); }
        LogWrapper.showLog(Log.INFO, "IeUtils", "compareAppVersion - currentVersion: "
                + currentVersion + ", apiVersion: " + apiVersion);

        final String[] currentArray = currentVersion.split("\\.");
        final String[] apiArray = apiVersion.split("\\.");
        final int currentSize = currentArray.length;
        if (currentSize == 0) { throw new IllegalArgumentException("currentSize is 0"); }
        final int apiSize =  apiArray.length;
        if (apiSize == 0) { throw new IllegalArgumentException("apiSize is 0"); }
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
            if (null == input) { return 0; }
            else { return Integer.parseInt(input); }
        }
        catch (Exception cause) { return 0; }
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
    public static String byteArrayToHexString(@NonNull final byte[] byteArray) {
        final StringBuilder builder = new StringBuilder();
        //String result = "";
        for (byte byteData : byteArray) {
            final String hexStr = byteToHexString(byteData);
            //result += hexStr + "-";
            builder.append(hexStr);
        }
        //return result;
        return builder.toString();
    }

    @NonNull
    public static String byteArrayToString(
            @NonNull final byte[] byteArray, final boolean characterEncoding, @NonNull final String defaultValue) {
        final byte zeroByte = (byte)0x00;

        int index = 0;
        for (int i = 0; i< byteArray.length; i++) {
            final byte theByte = byteArray[i];
            if (theByte == zeroByte) {
                index = i;
                break;
            }
        }

        final byte[] cloneByteArray = new byte[index];
        for (int i = 0; i< cloneByteArray.length; i++) {
            cloneByteArray[i] = byteArray[i];
        }

        try {
            return characterEncoding
                    ? new String(cloneByteArray, StandardCharsets.UTF_8)
                    : new String(cloneByteArray);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "error on byteArrayToString", cause.fillInStackTrace());
            return defaultValue;
        }
    }

    @NonNull
    public static String byteToHexString(final byte byteData) { return String.format("%02X", byteData); }

    public static int byteArrayToIntegerWithOrder(
            @NonNull final byte[] input, @NonNull final ByteOrder byteOrder, final int defaultValue) {
        try {
//            final ByteBuffer byteBuffer = ByteBuffer.wrap(input);
//            java.util.logging.Logger.getLogger("CodeUtil").log(Level.INFO, "byteArrayToIntegerWithOrder - byteBuffer.order(): " + byteBuffer.order());
//            if (byteBuffer.order() != byteOrder) {
//                byteBuffer.order(byteOrder);
//                java.util.logging.Logger.getLogger("CodeUtil").log(Level.INFO, "byteArrayToIntegerWithOrder - change order: " + byteBuffer.order());
//            }
//            final byte[] output = Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
//            java.util.logging.Logger.getLogger("CodeUtil").log(Level.INFO, "byteArrayToIntegerWithOrder - input: " + byteArrayToHexString(input) + ", output: " + byteArrayToHexString(output));
            //this statement must be executed as a stream
            final int value = ByteBuffer.wrap(input).order(byteOrder).getInt();
            java.util.logging.Logger.getLogger("CodeUtil").log(Level.INFO, "byteArrayToIntegerWithOrder - int value: " + value);
            return value;
        }
        catch (Exception cause) { return defaultValue; }
    }

    public static long byteArrayToLongWithOrder(
            @NonNull final byte[] input, @NonNull final ByteOrder byteOrder, final long defaultValue) {
        try {
            //this statement must be executed as a stream
            final long value = ByteBuffer.wrap(input).order(byteOrder).getLong();
            //java.util.logging.Logger.getLogger("IeUtils").log(Level.INFO, "byteArrayToLongWithOrder - long value: " + value);
            return value;
        }
        catch (Exception cause) { return defaultValue; }
    }

    @NonNull
    public static <T> String elementsToString(@NonNull List<T> itemList, @Nullable String newLine) {
        final String theNewLine = (null != newLine) ? newLine : "\n";
        //String message = "";
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < itemList.size(); i++) {
            //message += itemList.get(i).toString();
            builder.append(itemList.get(i).toString());
            if (i < (itemList.size() - 1)) {
                //message += ", " + theNewLine;
                builder.append(", ").append(theNewLine);
            }
        }
        //return "[" + message + "]";
        return "[" + builder.toString() + "]";
    }

    @NonNull
    public static String intItemsToString(@NonNull List<Integer> itemList, @Nullable String newLine) {
        final String theNewLine = (null != newLine) ? newLine : "";
        //String message = "";
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < itemList.size(); i++) {
            //message += integerToDigitFormat(itemList.get(i), 4);
            builder.append(integerToDigitFormat(itemList.get(i), 4));
            if (i < (itemList.size() - 1)) {
                //message += ", " + theNewLine;
                builder.append(", ").append(theNewLine);
            }
        }

        //return "[" + message + "]";
        return "[" + builder.toString() + "]";
    }

    @NonNull
    public static List<String> stringSplitByDelimiter(@Nullable final String input, @Nullable final String delimiter) {
        final List<String> itemList = new ArrayList<>();
        if ( (null == input) || (input.isEmpty()) ) {
            java.util.logging.Logger.getLogger("CodeUtils").log(Level.SEVERE, "stringSplitByDelimiter - input is either null or empty!!");
            return itemList;

        }
        if (null == delimiter) {
            java.util.logging.Logger.getLogger("CodeUtils").log(Level.SEVERE, "stringSplitByDelimiter - delimiter is null!!");
            return itemList;
        }

        try {
            final String[] stringArray = input.split(delimiter);
            return Arrays.asList(stringArray);
        }
        catch (Exception cause) {
            java.util.logging.Logger.getLogger("CodeUtils").log(Level.SEVERE, "stringSplitByDelimiter - error on String.split(): " + cause.getLocalizedMessage());
            return itemList;
        }
    }

    @NonNull
    public static <T> String itemListToString(@Nullable final List<T> itemList, @Nullable final String delimiter) {
        if ( (null == itemList) || (itemList.isEmpty()) ) {
            java.util.logging.Logger.getLogger("CodeUtils").log(Level.SEVERE, "itemListToString - itemList is either null or empty!!");
            return "";

        }
        if (null == delimiter) {
            java.util.logging.Logger.getLogger("CodeUtils").log(Level.SEVERE, "itemListToString - delimiter is null!!");
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < itemList.size(); i++) {
            builder.append(itemList.get(i).toString());
            if (i < (itemList.size() - 1)) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }

    @NonNull
    public static String integerToDigitFormat(int value, int digit) {
        return String.format("%0" + digit + "d", value);
    }

    // Returns a char sequence with characters in reversed order.
    // https://www.baeldung.com/java-reverse-string
    @NonNull
    public static String reverseStraightForward(@NonNull String input) {
        if (input.isEmpty()) { return input; }
        String output = "";
        for (int i = input.length() - 1; i >= 0; i--) {
            output = output + input.charAt(i);
        }
        return output;
    }

    // Returns a char sequence with characters in reversed order.
    // https://www.baeldung.com/java-reverse-string
    @NonNull
    public static String reverseUsingStringBuilder(@NonNull String input) {
        if (input.isEmpty()) { return input; }
        return new StringBuilder(input).reverse().toString();
    }

    /** https://stackoverflow.com/questions/9461306/how-to-change-spaces-to-underscore-and-make-string-case-insensitive */
    @NonNull
    public static String replaceAllSpaceWithUnderscore(@Nullable final String source) {
        if ((null == source) || (source.isEmpty())) { return ""; }
        return source.replaceAll("\\s+", "_");
    }

    // End of Log

    //

    @NonNull
    public static <T> List<T> arrayToList(@NonNull T[] input) {
        return Arrays.asList(input);
    }

    // https://www.techiedelight.com/convert-list-to-array-java/
    // https://www.techiedelight.com/creating-generic-array-java/
    // https://stackoverflow.com/questions/18581002/how-to-create-a-generic-array
    @NonNull
    public static <T> T[] listToArray(@NonNull List<T> input, @NonNull Class<T> type) {
        //return input.toArray(new T[input.size()]); --> cannot do this in Java
        final T[] array = (T[]) java.lang.reflect.Array.newInstance(type, input.size());
        return input.toArray(array);
    }

    ///

    public static void showSoftKeyboard(@NonNull Activity activity, @NonNull View view) {
        final View currentFocusedView = activity.getCurrentFocus();
        if (null != currentFocusedView) { currentFocusedView.clearFocus(); }

        /*
         * Ref:
         * https://github.com/codepath/android_guides/wiki/Working-with-the-Soft-Keyboard
         */
//        if (view.requestFocus()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                LogWrapper.showLog(Log.INFO, "CodeUtils", "showSoftKeyboard - >= Android 11!!");
//                final WindowInsetsController windowInsetsController = view.getWindowInsetsController();
//                if (null != windowInsetsController) {
//                    windowInsetsController.show(WindowInsets.Type.ime());
//                    LogWrapper.showLog(Log.INFO, "CodeUtils", "showSoftKeyboard - done!!");
//                }
//                else {
//                    LogWrapper.showLog(Log.WARN, "CodeUtils", "showSoftKeyboard - windowInsetsController is null!!");
//                }
//            }
//            else {
//                LogWrapper.showLog(Log.INFO, "CodeUtils", "showSoftKeyboard - < Android 11!!");
//                final InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (null != inputMethodManager) {
//                    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//                    LogWrapper.showLog(Log.INFO, "CodeUtils", "showSoftKeyboard - done!!");
//                }
//                else { LogWrapper.showLog(Log.WARN, "CodeUtils", "showSoftKeyboard - InputMethodManager is null!!"); }
//            }
//        }
//        else { LogWrapper.showLog(Log.WARN, "CodeUtils", "showSoftKeyboard - Fail to view.requestFocus()!!"); }

        if (view.requestFocus()) {
            final InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != inputMethodManager) {
                //inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT); // --> does not work out on Android 11

                inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                LogWrapper.showLog(Log.INFO, "IeUtils", "showSoftKeyboard - done!!");
            }
            else { LogWrapper.showLog(Log.WARN, "IeUtils", "showSoftKeyboard - InputMethodManager is null!!"); }
        }
        else { LogWrapper.showLog(Log.WARN, "IeUtils", "showSoftKeyboard - Fail to view.requestFocus()!!"); }
    }

    public static void hideSoftKeyboard(@NonNull Activity activity) {
        if (null == activity.getCurrentFocus()) { return; }
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        if (null == inputMethodManager) {
            LogWrapper.showLog(Log.WARN, "CodeUtils", "hideSoftKeyboard - InputMethodManager is null!!");
            return;
        }
        final View currentFocus = activity.getCurrentFocus();
        if (null != currentFocus) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
            //LogWrapper.showLog(Log.INFO, "CodeUtils", "hideSoftKeyboard - done!!");
        }
    }

    // [start] added by elite_lin - 2021/03/25
    public static void showSoftKeyboard(@NonNull final View view, @NonNull final Context context) {
        if (view.requestFocus()) {
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != inputMethodManager) {
                //inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT); // --> does not work out on Android 11

                inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                LogWrapper.showLog(Log.INFO, "IeUtils", "showSoftKeyboard - done!!");
            }
            else { LogWrapper.showLog(Log.WARN, "IeUtils", "showSoftKeyboard - InputMethodManager is null!!"); }
        }
        else { LogWrapper.showLog(Log.WARN, "IeUtils", "showSoftKeyboard - Fail to view.requestFocus()!!"); }
    }

    public static void hideSoftKeyboard(@NonNull final View view, @NonNull final Context context) {
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null == inputMethodManager) {
            LogWrapper.showLog(Log.WARN, "IeUtils", "hideSoftKeyboard - InputMethodManager is null!!");
            return;
        }
        if (view.hasFocus()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
            LogWrapper.showLog(Log.INFO, "IeUtils", "hideSoftKeyboard - done!!");
        }
        else {
            LogWrapper.showLog(Log.WARN, "IeUtils", "hideSoftKeyboard - view does not has Focus!!");
        }
    }
    // [end] added by elite_lin - 2021/03/25
}
