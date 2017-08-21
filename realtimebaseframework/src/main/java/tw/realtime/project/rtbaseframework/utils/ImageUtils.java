package tw.realtime.project.rtbaseframework.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import tw.realtime.project.rtbaseframework.LogWrapper;


public class ImageUtils {

    /**
     * @param context				Your activity.
     * @param imageUri				The Uri stands for where the specified image resides.
     */
    public static Bitmap getBitmapFromUri (Context context,
                                           Uri imageUri,
                                           int desiredWidth,
                                           int desiredHeight) throws Exception {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //First decode with 'inJustDecodeBounds = true' to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream (
                    context.getContentResolver().openInputStream(imageUri),
                    null, options);

            if ( (desiredWidth > 0) && (desiredHeight > 0) ) {

                if (options.outWidth > options.outHeight) {
                    if (options.outWidth > desiredWidth) {
                        float ratio = ((float)desiredWidth) / ((float)options.outWidth);
                        int newHeight = (int)( ( (float)options.outHeight ) * ratio );
                        LogWrapper.showLog(Log.INFO, "ImageUtils", "getBitmapFromUri"
                                + "\n--> desiredWidth = " + desiredWidth
                                + ", newHeight = " + newHeight);
                        options.inSampleSize = ImageUtils.calculateInSampleSize (
                                options, desiredWidth, newHeight);
                    }
                } else {
                    if (options.outHeight > desiredWidth) {
                        float ratio = ((float)desiredWidth) / ((float)options.outHeight);
                        int newWidth = (int)( ( (float)options.outWidth ) * ratio );
                        LogWrapper.showLog(Log.INFO, "ImageUtils", "getBitmapFromUri"
                                + "\n--> newWidth = " + newWidth
                                + ", desiredHeight = " + desiredWidth);
                        options.inSampleSize = ImageUtils.calculateInSampleSize (
                                options, newWidth, desiredWidth);
                    }
                }
            }

