package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * An activity that displays a Google map with polylines to represent paths or routes,
 * and polygons to represent areas.
 */
public class Page11Activity extends AppCompatActivity implements OnMapReadyCallback{

    ArrayList<history> historyArrayList;

    String getmail = "asdf@gmail.com";
    String dayStr, date, la, lo;
    Double latitude, longitude;


    private GoogleMap googleMap;
    Polyline polyline1;
    JSONArray contacts;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page11);


        historyArrayList = new ArrayList<>();

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.r_map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        //跳頁回家長主頁
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page11Activity.this, Page1101Activity.class);
                startActivity(it1);
            }
        });

    }




    public void fetch_history(){
        String URL = Urls.url1 + "/LoginRegister/fetch_perhistory.php?email=" + getmail;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        contacts = array;
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                //contacts = jonObject.getJSONObject("XML_Head").getJSONObject("Infos").getJSONArray("Info");
                                JSONObject object = array.getJSONObject(i);
                                date = object.getString("date").trim();
                                la = object.getString("latitude").trim();
                                lo = object.getString("longitude").trim();
                                latitude = Double.parseDouble(la);
                                longitude = Double.parseDouble(lo);


                                history hh = new history(latitude, longitude);

                                historyArrayList.add(hh);

//                                System.out.println("Get()");
//                                System.out.println("date :"+date);
//                                System.out.println("latitude :"+latitude);
//                                System.out.println("longitude :"+longitude);

                            } catch (JSONException e) {
                                System.out.println("JSONException e :" + e.toString());
                            }
                        }
                        System.out.println("historyArrayList :"+historyArrayList.size());
                        //PolylineOptions polylineOpt = new PolylineOptions();
                        ArrayList<LatLng> points = new ArrayList<>();
                        for(history h : historyArrayList){
                            LatLng latlng = new LatLng(h.getLatitude(), h.getLongitude());
                            googleMap.addMarker(new  MarkerOptions().position(latlng).title("hiiiiii"));
                            points.add(latlng);
                        }
                        PolylineOptions polylineOptions = new PolylineOptions().width(3);
                        for(int i = 0;i<points.size();i++){
                            polylineOptions.add(points.get(i));
                        }
                        googleMap.addPolyline(polylineOptions);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Page11Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(Page11Activity.this);
        requestQueue2.add(jsonArrayRequest);
    }
    @Override public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        LatLng schoollatlng = new LatLng(24.225431, 120.577063);
        mMap.addMarker(new MarkerOptions().
                position(schoollatlng).title("MyLocation"));
        fetch_history();

    }
}