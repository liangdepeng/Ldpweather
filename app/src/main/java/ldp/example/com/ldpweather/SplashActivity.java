package ldp.example.com.ldpweather;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.text.DecimalFormat;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private String locationId;
    private SharedPreferences preferences;
    private String locationName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HeConfig.init("HE1807050940521619", "e213b21d1a25491ba625ce3d3d5dfc2d");
        //切换至免费订阅
        HeConfig.switchToDevService();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String imageUrl = preferences.getString("bing_pic", null);
        ImageView splashAdIv = findViewById(R.id.splash_ad_iv);
        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .into(splashAdIv);

        locationId = preferences.getString("location_id", null);
        locationName = preferences.getString("local_city",null);
        if (ActivityCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 202);
            }
        } else {
            getCityName();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 202) {
            boolean isGetSuccess = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PERMISSION_DENIED) {
                    isGetSuccess = false;
                    break;
                }
            }
            if (isGetSuccess) {
                getCityName();
            } else {
                Toast.makeText(this, "获取定位权限失败", Toast.LENGTH_SHORT).show();
                jumpNext();
            }
            //  Toast.makeText(this, "test--"+isGetSuccess, Toast.LENGTH_SHORT).show();
        }
    }


    private void jumpNext() {
        if (locationId != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("countyName", locationName);
            intent.putExtra("locationId", locationId);
            startActivity(intent);
            finish();
        }else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private void getCityName() {
        double latitude = MyLocationManager.getInstance().getLatitude();
        double longitude = MyLocationManager.getInstance().getLongitude();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String latitudeStr = decimalFormat.format(latitude);
        String longitudeStr = decimalFormat.format(longitude);
        QWeather.getGeoCityLookup(this, longitudeStr + "," + latitudeStr, "", Range.CN, 10, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jumpNext();
                    }
                });
            }

            @Override
            public void onSuccess(final GeoBean geoBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<GeoBean.LocationBean> locationBeans = geoBean.getLocationBean();
                        if (locationBeans!=null&&locationBeans.size()>0){
                            GeoBean.LocationBean bean = locationBeans.get(0);
                            locationName = bean.getName();
                            locationId = bean.getId();
                            preferences.edit().putString("location_id",locationId).apply();
                            preferences.edit().putString("local_city", locationName).apply();
                            Toast.makeText(SplashActivity.this, locationName, Toast.LENGTH_SHORT).show();
                        }
                        jumpNext();
                    }
                });
            }
        });
    }
}