            //Decode bitmap with the specific option
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeStream (
                    context.getContentResolver().openInputStream(imageUri),
                    null, options
            );
            return bitmap;
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on getBitmapFromUri!", e);
            throw e;
        }
    }

    public static Bitmap resizeBitmap (Bitmap inputBitmap,
                                       int desiredBitmapWidth,
                                       int desiredBitmapHeight) throws Exception {

        try {
            int bmpWidth = inputBitmap.getWidth();
            int bmpHeight = inputBitmap.getHeight();
            float scaleWidth = ((float) desiredBitmapWidth) / ((float) bmpWidth);
            float scaleHeight = ((float) desiredBitmapHeight) / ((float) bmpHeight);
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    inputBitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
            return resizedBitmap;
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on resizeBitmap!", e);
            throw e;
        }
    }

    public static Bitmap rotateBitmap (Bitmap inputBitmap, float rotateDegree) throws Exception {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateDegree);
            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg,width,height,true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(inputBitmap , 0, 0,
                    inputBitmap.getWidth(), inputBitmap.getHeight(), matrix, true);
            return rotatedBitmap;
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on rotateBitmap!", e);
            throw e;
        }
    }

    /**
     * @param options	Your BitmapFactory.Options
     * @param reqWidth  The desired width of Bitmap.
     * @param reqHeight	The desired height of Bitmap.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( (height > reqHeight) || (width > reqWidth) )  {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ( ( (halfHeight / inSampleSize) > reqHeight) &&
                    ( (halfWidth / inSampleSize) > reqWidth) ) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Return the Orientation of an image;
     * One of four possible outcomes (0, 90, 180, and 270) will be returned.
     * @param context	Your activity.
     * @param imageUri	A URI that stands for a path where your image resides.
     */
    public static int getImageOrientation(Context context, Uri imageUri) {
        String path = imageUri.getPath();
        int degree = getImageRawOrientation(context, imageUri);
        if (degree < 0) {
            degree = readImgOrientationFromExif(path);
        }
        return degree;
    }

    /**
     * Return the orientation by query the external media.
     * If the query returns a valid result,
     * then one of four possible outcomes (0, 90, 180, and 270)
     * will be returned; otherwise, '-1' will be returned.
     * @param context	Your activity.
     * @param imageUri	A URI that stands for a path where your image resides.
     */
    private static int getImageRawOrientation (Context context, final Uri imageUri) {
        int result = -1;
        Cursor cursor = null;
        String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
        try {
            cursor = context.getContentResolver()
                    .query(imageUri, projection, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    result = cursor.getInt(0);
                }
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on getImageRawOrientation()!", e);
        }
        finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * Return the Orientation of a specified image from its Exif.
     * One of four possible outcomes (0, 90, 180, and 270) will be returned.
     */
    private static int readImgOrientationFromExif(String path) {

        int degree = -1;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
            return degree;
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on readImgOrientationFromExif()!", e);
            return -1;
        }
    }

    public static Point getNewImageSize (final int srcWidth,
                                         final int srcHeight,
                                         int maxWidth,
                                         int maxHeight) throws Exception {

        try {
            if (maxWidth == 0) {
                maxWidth  = srcWidth;
            }

            if (maxHeight == 0) {
                maxHeight = srcHeight;
            }

            // Calculate ratio of desired maximum sizes and original sizes.
            double widthRatio = ((double) maxWidth) / ((double) srcWidth);
            double heightRatio = ((double) maxHeight) / ((double) srcHeight);

            // Ratio used for calculating new image dimensions.
            double ratio = Math.min(widthRatio, heightRatio);

            // Calculate new image dimensions.
            int newWidth  = (int)( ((double) srcWidth) * ratio);
            int newHeight  = (int)( ((double) srcHeight) * ratio);
            return new Point(newWidth, newHeight);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on getNewImageSize!", e);
            throw e;
        }
    }



    public static Point getNewImageSizeWRSwidth (final int srcWidth,
                                                 final int srcHeight,
                                                 final int alignWidth) throws Exception {

        try {
            if (srcWidth == srcHeight) {
                return new Point(alignWidth, alignWidth);
            }
            else {
                float ratio = ((float) srcWidth) / ((float) srcHeight);
                int newWidth = alignWidth;
                int newHeight = (int)( ((float) newWidth) / ratio);
                return new Point(newWidth, newHeight);
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on getNewImageSizeWRSwidth!", e);
            throw e;
        }
    }

    public static Point getNewImageSizeWRSheight (final int srcWidth,
                                                  final int srcHeight,
                                                  final int alignHeight) throws Exception {

        try {
            if (srcWidth == srcHeight) {
                return new Point(alignHeight, alignHeight);
            }
            else {
                float ratio = ((float) srcWidth) / ((float) srcHeight);
                int newHeight = alignHeight;
                int newWidth = (int)( ((float) newHeight) / ratio);
                return new Point(newWidth, newHeight);
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "ImageUtils", "Exception on getNewImageSizeWRSheight!", e);
            throw e;
        }
    }

    /**
     * Convert the given image Bitmap into bytes[].
     * @param srcBmp
     * @param isJPEG
     * @param compressedQuality
     * @return
     */
    public static byte[] compressAndConvertImageToByteFrom(final Bitmap srcBmp,
                                                           final boolean isJPEG,
                                                           final int compressedQuality){

        Bitmap.CompressFormat format = (isJPEG) ? Bitmap.CompressFormat.JPEG: Bitmap.CompressFormat.PNG;
        int quality = ( (compressedQuality < 0) || (compressedQuality > 100) )
                ? 90 : compressedQuality;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        srcBmp.compress(format, quality, stream);
        return stream.toByteArray();
    }

    /**
     * Returns a new Bitmap copy with a crop effect depending on the crop anchor given. 0.5f is like
     * {@link android.widget.ImageView.ScaleType#CENTER_CROP}. The crop anchor will be be nudged
     * so the entire cropped bitmap will fit inside the src. May return the input bitmap if no
     * scaling is necessary.
     *
     *
     * Example of changing verticalCenterPercent:
     *   _________            _________
     *  |         |          |         |
     *  |         |          |_________|
     *  |         |          |         |/___0.3f
     *  |---------|          |_________|\
     *  |         |<---0.5f  |         |
     *  |---------|          |         |
     *  |         |          |         |
     *  |         |          |         |
     *  |_________|          |_________|
     *
     * @param src original bitmap of any size
     * @param w desired width in px
     * @param h desired height in px
     * @param horizontalCenterPercent determines which part of the src to crop from. Range from 0
     *                                .0f to 1.0f. The value determines which part of the src
     *                                maps to the horizontal center of the resulting bitmap.
     * @param verticalCenterPercent determines which part of the src to crop from. Range from 0
     *                              .0f to 1.0f. The value determines which part of the src maps
     *                              to the vertical center of the resulting bitmap.
     * @return a copy of src conforming to the given width and height, or src itself if it already
     *         matches the given width and height
     */
    public static Bitmap crop(final Bitmap src, final int w, final int h,
                              final float horizontalCenterPercent, final float verticalCenterPercent) {
        if (horizontalCenterPercent < 0 || horizontalCenterPercent > 1 || verticalCenterPercent < 0
                || verticalCenterPercent > 1) {
            throw new IllegalArgumentException(
                    "horizontalCenterPercent and verticalCenterPercent must be between 0.0f and "
                            + "1.0f, inclusive.");
        }
        final int srcWidth = src.getWidth();
        final int srcHeight = src.getHeight();
        // exit early if no resize/crop needed
        if (w == srcWidth && h == srcHeight) {
            return src;
        }
        final Matrix m = new Matrix();
        final float scale = Math.max(
                (float) w / srcWidth,
                (float) h / srcHeight);
        m.setScale(scale, scale);
        final int srcCroppedW, srcCroppedH;
        int srcX, srcY;
        srcCroppedW = Math.round(w / scale);
        srcCroppedH = Math.round(h / scale);
        srcX = (int) (srcWidth * horizontalCenterPercent - srcCroppedW / 2);
        srcY = (int) (srcHeight * verticalCenterPercent - srcCroppedH / 2);
        // Nudge srcX and srcY to be within the bounds of src
        srcX = Math.max(Math.min(srcX, srcWidth - srcCroppedW), 0);
        srcY = Math.max(Math.min(srcY, srcHeight - srcCroppedH), 0);
        final Bitmap cropped = Bitmap.createBitmap(src, srcX, srcY, srcCroppedW, srcCroppedH, m,
                true /* filter */);

        LogWrapper.showLog(Log.INFO, "ImageUtils", "IN centerCrop, src W/H = " + srcWidth + "/ " + srcHeight
                    + ", desired W/H = " + w + "/" + h + ", src X/Y = "
                    + srcX + "/" + srcY + ", inner W/H = " + srcCroppedW + "/" + srcCroppedH
                    + ", scale = " + scale + ", result W/H = " + cropped.getWidth() + "/" + cropped.getHeight());
        if ( (w != cropped.getWidth() || h != cropped.getHeight()) ) {
            LogWrapper.showLog(Log.WARN, "ImageUtils", "Warning: last center crop violated assumptions.");
        }
        return cropped;
    }

    /**
     * @param srcBmp	The input Bitmap Image.
     * @param bmpSize The desired size of image in pixels.
     */
    public static Bitmap shapeBitmapToCircle (Bitmap srcBmp, int bmpSize) {

        final Bitmap outputBmp = Bitmap.createBitmap(bmpSize, bmpSize,
                Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(outputBmp);

        //final int color = Color.RED;
        final int color = Color.WHITE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmpSize, bmpSize);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBmp, rect, rect, paint);

        return outputBmp;
    }
}
