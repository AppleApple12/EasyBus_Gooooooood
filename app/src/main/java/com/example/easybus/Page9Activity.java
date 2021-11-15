package com.example.easybus;
import android.content.Context;
import android.content.Intent;
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
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.PopupWindowCompat;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class Page9Activity extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = "GetLatLngActivity";
    private GoogleMap mMap;
    private TextView txvAdd;
    private  LocationManager locationManager = null;
    private  String provider;
    private  String getmail,la,lo,date;
    private Double latitude,longitude;
    String city = "";
    Button btn;
    Geocoder gc;
    ArrayList<history> historyArrayList;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page9);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//
        //自動更新
        final Timer timer = new Timer();
        timer.schedule(new callfetch(),0,60*1000);

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page9Activity.this,Page4Activity.class);
                timer.cancel();
                startActivity(back);
            }
        });

        historyArrayList = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        txvAdd = (TextView)findViewById(R.id.address);


        Intent intent = getIntent();
        //從PAGE1101 傳過來的 朋友email
        getmail = intent.getStringExtra("email");
        System.out.println("email : "+getmail);



    }


    private void showLocation(Double latitude, Double longitude) {
        //latitude = location.getLatitude();
        //longitude = location.getLongitude();
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


    public void fetch_latlong(){
        String URL =Urls.url1+"/LoginRegister/fetch_latlong.php?email="+getmail;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        System.out.println("yessss");
                        for(int i =0;i<array.length();i++){
                            try{
                                String[] selectday;
                                JSONObject object = array.getJSONObject(i);
                                date = object.getString("date").trim();
                                selectday = date.split(" ");
                                la = object.getString("latitude").trim();
                                lo = object.getString("longitude").trim();
                                latitude=Double.parseDouble(la);
                                longitude=Double.parseDouble(lo);
                                System.out.println("latitude :"+latitude);
                                System.out.println("longitude :"+longitude);


                                history hh = new history(selectday[1],latitude, longitude);
                                historyArrayList.add(hh);

                            } catch (JSONException e) {
                                System.out.println("JSONException e :"+e.toString());
                            }
                        }
                        gc = new Geocoder(Page9Activity.this, Locale.TRADITIONAL_CHINESE);

                        for(history h : historyArrayList) {
                            LatLng latlng = new LatLng(h.getLatitude(), h.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latlng).title("hiiiiii"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,14));
                            showLocation(h.getLatitude(), h.getLongitude());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(Page11Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(Page9Activity.this);
        requestQueue2.add(jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetch_latlong();
    }
    private class callfetch extends TimerTask{
        @Override
        public void run() {
            onMapReady(mMap);
            System.out.println("時間："+new Date());
        }
    }

}