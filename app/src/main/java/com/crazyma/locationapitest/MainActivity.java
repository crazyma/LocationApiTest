package com.crazyma.locationapitest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = "crazyma";
    private final int REQUEST_ACCESS_FINE_LOCATION_1 = 8;
    private final int REQUEST_ACCESS_FINE_LOCATION_2 = 82;
    private LocationManager mLocationManager;
    private String mProvider;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.text_1);
        textView2 = (TextView) findViewById(R.id.text_2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION_1) {
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
        }else if(requestCode == REQUEST_ACCESS_FINE_LOCATION_2){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  all permissions granted
                getLocationInfo2();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(checkRequiredPermission()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                textView2.setText("Longitude : " + mLastLocation.getLongitude() + ", Latitude : " + mLastLocation.getLatitude());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void buttonClick1(View v) {
        textView1.setText("XD");
        getLocationInfo();
    }

    public void buttonClick2(View v) {
        getLocationInfo2();
    }

    private boolean checkRequiredPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION);

            if (permission != PackageManager.PERMISSION_GRANTED) {  //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION_1
                );
                return false;
            } else {    //已有權限
                return true;
            }
        } else
            return true;
    }

    private boolean checkRequiredPermission2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION);

            if (permission != PackageManager.PERMISSION_GRANTED) {  //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION_2
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

                if (location != null) {
                    textView1.setText("Longitude : " + location.getLongitude() + ", Latitude : " + location.getLatitude());
                }else
                    Toast.makeText(this, "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocationInfo2(){
        textView2.setText("XD");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }
}
