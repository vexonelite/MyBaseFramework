package tw.realtime.project.rtbaseframework.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


public final class SharedPreferencesUtils {

    private static final String DEFAULT = "_default";

    /**
     * 設定key 值所對應的字串到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的字串
     */
    public static void updateStringInSharePreference (
            @NonNull Context context,
            @NonNull final String key,
            @NonNull final String value) {
        if (!key.isEmpty()) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putString(key, value);
            spEditor.apply();
            //Log.i(TAG, "updateStringInSharePreference - key: " + key + ", value: " + value);
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils", "updateStringInSharePreference - key: $key, value: $value")
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的字串
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的字串
     */
    @NonNull
    public static String retrieveStringFromSharePreference(@NonNull Context context, @NonNull String key) {
        if (!key.isEmpty()) {
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils",  "retrieveStringFromSharePreference - key: $key")
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final String result = settings.getString(key, "");
            return (null != result) ? result : "";
        }
        else {
            return "";
        }
    }

    /**
     * 設定key 值所對應的布林值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的布林值
     */
    public static void updateBooleanInSharePreference(@NonNull Context context,
                                                      @NonNull final String key,
                                                      final boolean value) {
        if (!key.isEmpty()) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putBoolean(key, value);
            spEditor.apply();
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils", "updateBooleanInSharePreference - key: $key, value: $value")
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的布林值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的布林值
     */
    public static boolean retrieveBooleanFromSharePreference(@NonNull Context context, @NonNull String key) {
        if (!key.isEmpty()) {
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils",  "retrieveBooleanFromSharePreference - key: $key");
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            return settings.getBoolean(key, false);
        }
        else {
            return false;
        }
    }

    /**
     * 設定key 值所對應的浮點數值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的浮點數值
     */
    public static void updateFloatInSharePreference(@NonNull Context context,
                                                    @NonNull String key,
                                                    final float value) {
        if (!key.isEmpty()) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putFloat(key, value);
            spEditor.apply();
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils", "updateFloatInSharePreference - key: $key, value: $value");
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的浮點數值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的浮點數值
     */
    public static float retrieveFloatFromSharePreference(@NonNull Context context, @NonNull String key) {
        if (!key.isEmpty()) {
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils",  "retrieveFloatFromSharePreference - key: $key")
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            return settings.getFloat(key, 0f);
        }
        else {
            return 0f;
        }
    }

    /**
     * 設定key 值所對應的整數值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的整數值
     */
    public static void updateIntegerInSharePreference(@NonNull Context context,
                                                      @NonNull String key,
                                                      final int value) {
        if (!key.isEmpty()) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putInt(key, value);
            spEditor.apply();
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils", "updateIntegerInSharePreference - key: $key, value: $value")
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的整數值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的整數值
     */
    public static int retrieveIntegerFromSharePreference(@NonNull Context context, @NonNull String key) {
        if (!key.isEmpty()) {
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils",  "retrieveIntegerFromSharePreference - key: $key")
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            return settings.getInt(key, 0);
        }
        else {
            return 0;
        }
    }

    /**
     * 設定key 值所對應的長整數值到 SharedPreference 中
     * @param context
     * @param key   指定的 key 值
     * @param value key 值所對應的長整數值
     */
    public static void updateLongInSharePreference(@NonNull Context context,
                                                   @NonNull String key,
                                                   final long value) {
        if (!key.isEmpty()) {
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            final SharedPreferences.Editor spEditor = settings.edit();
            spEditor.putLong(key, value);
            spEditor.apply();
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils", "updateLongInSharePreference - key: $key, value: $value");
        }
    }

    /**
     * 從 SharedPreference 中取出 key 值所對應的長整數值
     * @param context
     * @param key 指定的 key 值
     * @return key 值所對應的長整數值
     */
    public static long retrieveLongFromSharePreference (@NonNull Context context, final String key) {
        if ( (null != key) && (!key.isEmpty()) ) {
            //LogWrapper.showLog(Log.INFO, "SharePreferenceUtils",  "retrieveLongFromSharePreference - key: $key")
            final SharedPreferences settings = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
            return settings.getLong(key, 0L);
        }
        else {
            return 0L;
        }
    }

    //http://www.sherif.mobi/2012/05/string-arrays-and-object-arrays-in.html
    public static boolean saveStringArray(@NonNull Context context,
                                          @NonNull String key,
                                          @NonNull List<String> stringList) {

        if ( (key.isEmpty()) || (stringList.isEmpty()) ) {
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
                                                   @NonNull String key) {
        final List<String> resultList = new ArrayList<>();
        if ((key.isEmpty()) ) {
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

