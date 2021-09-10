package com.example.easybus;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.PopupWindowCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Page9Activity extends AppCompatActivity implements OnMapReadyCallback {

    private final static String TAG = "GetLatLngActivity";

    private GoogleMap mMap;
    private TextView txvAdd;
    private  LocationManager locationManager = null;
    private  String provider;
    private Double latitude,longitude;
    String city = "";
    Button btn;
    Geocoder gc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page9);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        txvAdd = (TextView)findViewById(R.id.address);

        if(checkPermissions()){
            init();
        }
        btn = (Button)findViewById(R.id.btn_intent);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initPopWindow(v);
                TestPopupWindow mWindow = new TestPopupWindow(Page9Activity.this);

                //測量contentView大小
                TestPopupWindow window = new TestPopupWindow(Page9Activity.this);
                View content = window.getContentView();
                content.measure(makeDropDownMeasureSpec(window.getWidth()),makeDropDownMeasureSpec(window.getHeight()));
                //Math.abs 回傳絕對值
                int offset_x= mWindow.getContentView().getMeasuredWidth()-btn.getWidth()/5*3;
                int offset_y = -(window.getContentView().getMeasuredHeight()+btn.getHeight()+30);
                PopupWindowCompat.showAsDropDown(mWindow,btn,offset_x,offset_y,Gravity.NO_GRAVITY);
            }
        });

        gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);
    }

    public class TestPopupWindow extends PopupWindow{
        public  TestPopupWindow(Context context){
            super(context);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setOutsideTouchable(true);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View contentView = LayoutInflater.from(context).inflate(R.layout.page9_popup,null,false);
            setContentView(contentView);
        }
    }
/*
    private void initPopWindow(View v) {
        View view = LayoutInflater.from(Page9Activity.this).inflate(R.layout.page9_popup,null,false);
        TextView txv = (TextView)view.findViewById(R.id.txv_line);
        Button btn2 = (Button) view.findViewById(R.id.btn_back);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.anim.page9_animation);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
        popupWindow.showAsDropDown(v,0,0);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

 */

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
            showLocation(location);
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
    };

    @Override
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
        txvAdd.setText(city);
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
                if(pm!=PackageManager.PERMISSION_GRANTED){
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
        if(measureSpec==ViewGroup.LayoutParams.WRAP_CONTENT){
            mode = View.MeasureSpec.UNSPECIFIED;
        }else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec),mode);
    }

}
