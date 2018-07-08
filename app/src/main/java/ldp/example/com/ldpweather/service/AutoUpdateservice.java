package ldp.example.com.ldpweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.io.IOException;

import ldp.example.com.ldpweather.gsonJavaBean.AllData;
import ldp.example.com.ldpweather.util.AddressJsontoJava;
import ldp.example.com.ldpweather.util.Httputil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateservice extends Service {


    @Override
    public IBinder onBind(Intent intent)  {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        updateweather();
        updatePic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 3*60*60*1000;
        long time = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateservice.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pendingIntent);
        return super.onStartCommand(intent,flags,startId);
    }

    private void updateweather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString!=null){
            AllData weather = AddressJsontoJava.handleWeatherResponse(weatherString);
            String countyName = weather.allweather.getCity();

            String weather_url = "http://api.jisuapi.com/weather/query?appkey=2416dc7648af35e6&city=" + countyName;
            Httputil.sendOkhttpRequest(weather_url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    AllData weather = AddressJsontoJava.handleWeatherResponse(responseText);
                    if (weather!=null&&"ok".equals(weather.getStatus())){
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(AutoUpdateservice.this)
                                .edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updatePic(){
        String Pic_url = "http://guolin.tech/api/bing_pic";
        Httputil.sendOkhttpRequest(Pic_url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(AutoUpdateservice.this)
                        .edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();

            }
        });
    }
}
