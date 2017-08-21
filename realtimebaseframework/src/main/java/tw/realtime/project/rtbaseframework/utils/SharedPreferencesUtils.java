package tw.realtime.project.rtbaseframework.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static final String DEFAULT = "_default";

    /**
     * 設定key 值所對應的字串到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的字串
     */
    public static void updateStringInSharePreference (Context context,
                                                      final String key,
                                                      final String value) {
        //if ( (null != key) && (!key.isEmpty()) && (null != value) && (!value.isEmpty()) ) {
        if ( (null != key) && (!key.isEmpty()) && (null != value) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putString(key, value);
            spEditor.apply();
            //Log.i(TAG, "updateStringInSharePreference - key: " + key + ", value: " + value);
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的字串
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的字串
     */
    public static String retrieveStringFromSharePreference (Context context, final String key) {
        String result = "";
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            result = settings.getString(key, "");
        }
        return result;
    }

    /**
     * 設定key 值所對應的布林值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的布林值
     */
    public static void updateBooleanInSharePreference (Context context,
                                                       final String key,
                                                       final boolean value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putBoolean(key, value);
            spEditor.apply();
            //Log.i(TAG, "updateBooleanInSharePreference - post: " + settings.getBoolean(key, defaultValue));
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的布林值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的布林值
     */
    public static boolean retrieveBooleanFromSharePreference (Context context, final String key) {

        boolean result = false;
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            result = settings.getBoolean(key, false);
        }
        return result;
    }

    /**
     * 設定key 值所對應的浮點數值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的浮點數值
     */
    public static void updateFloatInSharePreference (Context context,
                                                     final String key,
                                                     final float value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            //Log.i(TAG, "updateFloatInSharePreference - key: " + key
            //        + ", value: " + value + ", pre: " + settings.getFloat(key, defaultValue) );
            SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putFloat(key, value);
            spEditor.apply();
            //Log.i(TAG, "updateFloatInSharePreference - post: " + settings.getFloat(key, defaultValue));
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的浮點數值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的浮點數值
     */
    public static float retrieveFloatFromSharePreference (Context context, final String key) {
        float result = 0f;
        if ( (null != key) && (!key.isEmpty()) ) {
            //Log.i(TAG, "retrieveFloatFromSharePreference - key: " + key);
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            result = settings.getFloat(key, 0f);
            //Log.i(TAG, "retrieveBooleanFromSharePreference - key: " + key);
        }
        return result;
    }

    /**
     * 設定key 值所對應的整數值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的整數值
     */
    public static void updateIntegerInSharePreference (Context context,
                                                       final String key,
                                                       final int value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putInt(key, value);
            spEditor.apply();
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的整數值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的整數值
     */
    public static int retrieveIntegerFromSharePreference (Context context, final String key) {
        int result = 0;
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            result = settings.getInt(key, 0);
        }
        return result;
    }

    /**
     * 設定key 值所對應的長整數值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的長整數值
     */
    public static void updateLongInSharePreference (Context context,
                                                    final String key,
                                                    final long value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putLong(key, value);
            spEditor.apply();
            //Log.i(TAG, "updateBooleanInSharePreference - post: " + settings.getBoolean(key, defaultValue));
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的長整數值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的長整數值
     */
    public static long retrieveLongFromSharePreference (Context context, final String key) {
        long result = 0;
        if ( (null != key) && (!key.isEmpty()) ) {
            SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            result = settings.getLong(key, result);
        }
        return result;
    }
}
