package tw.realtime.project.rtbaseframework.models;

import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.OptionDelegate;

public class DistrictData implements OptionDelegate<DistrictData> {

    private String districtName;
    private String zipCode;

    public DistrictData (String name, String code) {
        districtName = name;
        zipCode = code;
    }


    public String getDistrictName () {
        return districtName;
    }

    public String getDistrictZipCode () {
        return zipCode;
    }


    public boolean isSelected(String title) {
        return (null != districtName) && (null != title) && (districtName.equals(title));
    }

    @NonNull
    @Override
    public String getOptionTitle() {
        return districtName;
    }

    @NonNull
    @Override
    public DistrictData getHeldObject() {
        return this;
    }


    public static class Builder {
        private String bName;
        private String bZipCode;

        public Builder setDistrictName (String name) {
            bName = name;
            return this;
        }

        public Builder setDistrictZipCode (String name) {
            bZipCode = name;
            return this;
        }

        public DistrictData build() {
            return new DistrictData(this);
        }
    }

    private DistrictData(Builder builder) {
        districtName = builder.bName;
        zipCode = builder.bZipCode;
    }


    private static String getLogTag () {
        return DistrictData.class.getSimpleName();
    }


    public static DistrictData convertJSonObject (final JSONObject jsonObject) {
        Builder builder = new Builder();

        LogWrapper.showLog(Log.INFO, getLogTag(), "convertJSonObject - dataObject ==>");
        LogWrapper.showLongLog(Log.INFO, getLogTag(), jsonObject.toString());

        if (jsonObject.has("title")) {
            try {
                builder.setDistrictName(jsonObject.getString("title"));
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on jsonObject.getString(\"title\")!", e);
            }
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "convertJSonObject - NO \"title\"!");
        }

        if (jsonObject.has("zip")) {
            try {
                builder.setDistrictZipCode(jsonObject.getString("zip"));
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on jsonObject.getString(\"zip\")!", e);
            }
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "convertJSonObject - NO \"zip\"!");
        }

        return builder.build();
    }
}
