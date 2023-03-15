package ldp.example.com.ldpweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.qweather.sdk.view.HeConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HeConfig.init("HE1807050940521619","e213b21d1a25491ba625ce3d3d5dfc2d");
        //切换至免费订阅
        HeConfig.switchToDevService();
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String city = preferences.getString("local_city", null);
        if (city!=null){
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("countyName",city);
            startActivity(intent);
            finish();
        }
    }
}
