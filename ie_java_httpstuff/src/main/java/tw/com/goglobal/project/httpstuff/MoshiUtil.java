package tw.com.goglobal.project.httpstuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class MoshiUtil {

    @NonNull
    public static Moshi newInstanceOfMoshi() {
        return new Moshi.Builder().build();
    }

    @Nullable
    public static <T> T decodeJsonByMoshi(@NonNull Moshi moshi, @NonNull String json, @NonNull Type type) {
        try {
            final JsonAdapter<T> adapter = moshi.adapter(type);
            return adapter.fromJson(json);
        } catch (Exception cause) {
            LogWrapper.showLog(android.util.Log.ERROR, "MoshiUtil", "Exception on adapter.fromJson()", cause);
            return null;
        }
    }

    @NonNull
    public static <T> Type getListType(@NonNull Class<T> tClass) {
        return Types.newParameterizedType(List.class, tClass);
    }
}

