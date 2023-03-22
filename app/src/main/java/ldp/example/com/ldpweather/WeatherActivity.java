package ldp.example.com.ldpweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import ldp.example.com.ldpweather.javaBean.DailyForecastBean;
import ldp.example.com.ldpweather.javaBean.HeWeather6Bean;
import ldp.example.com.ldpweather.javaBean.LifestyleBean;
import ldp.example.com.ldpweather.service.AutoUpdateservice;
import ldp.example.com.ldpweather.util.AddressJsontoJava;
import ldp.example.com.ldpweather.util.Httputil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private String cityName;
    private String locationId;
    private NestedScrollView Weatherlayout;
    private TextView Title_city;
    private TextView mTitlie_updatetime;
    private TextView mDegretext;
    private TextView mWeatherInfo_text;
    private LinearLayout mForecast_linearlayout;
    private TextView mAqi_text;
    private TextView mPm_25;
    private LinearLayout mLife_text;
    private ImageView mImageView;
    private ImageView mBaxkgroud_imagine;
    private LinearLayout mHourdailyforecast;
    public DrawerLayout mDrawerLayout;
    private Button mChangecity_btn;
    private Button mSetting_btn;
    private String mcityName;
    private String mCityNames;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 背景图和状态图融合到一起
         */

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        /**
         * 初始化各控件
         */
        mImageView = (ImageView) findViewById(R.id.back_imagine);
        Weatherlayout = (NestedScrollView) findViewById(R.id.weather_addAll_layout);
        Title_city = (TextView) findViewById(R.id.title_city);
        mTitlie_updatetime = (TextView) findViewById(R.id.title_updatetime);
        mDegretext = (TextView) findViewById(R.id.degree_text);
        mWeatherInfo_text = (TextView) findViewById(R.id.weather_info_text);
        mForecast_linearlayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mLife_text = (LinearLayout) findViewById(R.id.life_text);
        mHourdailyforecast = (LinearLayout) findViewById(R.id.dailyforcasthour);
        mAqi_text = (TextView) findViewById(R.id.aqi_text);
        mPm_25 = (TextView) findViewById(R.id.pm25_text);
        mBaxkgroud_imagine = (ImageView) findViewById(R.id.back_imagine);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.changecity_layout);
        mChangecity_btn = (Button) findViewById(R.id.changecity_btn);
        mSetting_btn = (Button) findViewById(R.id.setting_btn);

        mSetting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeatherActivity.this,"正在努力开发中...",Toast.LENGTH_LONG).show();
            }
        });

        /**
         * 滑动菜单监听器
         */
        mChangecity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        /**
         * 下拉刷新...
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.updateWeather);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String imagine_backgroud = preferences.getString("bing_pic", null);
        if (imagine_backgroud != null) {
            Glide.with(this).load(imagine_backgroud).into(mBaxkgroud_imagine);
        } else {
            loadBingyinPic();
        }

      //  String weatherString = preferences.getString("weather", null);

//        if (weatherString != null) {
//            HeWeather6Bean weather = AddressJsontoJava.handleWeatherResponse(weatherString);
//            cityName = weather.getBasic().getLocation();
//            queryCityCode(cityName);
//           // showWeatherInfo(weather);
//        } else {
            cityName = getIntent().getStringExtra("countyName");
            locationId = getIntent().getStringExtra("locationId");
            Weatherlayout.setVisibility(View.INVISIBLE);
            queryCityCode(cityName);
//        }
        preferences.edit().putString("local_city",cityName).apply();

        PreferenceManager.getDefaultSharedPreferences(this);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        /**
                         *刷新当前页面城市天气
                         */
                        System.out.println("刷新时 ： " + mCityNames);
                        queryCityCode(mCityNames);
                    }
                });
    }

    public void queryCityCode(String countyName) {
        if (!TextUtils.isEmpty(locationId)){
            mCityNames = countyName;
            Title_city.setText(mCityNames);
            requestWeather(locationId);
            return;
        }

       // preferences.edit().putString("local_city",countyName).apply();

        QWeather.getGeoCityLookup(this, countyName, Range.CN, 10, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(WeatherActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                if (geoBean == null) {
                    return;
                }
                List<GeoBean.LocationBean> list = geoBean.getLocationBean();
                if (list == null || list.size() == 0) {
                    return;
                }
                final GeoBean.LocationBean bean = list.get(0);
                String locationId = bean.getId();
                requestWeather(locationId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCityNames = bean.getName();
                        Title_city.setText(mCityNames);
                    }
                });
            }
        });
    }

    /**
     * 根据id请求城市天气信息
     *
     * @param countyId address
     */
    public void requestWeather(final String countyId) {

        QWeather.getWeatherNow(WeatherActivity.this, countyId, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onSuccess(final WeatherNowBean weatherNowBean) {
                if (weatherNowBean==null||weatherNowBean.getNow()==null)
                    return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  mTitlie_updatetime.setText(weatherNowBean.getNow().);
                        mDegretext.setText(weatherNowBean.getNow().getTemp()+"℃");
                        mWeatherInfo_text.setText(weatherNowBean.getNow().getText());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        QWeather.getWeather7D(this, countyId, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(final WeatherDailyBean weatherDailyBean) {
                if (weatherDailyBean==null||weatherDailyBean.getDaily()==null||weatherDailyBean.getDaily().size()==0)
                    return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<WeatherDailyBean.DailyBean> dailys = weatherDailyBean.getDaily();
                        mForecast_linearlayout.removeAllViews();
                        for (WeatherDailyBean.DailyBean bean:dailys){
                            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item,
                                    mForecast_linearlayout, false);
                            TextView dateText = (TextView) view.findViewById(R.id.date_text);
                            TextView infoText = (TextView) view.findViewById(R.id.info_text);
                            TextView maxText = (TextView) view.findViewById(R.id.max_text);
                            TextView minText = (TextView) view.findViewById(R.id.min_text);
                            dateText.setText(bean.getFxDate());
                            /**
                             * 判断白天和夜间天气是否相同，如果相同，则只显示一个即可
                             */
                    if ((bean.getTextDay()).equals(bean.getTextNight())) {
                        infoText.setText(bean.getTextDay());
                    } else {
                        infoText.setText(bean.getTextDay() + "转" + bean.getTextNight());
                    }
                    maxText.setText(bean.getTempMax() + "℃");
                    minText.setText(bean.getTempMin() + "℃");
                    mForecast_linearlayout.addView(view);
                        }
                    }
                });

            }
        });

        List<IndicesType> indicesTypes = new ArrayList<>();
        indicesTypes.add(IndicesType.ALL);
        QWeather.getIndices1D(this, countyId, Lang.ZH_HANS, indicesTypes, new QWeather.OnResultIndicesListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(final IndicesBean indicesBean) {
                if (indicesBean==null||indicesBean.getDailyList()==null||indicesBean.getDailyList().size()==0){
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<IndicesBean.DailyBean> dailyList = indicesBean.getDailyList();
                        mLife_text.removeAllViews();
                        for (IndicesBean.DailyBean dailyBean:dailyList){
                            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.lifestylelayout,
                                    mLife_text, false);
                            TextView lifestyle_tltle = (TextView) view.findViewById(R.id.lifestyle_title);
                            TextView lifestyle_text = (TextView) view.findViewById(R.id.lifestyle_text);
                            lifestyle_tltle.setText(dailyBean.getName());
                            lifestyle_text.setText(dailyBean.getText());
                            mLife_text.addView(view);
                        }
                    }
                });
            }
        });


        QWeather.getAirNow(this, countyId, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(final AirNowBean airNowBean) {
                //showWeatherInfo();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAqi_text.setText(airNowBean.getNow().getAqi());
                        mPm_25.setText(airNowBean.getNow().getPm2p5());
                        Weatherlayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        //        String weather_url = "https://free-api.heweather.com/s6/weather?location="
        //                + wetherId + "&7f38cf5c1c614163991cfecccfa65d0d";
//
//        String weather_url = "https://devapi.qweather.com/v7/weather?location=" + countyId + "&key=e213b21d1a25491ba625ce3d3d5dfc2d";
//
//
//        Httputil.sendOkhttpRequest(weather_url, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(WeatherActivity.this, "获取信息失败122", Toast.LENGTH_SHORT).show();
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("获取json数据");
//                final String responseText = response.body().string();
//                final HeWeather6Bean weather = AddressJsontoJava.handleWeatherResponse(responseText);
//                System.out.println(responseText);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (weather != null && "ok".equals(weather.getStatus())) {
//                            Log.d("x", weather.getStatus());
//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.putString("weather", responseText);
//                            editor.apply();
//                          //  showWeatherInfo(weather);
//                        } else {
//                            Toast.makeText(WeatherActivity.this, "获取天气信息失败233", Toast.LENGTH_SHORT).show();
//                        }
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//            }
//        });
        loadBingyinPic();
    }

    /**
     * 处理并且显示 Weather 实体类数据
     *
     * @param weather
     */

    public void showWeatherInfo(HeWeather6Bean weather) {
        mCityNames = weather.getBasic().getLocation();
        String update_time = "";
        String degree = weather.getNow().getTmp() + "℃";
        String weatherInfo = weather.getNow().getCond_txt();

        Title_city.setText(mCityNames);
        mTitlie_updatetime.setText(update_time);
        mDegretext.setText(degree);
        mWeatherInfo_text.setText(weatherInfo);

        /**
         * 清空layout容器中的的views视图
         *
         * 解决 重新加载 之后数据叠加问题
         */
      //  mForecast_linearlayout.removeAllViews();
        mHourdailyforecast.removeAllViews();
      //  mLife_text.removeAllViews();

//        mAqi_text.setText(weather.getNow().getPcpn());
//        mPm_25.setText(weather.getNow().getWind_dir());

//        /**
//         * 显示未来3天天气信息
//         */
//        for (HourForecast hourForecast : weather.allweather.hourForecastList) {
//            View view = LayoutInflater.from(this).inflate(R.layout.dailyforecastlayout,
//                    mHourdailyforecast, false);
//            TextView dailyforecastTime = (TextView) view.findViewById(R.id.dailyforecastTime);
//            TextView dailyforecastWeather = (TextView) view.findViewById(R.id.dailyforecastWeather);
//            TextView dailyforecastTemp = (TextView) view.findViewById(R.id.dailyforecastTemp);
//            dailyforecastTime.setText(hourForecast.getTime());
//            dailyforecastWeather.setText(hourForecast.getWeather());
//            dailyforecastTemp.setText(hourForecast.getTemp() + "°");
//            mHourdailyforecast.addView(view);
//        }
//        /**
//         * 显示3天天气信息
//         */
//        for (DailyForecastBean forecast : weather.getDaily_forecast()) {
//            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
//                    mForecast_linearlayout, false);
//            TextView dateText = (TextView) view.findViewById(R.id.date_text);
//            TextView infoText = (TextView) view.findViewById(R.id.info_text);
//            TextView maxText = (TextView) view.findViewById(R.id.max_text);
//            TextView minText = (TextView) view.findViewById(R.id.min_text);
//            dateText.setText(forecast.getDate());
//            /**
//             * 判断白天和夜间天气是否相同，如果相同，则只显示一个即可
//             */
//            if ((forecast.getCond_code_d()).equals(forecast.getCond_code_n())) {
//                infoText.setText(forecast.getCond_txt_n());
//            } else {
//                infoText.setText(forecast.getCond_txt_d() + "转" + forecast.getCond_txt_n());
//            }
//            maxText.setText(forecast.getTmp_min() + "°");
//            minText.setText(forecast.getTmp_max() + "°");
//            mForecast_linearlayout.addView(view);
//        }
//
//        /**
//         * 显示生活建议
//         */
//        for (LifestyleBean lifestyle : weather.getLifestyle()) {
//            View view = LayoutInflater.from(this).inflate(R.layout.lifestylelayout,
//                    mLife_text, false);
//            TextView lifestyle_tltle = (TextView) view.findViewById(R.id.lifestyle_title);
//            TextView lifestyle_text = (TextView) view.findViewById(R.id.lifestyle_text);
//            lifestyle_tltle.setText(chooseTitle(lifestyle.getType()));
//            lifestyle_text.setText(lifestyle.getTxt());
//            mLife_text.addView(view);
//        }
        Weatherlayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateservice.class);
        startService(intent);
    }


    /**
     * 加载网络图片......
     */
    private void loadBingyinPic() {
        String resquestBingyinPic = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        Httputil.sendOkhttpRequest(resquestBingyinPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(bingPic);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    JSONObject json = jsonArray.getJSONObject(0);
                    final String url = "https://www.bing.com"+json.getString("url");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this)
                                    .load(url)
                                    .into(mBaxkgroud_imagine);
                            preferences.edit().putString("bing_pic", url).apply();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private String chooseTitle(String s){
        String s2 = "";
        switch (s){
            case "comf":
                s2="舒适度指数";
                break;
            case "drsg":
                s2="穿衣指数";
                break;
            case "flu":
                s2="感冒指数";
                break;
            case "sport":
                s2="运动指数";
                break;
            case "trav":
                s2="旅游指数";
                break;
            case "uv":
                s2="紫外线指数";
                break;
            case "cw":
                s2="洗车指数";
                break;
            case "air":
                s2="空气污染扩散条件指数";
                break;
            default:
                break;
        }
        return s2;
    }
}
