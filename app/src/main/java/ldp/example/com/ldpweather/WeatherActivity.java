package ldp.example.com.ldpweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;


import ldp.example.com.ldpweather.gsonJavaBean.AllData;
import ldp.example.com.ldpweather.gsonJavaBean.Forecastsevendays;
import ldp.example.com.ldpweather.gsonJavaBean.Suggestions;
import ldp.example.com.ldpweather.util.AddressJsontoJava;
import ldp.example.com.ldpweather.util.Httputil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView Weatherlayout;
    private TextView Title_city;
    private TextView mTitlie_updatetime;
    private TextView mDegretext;
    private TextView mWeatherInfo_text;
    private LinearLayout mForecast_linearlayout;
    private TextView mAqi_text;
    private TextView mPm_25;
    //    private TextView mComfort_text;
    //    private TextView mCar_wash_text;
    //    private TextView mSport_text;
    private LinearLayout mLife_text;
    private ImageView mImageView;
    private ImageView mBaxkgroud_imagine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * 背景图和状态融合到一起
         */

        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        /**
         * 初始化各控件
         */
        mImageView = (ImageView) findViewById(R.id.back_imagine);
        Weatherlayout = (ScrollView) findViewById(R.id.weather_addAll_layout);
        Title_city = (TextView) findViewById(R.id.title_city);
        mTitlie_updatetime = (TextView) findViewById(R.id.title_updatetime);
        mDegretext = (TextView) findViewById(R.id.degree_text);
        mWeatherInfo_text = (TextView) findViewById(R.id.weather_info_text);
        mForecast_linearlayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mLife_text = (LinearLayout) findViewById(R.id.life_text);
        mAqi_text = (TextView) findViewById(R.id.aqi_text);
        mPm_25 = (TextView) findViewById(R.id.pm25_text);
        mBaxkgroud_imagine = (ImageView) findViewById(R.id.back_imagine);
        //        mComfort_text = (TextView) findViewById(R.id.comfort_text);
        //        mCar_wash_text = (TextView) findViewById(R.id.car_wash_text);
        //        mSport_text = (TextView) findViewById(R.id.sport_text);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String imagine_backgroud = preferences.getString("bing_pic", null);
        if (imagine_backgroud != null) {
            Glide.with(this).load(imagine_backgroud).into(mBaxkgroud_imagine);
        } else {
            loadBingyinPic();
        }

        String weatherString = preferences.getString("weather", null);

        if (weatherString != null) {
            AllData weather = AddressJsontoJava.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            String countyName = getIntent().getStringExtra("countyName");
            Weatherlayout.setVisibility(View.INVISIBLE);
            requestWeather(countyName);
        }
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param countyName address
     */
    private void requestWeather(final String countyName) {

        //        String weather_url = "https://free-api.heweather.com/s6/weather?location="
        //                + wetherId + "&7f38cf5c1c614163991cfecccfa65d0d";

        String weather_url = "http://api.jisuapi.com/weather/query?appkey=2416dc7648af35e6&city=" + countyName;

        Httputil.sendOkhttpRequest(weather_url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取信息失败122", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("1111111");
                final String responseText = response.body().string();
                final AllData weather = AddressJsontoJava.handleWeatherResponse(responseText);
                System.out.println(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.msg)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败233", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadBingyinPic();

    }

    /**
     * 处理并且显示 Weather 实体类数据
     *
     * @param weather
     */

    public void showWeatherInfo(AllData weather) {
        String cityName = weather.allweather.getCity();
        String update_time = "";
        String degree = weather.allweather.getTemp() + "℃";
        String weatherInfo = weather.allweather.getWeather();

        Title_city.setText(cityName);
        mTitlie_updatetime.setText(update_time);
        mDegretext.setText(degree);
        mWeatherInfo_text.setText(weatherInfo);


        /**
         * 显示未来几天天气信息
         */

        mForecast_linearlayout.removeAllViews();

        mAqi_text.setText(weather.allweather.aqi.getAqi());
        mPm_25.setText(weather.allweather.aqi.getPm2_524());

        for (Forecastsevendays forecast : weather.allweather.forecastsevendaysList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                    mForecast_linearlayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.getDate());
            infoText.setText(forecast.getDay().getWeather() + "转" + forecast.getNight().getWeather());
            maxText.setText(forecast.getNight().getTemplow());
            minText.setText(forecast.getDay().getTemphigh());
            mForecast_linearlayout.addView(view);
        }


        for (Suggestions lifestyle : weather.allweather.suggestionslist) {
            View view = LayoutInflater.from(this).inflate(R.layout.lifestylelayout,
                    mLife_text, false);
            TextView lifestyle_tltle = (TextView) view.findViewById(R.id.lifestyle_title);
            TextView lifestyle_text = (TextView) view.findViewById(R.id.lifestyle_text);
            lifestyle_tltle.setText(lifestyle.getIname());
            lifestyle_text.setText(lifestyle.getDetail());
            mLife_text.addView(view);
        }

        //        String comfort = "舒适度" + weather.Suggestion.comfort;
        //        String CarWash = "洗车指数" + weather.Suggestion.carwash;
        //        String sport = "运动指数" + weather.Suggestion.sport;
        //        mComfort_text.setText(comfort);
        //        mCar_wash_text.setText(CarWash);
        //        mSport_text.setText(sport);

        Weatherlayout.setVisibility(View.VISIBLE);
    }

    /**
     *   加载网络图片......
     *
     */

    private void loadBingyinPic() {
        String resquestBingyinPic = "http://guolin.tech/api/bing_pic";
        Httputil.sendOkhttpRequest(resquestBingyinPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this)
                                .load(bingPic)
                                .into(mBaxkgroud_imagine);
                    }
                });
            }
        });
    }
}
