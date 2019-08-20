package tw.com.goglobal.project.httpstuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.reflect.Type;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class MoshiUtil {

    @NonNull
    public Moshi newInstanceOfMoshi() {
        return new Moshi.Builder().build();
    }

    @Nullable
    public <T> T decodeJsonByMoshi(@NonNull Moshi moshi, @NonNull String json, @NonNull Type type) {
        try {
            final JsonAdapter<T> adapter = moshi.adapter(type);
            return adapter.fromJson(json);
        } catch (Exception e) {
            LogWrapper.showLog(android.util.Log.ERROR, "MoshiUtil", "Exception on adapter.fromJson()", e);
            return null;
        }
    }
}

