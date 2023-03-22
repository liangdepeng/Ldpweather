package ldp.example.com.ldpweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class MyLocationManager implements LocationListener{

    private LocationManager locationManager;
    private double latitude;
    private double longitude;


    private final static class Ins {
        private final static MyLocationManager manager = new MyLocationManager();
    }

    public static MyLocationManager getInstance() {
        return Ins.manager;
    }

    private MyLocationManager() {
        initLoc();
    }

    private void initLoc() {
        if (ActivityCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = ((LocationManager) MyApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE));
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
           // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, this);
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
           // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0, this);
        } else {
            Toast.makeText(MyApplication.getAppContext(), "获取经纬度失败", Toast.LENGTH_SHORT).show();
        }
        Log.e("locationer",latitude+"---123");
        Log.e("locationer",longitude+"---123");
    }

    @SuppressLint("MissingPermission")
    private void addLocationChangeListener(){


    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e("locationer",Thread.currentThread().toString());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
