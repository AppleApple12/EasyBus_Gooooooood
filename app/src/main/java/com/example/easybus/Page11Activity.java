package com.example.easybus;
/*需求者歷史足跡 地圖*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class Page11Activity extends AppCompatActivity implements OnMapReadyCallback{
    ArrayList<history> historyArrayList;

    String dayStr, date, la, lo,getmail;
    Double latitude, longitude;
    private GoogleMap googleMap;
    Dialog dialog;
    Button nodatabtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page11);

        historyArrayList = new ArrayList<>();

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        //從PAGE1101 傳過來的 年月日
        dayStr = intent.getStringExtra("dayStr");
        getmail = intent.getStringExtra("email");
        System.out.println("dayStr :"+dayStr);
        System.out.println("email : "+getmail);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.r_map);
        mapFragment.getMapAsync( this);

        dialog = new Dialog(Page11Activity.this);
        dialog.setContentView(R.layout.nodata_dialog);
        //刪除dialog方方的背景
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nodatabtn = dialog.findViewById(R.id.nodatabtn);
        //跳頁回家長主頁
        ImageView back = findViewById(R.id.view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page11Activity.this, Page10Activity.class);
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

                        for (int i = 0; i < array.length(); i++) {
                            try {
                                String[] selectday;
                                JSONObject object = array.getJSONObject(i);
                                date = object.getString("date").trim();
                                selectday = date.split(" ");
                                la = object.getString("latitude").trim();
                                lo = object.getString("longitude").trim();
                                latitude = Double.parseDouble(la);
                                longitude = Double.parseDouble(lo);
                               System.out.println("selectdate[0] :"+selectday[0]);
//                                System.out.println("selectdate[1] :"+selectdate[1]);
                                if(selectday[0].equals(dayStr)) {
                                    System.out.println("TRUE");
                                    history hh = new history(selectday[1],latitude, longitude);
                                    //hh.setDate(selectday[1]);
                                    historyArrayList.add(hh);
                                }else{
                                    continue;
                                }

//                                System.out.println("Get()");
//                                System.out.println("latitude :"+latitude);
//                                System.out.println("longitude :"+longitude);

                            } catch (JSONException e) {
                                System.out.println("JSONException e :" + e.toString());
                            }
                        }
                        System.out.println("historyArrayList :"+historyArrayList.size());
                        if(historyArrayList.size()==0){
                            dialog.show();
                            nodatabtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Page11Activity.this,Page10Activity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        //PolylineOptions polylineOpt = new PolylineOptions();
                        ArrayList<LatLng> points = new ArrayList<>();
                        for(history h : historyArrayList){
                            LatLng latlng = new LatLng(h.getLatitude(), h.getLongitude());
                            googleMap.addMarker(new  MarkerOptions().position(latlng).title(h.getDate())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_brightness_1_24)));
                            points.add(latlng);

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,14));
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
                Toast.makeText(Page11Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(Page11Activity.this);
        requestQueue2.add(jsonArrayRequest);
    }
    @Override public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        fetch_history();

    }
    private BitmapDescriptor bitmapDescriptorFromVector (Context context, int vectorResId){
        Drawable vectorDrawable=ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}