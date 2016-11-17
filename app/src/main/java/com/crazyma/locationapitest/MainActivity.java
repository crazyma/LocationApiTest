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

public class MainActivity extends AppCompatActivity{
    private final String TAG = "crazyma";

    private final int REQUEST_ACCESS_FINE_LOCATION_API = 8;
    private final int REQUEST_ACCESS_FINE_LOCATION_PLAY_SERVICE = 82;

    /*  This is for Android Location API    */
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String mProvider;


    /*  This is for Google Play Service */
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

        if(mLocationManager != null &&
                mLocationListener != null &&
                checkRequiredPermission(REQUEST_ACCESS_FINE_LOCATION_API))
            mLocationManager.removeUpdates(mLocationListener);

        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION_API ||
                requestCode == REQUEST_ACCESS_FINE_LOCATION_PLAY_SERVICE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  all permissions granted
                if(requestCode == REQUEST_ACCESS_FINE_LOCATION_API)
                    getLocationInfoByAPI();
                else
                    getLocationInfoByPlayService();
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
        getLocationInfoByAPI();
    }

    public void buttonClick2(View v) {
        getLocationInfoByPlayService();
    }

    private boolean checkRequiredPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION);

            if (permission != PackageManager.PERMISSION_GRANTED) {  //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        requestCode
                );
                return false;
            } else {    //已有權限
                return true;
            }
        } else
            return true;
    }

    private void getLocationInfoByAPI() {

        if (checkRequiredPermission(REQUEST_ACCESS_FINE_LOCATION_API)) {

            if (mLocationManager == null)
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            mProvider = mLocationManager.getBestProvider(criteria, false);

            if (mProvider != null && !mProvider.equals("")) {

                Location location = mLocationManager.getLastKnownLocation(mProvider);
                if (location != null) {
                    textView1.setText("Longitude : " + location.getLongitude() + ", Latitude : " + location.getLatitude());
                }

                mLocationListener = new LocationListener() {
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
                };

                mLocationManager.requestLocationUpdates(mProvider, 5000, 1, mLocationListener);
            }else
                Toast.makeText(this,"There is no available Location Provider",Toast.LENGTH_LONG).show();
        }
    }

    private void getLocationInfoByPlayService(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            if(checkRequiredPermission(REQUEST_ACCESS_FINE_LOCATION_PLAY_SERVICE)) {
                                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);
                                if (mLastLocation != null) {
                                    textView2.setText("Longitude : " + mLastLocation.getLongitude() + ", Latitude : " + mLastLocation.getLatitude());
                                }
                            }else
                                mGoogleApiClient.disconnect();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }

        if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }
}
