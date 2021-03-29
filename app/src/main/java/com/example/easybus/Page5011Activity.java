package com.example.easybus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Page5011Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Page5011Activity";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5011);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng school = new LatLng(24.225431, 120.577063);

        mMap.addMarker(new MarkerOptions()
                .title("靜宜大學")
                .position(school));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setZoomControlsEnabled(true);  //右下角的放大縮小功能
        //mMap.getUiSettings().setCompassEnabled(true);   //左上角的指南針，要兩指旋轉
        //mMap.getUiSettings().setMapToolbarEnabled(true);    //右下角的導覽及開啟Google Map功能

        //Log.d(TAG,"最高放大層級："+mMap.getMaxZoomLevel());
        //Log.d(TAG,"最低放大層級："+mMap.getMinZoomLevel());
        //if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
          //  return;
        //}

        //mMap.setMyLocationEnabled(true);    //右上角的定位功能
    }
}
