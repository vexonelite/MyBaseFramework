package tw.com.goglobal.project.rxjava2.reactives;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;
import tw.com.goglobal.project.httpstuff.MoshiUtil;
import tw.realtime.project.rtbaseframework.apis.converters.IeBaseConverter;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;


public abstract class JsonStringToObjectList<T>
        extends IeBaseConverter<String, List<T>> implements Function<String, List<T>> {

    private Class<T> tClass;

    public JsonStringToObjectList(@NonNull Class<T> tClass) { this.tClass = tClass; }

    @NonNull
    @Override
    public List<T> apply(@NonNull String jsonString) throws IeRuntimeException {
        return convertIntoData(jsonString);
    }

    @NonNull
    @Override
    protected List<T> doConversion(@NonNull String input) throws Exception {
        final Type type = MoshiUtil.getListType(tClass);
        final List<T> resultList = MoshiUtil.decodeJsonByMoshi(MoshiUtil.newInstanceOfMoshi(), input, type);
        if (null == resultList) { throw new IeRuntimeException("resultList is null", ErrorCodes.Base.JSON_PARSING_FAILURE); }
        return resultList;
    }
}
