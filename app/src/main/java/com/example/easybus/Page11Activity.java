package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Page11Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "GetLatLngActivity";
    private GoogleMap mMap;
    private TextView txvAdd;
    private LocationManager locationManager = null;
    private  String provider;
    private Double latitude,longitude;
    String city = "";
    Button btn;
    Geocoder gc;
    /** 記錄軌跡 */
    private ArrayList<LatLng> traceOfMe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page11);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //跳頁回家長主頁
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page11Activity.this,Page4Activity.class);
                startActivity(it1);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        if(checkPermissions()){
            init();
            //drawPolyline();
        }
        gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);
    }
/*
   private void drawPolyline() {
        PolylineOptions polylineOpt = new PolylineOptions();
        polylineOpt.add(new LatLng(25.033611, 121.565000));
        polylineOpt.add(new LatLng(25.032728, 121.565137));
        polylineOpt.add(new LatLng(25.033739, 121.527886));
        polylineOpt.add(new LatLng(25.038716, 121.517758));
        polylineOpt.add(new LatLng(25.045656, 121.519636));
        polylineOpt.add(new LatLng(25.046200, 121.517533));

        polylineOpt.color(Color.BLUE);

        Polyline polyline = mMap.addPolyline(polylineOpt);
        polyline.setWidth(10);
    }*/
    private void trackToMe(double lat, double lng){
        if (traceOfMe == null) {
            traceOfMe = new ArrayList<LatLng>();
        }
        traceOfMe.add(new LatLng(lat, lng));

        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : traceOfMe) {
            polylineOpt.add(latlng);
        }

        polylineOpt.color(Color.RED);

        Polyline line = mMap.addPolyline(polylineOpt);
        line.setWidth(10);
    }
    private void init() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }else{
            Toast.makeText(this,"沒有位置提供器可使用",Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this,provider+"可使用",Toast.LENGTH_LONG).show();
        if(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            showLocation(location);
        } else{
            String info = "無法獲得當前位置";
            Toast.makeText(this,info,Toast.LENGTH_LONG).show();
        }
        locationManager.requestLocationUpdates(provider,100,0,locationListener);
    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(Page9Activity.this,"onLocationChanged",Toast.LENGTH_LONG).show();
            /*String where = "";
            if (location != null) {
            //經度
            double lng = location.getLongitude();
            //緯度
            double lat = location.getLatitude();
            //速度
            float speed = location.getSpeed();
            //時間
            long time = location.getTime();
            String timeString = getTimeString(time);

            where = "經度: " + lng +
                    "\n緯度: " + lat +
                    "\n速度: " + speed +
                    "\n時間: " + timeString +
                    "\nProvider: " + provider;
            showLocation(location);
            trackToMe(lat, lng);
            }else {
                where = "No location found.";
            }*/
        }
        private String getTimeString(long timeInMilliseconds){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(timeInMilliseconds);
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
    }; @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int i = 0;i<permissions.length;i++){
                        if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG,permissions[i]+"allow");
                            init();
                        }else{
                            checkPermissions();
                            Log.d(TAG,permissions[i]+"not allow");
                        }
                    }
                }else{
                    Log.d("TAG","no pm allow");
                }
                return;
        }
    }

    private void showLocation(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        city = GPS2City(gc,latitude,longitude);
        trackToMe(latitude,longitude);
        ///txvAdd.setText(city);
    }

    private String GPS2City(Geocoder gc, Double latitude, Double longitude) {
        String city = "";
        try{
            List<Address> lstAddress = gc.getFromLocation(latitude,longitude,1);
            String returnAddess = lstAddress.get(0).getAddressLine(0);
            city = lstAddress.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }
    private boolean checkPermissions() {

        if(Build.VERSION.SDK_INT>=23){
            String permission[] = {ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION};
            List<String> pm_list = new ArrayList<>();
            for(int i = 0;i<permission.length;i++){
                int pm = ActivityCompat.checkSelfPermission(this,permission[i]);
                if(pm!= PackageManager.PERMISSION_GRANTED){
                    pm_list.add(permission[i]);
                }
            }
            if(pm_list.size()>0){
                for(int i = 0;i<pm_list.size();i++){
                    Log.d(TAG, pm_list.get(i));
                }
                Log.d(TAG,pm_list.size()+"");
                ActivityCompat.requestPermissions(this,pm_list.toArray(new String[pm_list.size()]),1);
                return false;
            }
        }
        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng school = new LatLng(24.225431, 120.577063);
        LatLng school = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(school)
                .title("靜宜大學").snippet("Providence University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(school,14));
    }
    private static int makeDropDownMeasureSpec(int measureSpec){
        int mode;
        if(measureSpec== ViewGroup.LayoutParams.WRAP_CONTENT){
            mode = View.MeasureSpec.UNSPECIFIED;
        }else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec),mode);
    }

}