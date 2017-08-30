package tw.realtime.project.rtbaseframework.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Created by vexonelite on 2017/6/5.
 */

public class CodeUtils {

    /**
     * 將日期字串轉換成 Date 物件
     * @param dateString 日期字串
     * @param dateFormat 指定的日期格式 (若是空字串或 Null，會使用預設值)
     * @return Date 物件
     */
    public static Date convertStringToDate (String dateString, String dateFormat) {
        try {
            if ( (null == dateFormat) || (dateFormat.isEmpty()) ) {
                dateFormat = "yyyy-MM-dd hh:mm:ss";
            }
            Locale locale = Locale.getDefault();
            //LogWrapper.showLog(Log.INFO, "CodeUtil", "convertStringToDate: " + locale);
            SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, locale);
            return fmt.parse(dateString);

        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on convertStringToDate", e);
            return null;
        }
    }

    /**
     * 將Date 物件轉換成日期字串
     * @param date Date 物件
     * @param dateFormat 指定的日期格式 (若是空字串或 Null，會使用預設值)
     * @return 日期字串
     */
    public static String convertDateToString (Date date, String dateFormat) {
        try {
            if ( (null == dateFormat) || (dateFormat.isEmpty()) ) {
                dateFormat = "yyyy-MM-dd hh:mm:ss";
            }
            Locale locale = Locale.getDefault();
            //LogWrapper.showLog(Log.INFO, "CodeUtil", "convertDateToString: " + locale);
            SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, locale);
            return fmt.format(date);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on convertDateToString", e);
            return null;
        }
    }

    public static String getDecimalFormatString (String inputPrice) {
        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);
        try {
            float fPrice = Float.parseFloat(inputPrice);
            return fmt.format(fPrice);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on getDecimalFormatString", e);
            return null;
        }
    }

    /**
     * 將 R.color.resID 轉成 int color
     * @param context
     * @param resID
     * @return
     */
    public static int getColorFromResourceId (Context context, int resID) {
        return (Build.VERSION.SDK_INT < 23)
                ? context.getResources().getColor(resID)
                : context.getResources().getColor(resID, null);
    }

    /**
     * 將 R.drawable.resID 轉成 Drawable drawable
     * @param context
     * @param resID
     * @return
     *
     * Ref: https://stackoverflow.com/questions/29041027/android-getresources-getdrawable-deprecated-api-22
     */
    public static Drawable getDrawableFromResourceId (Context context, int resID) {
        return ContextCompat.getDrawable(context, resID);
//        if (Build.VERSION.SDK_INT >= 21) {
//            return Resources.getDrawable(resID, Theme);
//        }
    }

    /**
     * 取得螢幕的寬與高
     * @param context
     * @return
     */
    public static Point getRealScreenSize (Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        try {
            Point size = new Point();
            manager.getDefaultDisplay().getRealSize(size);
            return size;
        }
        catch (NoSuchMethodError e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on getRealScreenSize", e);
            Point size = new Point();
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            size.x = metrics.widthPixels;
            size.y = metrics.heightPixels;
            return size;
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
}
