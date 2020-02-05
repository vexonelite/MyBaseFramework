package tw.com.goglobal.project.rxjava2.reactives;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;
import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public final class GsonJsonStringToObjectList<T>
        extends IeBaseConverter<String, List<T>> implements Function<String, List<T>> {

    private Class<T> tClass;

    public GsonJsonStringToObjectList(@NonNull Class<T> tClass) { this.tClass = tClass; }

    @NonNull
    @Override
    public List<T> apply(@NonNull String jsonString) throws IeRuntimeException {
        return convertIntoData(jsonString);
    }

    @NonNull
    @Override
    protected List<T> doConversion(@NonNull String input) throws Exception {
        final Gson gson = new Gson();
        final JsonParser parser = new JsonParser();
        final List<T> resultList = new ArrayList<>();
        final JsonArray jsonArray = parser.parse(input).getAsJsonArray();
        for(final JsonElement jsonElement : jsonArray) {
            final T item = gson.fromJson(jsonElement, tClass);
            resultList.add(item);
        }
        return resultList;
    }
}

