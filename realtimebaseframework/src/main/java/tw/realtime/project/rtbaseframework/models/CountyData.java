package tw.realtime.project.rtbaseframework.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.interfaces.OptionDelegate;


public class CountyData implements OptionDelegate {

    private String countyName;
    private List<DistrictData> districtList;


    public String getCountyName () {
        return countyName;
    }

    public List<DistrictData> getDistrictList () {
        return districtList;
    }


    @Override
    public String getTitle() {
        return countyName;
    }

    @Override
    public boolean isSelected(String title) {
        return (null != countyName) && (null != title) && (countyName.equals(title));
    }


    public static class Builder {
        private String bName;
        private List<DistrictData> bDistrictList;

        public Builder setCountyName (String name) {
            bName = name;
            return this;
        }

        public Builder setDistrictList (List<DistrictData> list) {
            bDistrictList = list;
            return this;
        }

        public CountyData build() {
            return new CountyData(this);
        }
    }

    private CountyData(Builder builder) {
        countyName = builder.bName;
        districtList = builder.bDistrictList;
    }


    private static String getLogTag () {
        return CountyData.class.getSimpleName();
    }


    public static CountyData convertJSonObject (final JSONObject jsonObject, String defaultDistrict) {
        Builder builder = new Builder();

        LogWrapper.showLog(Log.INFO, getLogTag(), "convertJSonObject - dataObject ==>");
        LogWrapper.showLongLog(Log.INFO, getLogTag(), jsonObject.toString());

        if (jsonObject.has("title")) {
            try {
                builder.setCountyName(jsonObject.getString("title"));
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on jsonObject.getString(\"title\")!", e);
            }
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "convertJSonObject - NO \"title\"!");
        }

        List<DistrictData> list = new ArrayList<>();
        DistrictData defaultDistrictData = new DistrictData.Builder()
                .setDistrictName(defaultDistrict)
                .setDistrictZipCode("")
                .build();
        list.add(defaultDistrictData);

        if (jsonObject.has("sub")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("sub");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject subObject = jsonArray.getJSONObject(i);
                    DistrictData districtData = DistrictData.convertJSonObject(subObject);
                    if (null != districtData) {
                        list.add(districtData);
                    }
                }
                LogWrapper.showLog(Log.INFO, getLogTag(), "jsonArray length: " + jsonArray.length() + ", list size: " + list.size());
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on jsonObject.getString(\"sub\")!", e);
            }
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "convertJSonObjectIntoPostComment - NO \"sub\"!");
        }

        builder.setDistrictList(list);

        return builder.build();
    }
}
