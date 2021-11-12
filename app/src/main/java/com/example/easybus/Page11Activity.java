package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 * An activity that displays a Google map with polylines to represent paths or routes,
 * and polygons to represent areas.
 */
public class Page11Activity extends AppCompatActivity implements  OnMapReadyCallback{

    private static final String TAG = "Page11Activity";
    //GoogleMap.OnPolygonClickListener{
    private GoogleMap mMap;
    ArrayList<String> dateArrayList;
    ArrayList<history> historyArrayList= new ArrayList<>();
   // historyAdapter historyAdapter;
    String getmail;
    String dayStr,date,la,lo;
    Double latitude,longitude;
    Double mlatitude,mlongitude;
    RequestQueue requestQueue2;
    ArrayList<HashMap<String,String>> latlong = new ArrayList<>();
    GoogleMap googleMap;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page11);
  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_page11, container, false);
        if (isConnected()) {
            mMapView = (MapView) v.findViewById(R.id.r_map);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        fetch_history();

        Page11Activity page11Activity =new Page11Activity();
        dateArrayList =  new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.r_map);
        bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        arrayList.add(historyArrayList);
        bundle.putParcelableArrayList("history",arrayList);
        mapFragment.setArguments(bundle);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        //從PAGE1101 傳過來的 年月日
        dayStr = intent.getStringExtra("dayStr");
        System.out.println("dayStr : "+dayStr);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //從PAGE1101 傳過來的 朋友email
        getmail = intent.getStringExtra("email");
        System.out.println("email : "+getmail);


        //跳頁回家長主頁
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page11Activity.this,Page4Activity.class);
                startActivity(it1);
            }
        });


    }

    public void fetch_history(){
        String URL =Urls.url1+"/LoginRegister/fetch_perhistory.php?email="+getmail;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        for(int i =0;i<array.length();i++){
                            try{
                                JSONObject object = array.getJSONObject(i);
                                date = object.getString("date").trim();
                                la = object.getString("latitude").trim();
                                lo = object.getString("longitude").trim();
                                latitude=Double.parseDouble(la);
                                longitude=Double.parseDouble(lo);
                                //mlatitude=GetLa(latitude);
                                System.out.println("Get()");
                                System.out.println("date :"+date);
                                System.out.println("latitude :"+latitude);
                                System.out.println("longitude :"+longitude);

                                history h =new history();
                                h.setDate(date);
                                h.setLatitude(latitude);
                                h.setLongitude(longitude);
                                historyArrayList.add(h);


                            } catch (JSONException e) {
                                System.out.println("JSONException e :"+e.toString());
                            }
                        }
                        // historyAdapter =  new historyAdapter(Page11Activity.this,historyArrayList);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(Page11Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(Page11Activity.this);
        requestQueue2.add(jsonArrayRequest);
    }

    /**  for(history mh :historyArrayList){
     System.out.println("getDate :"+mh.getDate());
     System.out.println("getLatitude :"+mh.getLatitude());
     System.out.println("getLongitude :"+mh.getLongitude());
     }
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        // Add polylines to the map.
        // Polylines are useful to show a route or some other connection between points.
//        LatLng schoollatlng = new LatLng(latitude, longitude);
//        googleMap.addMarker(new MarkerOptions().position(schoollatlng).title("MyLocation"));
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(schoollatlng).zoom(10).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        ArrayList list = bundle.getParcelableArrayList("history");
        ArrayList<history> list2 = (ArrayList<history>)list.get(0);
        System.out.println(list2.size());
        for(int i = 0;i<historyArrayList.size();i++){
            System.out.println(historyArrayList.size());
            System.out.println(historyArrayList.get(i).date);
            System.out.println(historyArrayList.get(i).longitude);
            System.out.println(historyArrayList.get(i).latitude);
        }

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        // 把抓到的資料 丟到這邊
                        new LatLng(-35.016, 143.321),//緯度 經度
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.setTag("A");
        // Style the polyline.

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        // Set listeners for click events.

        //googleMap.setOnPolygonClickListener(this);
    }
}