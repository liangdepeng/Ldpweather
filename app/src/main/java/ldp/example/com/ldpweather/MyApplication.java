package ldp.example.com.ldpweather;

import android.annotation.SuppressLint;
import android.content.Context;

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
