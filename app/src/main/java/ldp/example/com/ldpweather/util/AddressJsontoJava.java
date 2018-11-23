package ldp.example.com.ldpweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ldp.example.com.ldpweather.db.City;
import ldp.example.com.ldpweather.db.County;
import ldp.example.com.ldpweather.db.Province;
import ldp.example.com.ldpweather.javaBean.HeWeather6Bean;

/**
 * @author Adminstrator
 * des             ${TODO}
 * @version $Rev$
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class AddressJsontoJava {

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCitres = new JSONArray(response);
                for (int j = 0; j < allCitres.length(); j++) {
                    JSONObject cityObject = allCitres.getJSONObject(j);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCoe(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 解析JSON数据为weather实体类
     */
        public static HeWeather6Bean handleWeatherResponse(String response){
            try {
                HeWeather6Bean allData = new HeWeather6Bean();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
                String weatherContent = jsonArray.getJSONObject(0).toString();
                return new Gson().fromJson(weatherContent,HeWeather6Bean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


//    public static HeWeather6Bean handleWeatherResponse(String response) {
//
//        return new Gson().fromJson(response,HeWeather6Bean.class);
//    }
}
