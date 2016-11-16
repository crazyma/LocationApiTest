package com.crazyma.locationapitest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "crazyma";
    private final int REQUEST_ACCESS_FINE_LOCATION = 8;
    private LocationManager mLocationManager;
    private String mProvider;

    private TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.text_1);
        textView2 = (TextView) findViewById(R.id.text_2);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  all permissions granted
                getLocationInfo();
            } else {
                if (shouldShowRequestPermissionRationale(permissions[0])) {
                    //  show rational dialog
                    Log.i(TAG, "onRequestPermissionsResult: permission denied and need to show rational dialog");

                } else {
                    //  no rational dialog
                    Log.i(TAG, "onRequestPermissionsResult: permission denied and not rational dialog");
                }
            }
        }
    }

    public void buttonClick1(View v) {
        textView1.setText("XD");
        getLocationInfo();
    }

    public void buttonClick2(View v) {
        textView2.setText("XD");
    }

    private boolean checkRequiredPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION);

            if (permission != PackageManager.PERMISSION_GRANTED) {  //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION
                );
                return false;
            } else {    //已有權限
                return true;
            }
        } else
            return true;
    }

    private void getLocationInfo() {

        if (checkRequiredPermission()) {

            if (mLocationManager == null)
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            mProvider = mLocationManager.getBestProvider(criteria, false);

            if (mProvider != null && !mProvider.equals("")) {

                Location location = mLocationManager.getLastKnownLocation(mProvider);
                mLocationManager.requestLocationUpdates(mProvider, 5000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        textView1.setText("Longitude : " + location.getLongitude() + ", Latitude : " + location.getLatitude());
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

//                if (location != null) {
//                    onLocationChanged(location);
//                }else
//                    Toast.makeText(this, "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
