package tw.realtime.project.rtbaseframework.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class SharedPreferencesUtils {

    private static final String DEFAULT = "_default";

    /**
     * 設定key 值所對應的字串到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的字串
     */
    public static void updateStringInSharePreference (@NonNull Context context,
                                                      final String key,
                                                      final String value) {
        //if ( (null != key) && (!key.isEmpty()) && (null != value) && (!value.isEmpty()) ) {
        if ( (null != key) && (!key.isEmpty()) && (null != value) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
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
    @NonNull
    public static String retrieveStringFromSharePreference (@NonNull Context context, final String key) {
        String result = "";
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
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
    public static void updateBooleanInSharePreference (@NonNull Context context,
                                                       final String key,
                                                       final boolean value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
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
    public static boolean retrieveBooleanFromSharePreference (@NonNull Context context, final String key) {

        boolean result = false;
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
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
    public static void updateFloatInSharePreference (@NonNull Context context,
                                                     final String key,
                                                     final float value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            //Log.i(TAG, "updateFloatInSharePreference - key: " + key
            //        + ", value: " + value + ", pre: " + settings.getFloat(key, defaultValue) );
            final SharedPreferences.Editor spEditor = settings.edit();
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
    public static float retrieveFloatFromSharePreference (@NonNull Context context, final String key) {
        float result = 0f;
        if ( (null != key) && (!key.isEmpty()) ) {
            //Log.i(TAG, "retrieveFloatFromSharePreference - key: " + key);
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
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
    public static void updateIntegerInSharePreference (@NonNull Context context,
                                                       final String key,
                                                       final int value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
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
    public static int retrieveIntegerFromSharePreference (@NonNull Context context, final String key) {
        int result = 0;
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
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
    public static void updateLongInSharePreference (@NonNull Context context,
                                                    final String key,
                                                    final long value) {
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
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
    public static long retrieveLongFromSharePreference (@NonNull Context context, final String key) {
        long result = 0;
        if ( (null != key) && (!key.isEmpty()) ) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            result = settings.getLong(key, result);
        }
        return result;
    }

    //http://www.sherif.mobi/2012/05/string-arrays-and-object-arrays-in.html
    public static boolean saveStringArray(@NonNull Context context,
                                          final String key,
                                          @NonNull List<String> stringList) {

        if ( (null == key) || (key.isEmpty()) || (stringList.isEmpty()) ) {
            return false;
        }
        final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
        final SharedPreferences.Editor spEditor = settings.edit();
        spEditor.putInt(key + "_size" , stringList.size());
        for(int i = 0; i < stringList.size(); i++) {
            spEditor.putString(key + "_" + i, stringList.get(i));
        }
        return spEditor.commit();
    }

    @NonNull
    public static List<String> retrieveStringArray(@NonNull Context context,
                                                   final String key) {
        final List<String> resultList = new ArrayList<>();
        if ( (null == key) || (key.isEmpty()) ) {
            return resultList;
        }

        final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
        final int size = settings.getInt(key + "_size", 0);
        for (int i = 0; i < size; i++) {
            final String text = settings.getString(key + "_" + i, null);
            if ( (null != text) && (!text.isEmpty()) ) {
                resultList.add(text);
            }
        }
        return resultList;
    }

}
