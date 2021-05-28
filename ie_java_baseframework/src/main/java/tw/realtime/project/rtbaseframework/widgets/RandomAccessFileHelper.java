package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.utils.CodeUtils;

public final class RandomAccessFileHelper {

    private final RandomAccessFile randomAccessFile;

    private byte[] byteArray;

    private String getLogTag() { return this.getClass().getSimpleName(); }

    public RandomAccessFileHelper(@NonNull final RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    public RandomAccessFileHelper initializeByteArray(final int numberOfBytes) {
        this.byteArray = new byte[numberOfBytes];
        LogWrapper.showLog(Log.INFO, getLogTag(), "initializeByteArray - numberOfBytes: " + numberOfBytes + ", byteArray.length: " + byteArray.length);
        return this;
    }

    public RandomAccessFileHelper readBytes(@NonNull final String propertyName) {
        try {
            randomAccessFile.read(byteArray);
            LogWrapper.showLog(Log.INFO, getLogTag(), "readBytes - [" + propertyName + "] FilePointer: " + randomAccessFile.getFilePointer() + ", Hex: " + CodeUtils.byteArrayToHexString(byteArray));
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "error on readBytes - [" + propertyName + "]", cause.fillInStackTrace());
        }
        return this;
    }

    @NonNull
    public String convertIntoString(@NonNull final String defaultValue) {
        if (null != byteArray) {
            return CodeUtils.byteArrayToString(byteArray, true, defaultValue);
        }
        else {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "convertIntoString - byteArray is null!!");
            return defaultValue;
        }
    }

    public int convertIntoInteger(@NonNull final ByteOrder byteOrder, final int defaultValue) {
        if (null != byteArray) {
            return CodeUtils.byteArrayToIntegerWithOrder(byteArray, byteOrder, defaultValue);
        }
        else {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "convertIntoInteger - byteArray is null!!");
            return defaultValue;
        }
    }

    public long convertIntoLong(@NonNull final ByteOrder byteOrder, final long defaultValue) {
        if (null != byteArray) {
            return CodeUtils.byteArrayToLongWithOrder(byteArray, byteOrder, defaultValue);
        }
        else {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "convertIntoLong - byteArray is null!!");
            return defaultValue;
        }
    }
}
