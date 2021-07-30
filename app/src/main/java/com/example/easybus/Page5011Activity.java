package com.example.easybus;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import tw.edu.pu.s1071481.module.DirectionFinder;
import tw.edu.pu.s1071481.module.DirectionFinderListener;
import tw.edu.pu.s1071481.module.Route;

public class Page5011Activity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener{

    String origin,destination;
    private static final String TAG = "Page5011Activity";
    private GoogleMap mMap;
    private EditText mEtOrigin,mEtDestination;
    private ImageButton mBtnFindPath,mBusInfo;
    private ProgressDialog progressDialog;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5011);

        mBtnFindPath=(ImageButton)findViewById(R.id.btnFindPath);
        mBusInfo=(ImageButton)findViewById(R.id.busInfo);
        mEtOrigin=(EditText)findViewById(R.id.etOrigin);
        mEtDestination=(EditText)findViewById(R.id.etDestination);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        mBtnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        mBusInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest2();
            }
        });
    }

    public void sendRequest(){
        origin = mEtOrigin.getText().toString().trim();
        destination = mEtDestination.getText().toString().trim();
        if(origin.isEmpty()){
            Toast.makeText(this, "請輸入起點", Toast.LENGTH_LONG).show();
            return;
        }else if(destination.isEmpty()){
            Toast.makeText(this, "請輸入目的地", Toast.LENGTH_LONG).show();
            return;
        }
        try{
            new DirectionFinder(this,origin,destination).execute();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void sendRequest2(){
        origin = mEtOrigin.getText().toString().trim();
        destination = mEtDestination.getText().toString().trim();
        if(origin.isEmpty()){
            Toast.makeText(this, "請輸入起點", Toast.LENGTH_LONG).show();
            return;
        }else if(destination.isEmpty()){
            Toast.makeText(this, "請輸入目的地", Toast.LENGTH_LONG).show();
            return;
        }
        try{
            Intent it=new Intent(Page5011Activity.this,Page5012Activity.class);
            Bundle bundle = new Bundle();
            //傳值(origin,destination)
            bundle.putString("origin2",origin);
            bundle.putString("destination2",destination);
            it.putExtras(bundle);
            startActivity(it);
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng school = new LatLng(24.225431, 120.577063);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(school,18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("靜宜大學")
                .position(school)));

        mMap.getUiSettings().setZoomControlsEnabled(true);  //右下角的放大縮小功能
        mMap.getUiSettings().setCompassEnabled(true);   //左上角的指南針，要兩指旋轉
        mMap.getUiSettings().setMapToolbarEnabled(true);    //右下角的導覽及開啟Google Map功能

        Log.d(TAG,"最高放大層級："+mMap.getMaxZoomLevel());
        Log.d(TAG,"最低放大層級："+mMap.getMinZoomLevel());
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);    //右上角的定位功能
    }

    public void onDirectionFinderStart(){
        progressDialog=ProgressDialog.show(this,"請稍等","正在規劃路線...",true);

        if(originMarkers != null){
            for(Marker marker:originMarkers){
                marker.remove();
            }
        }
        if(destinationMarkers != null){
            for(Marker marker:destinationMarkers){
                marker.remove();
            }
        }
        if(polylinePaths != null){
            for(Polyline polyline:polylinePaths){
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths=new ArrayList<>();
        originMarkers=new ArrayList<>();
        destinationMarkers=new ArrayList<>();

        for(Route route:routes){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation,16));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.a))
                    .position(route.startLocation)));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.b))
                    .position(route.endLocation)));

            PolylineOptions polylineOptions=new PolylineOptions().
                    geodesic(true).color(Color.BLUE).width(10);

            for(int i=0;i<route.points.size();i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }

    }
}
