package com.example.easybus;
/*需求者主頁*/
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Page3Activity extends AppCompatActivity {
    TextView test;
    String email, getmail, dateString;//
    RequestQueue requestQueue;

    Geocoder gc;
    LocationManager locationManager;               //宣告定位管理控制

    private String provider;

    double latitude, longitude;
    String TAG = "Page3Activity";
    //int LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    static Page3Activity instance;

    public static Page3Activity getInstance() {
        return instance;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
       
        instance = this;
        gc = new Geocoder(Page3Activity.this, Locale.TRADITIONAL_CHINESE);
        Dexter.withActivity(this)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    updataLocation();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        //Toast.makeText(Page3Activity.this, "You must accept this location .", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();



        requestQueue = Volley.newRequestQueue(this);
        //抓email
        SharedPreferences email = getSharedPreferences("email", MODE_PRIVATE);
        getmail = email.getString("Email", "");
        //dateString = date();
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //跳頁到新增路線
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page3Activity.this, Page5011Activity.class);
                //it1.putExtra("email",getmail);
                startActivity(it1);
            }
        });

        //跳頁到搭車
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it2 = new Intent(Page3Activity.this, Page61.class);
                //it2.putExtra("email",getmail);
                startActivity(it2);
            }
        });

        //跳頁到注意事項
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3 = new Intent(Page3Activity.this, Page7Activity.class);
                //it3.putExtra("email",getmail);
                startActivity(it3);
            }
        });

        //跳頁到我的帳戶
        Button btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it4 = new Intent(Page3Activity.this, Page8Activity.class);
                // it4.putExtra("email",getmail);
                // Toast.makeText(Page3Activity.this, getmail, Toast.LENGTH_SHORT).show();
                startActivity(it4);
            }
        });

        //浮動按鈕撥打給緊急聯絡人
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });
        //取得定位權限
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationRequest = LocationRequest.create();
       // init();
    }
    // 獲取系統預設鈴聲的Uri
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_RINGTONE);
    }
    //更新定位位置
    private void updataLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
       // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         //       && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());

    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(Page3Activity.this,MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();  
        locationRequest.setInterval(1000*10); //設置活動位置更新所需的時間間隔，以毫秒為單位
        locationRequest.setFastestInterval(1000*10); //顯式設置位置更新的最快間隔，以毫秒為單位
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //設置請求的優先級
       // locationRequest.setSmallestDisplacement(10f); //以米為單位設置位置更新之間的最小位移
        locationRequest.setSmallestDisplacement(100); //以米為單位設置位置更新之間的最小位移

    }
    public  void UpdateUser2(final String email,final double longitude,final double latitude){
        Page3Activity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // test.setText(value);
                dateString = date();
                String URL = Urls.url1+"/LoginRegister/save_perhistory.php";
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("Success")){
                                    // String s ="已將 "+phone+" 設為緊急聯絡電話";
                                    // Toast.makeText(Page3Activity.this, s, Toast.LENGTH_SHORT).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               // Toast.makeText(Page3Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parms = new HashMap<>();
                        parms.put("email", email);
                        parms.put("date", dateString);
                        parms.put("longitude", Double.toString(longitude));
                        parms.put("latitude", Double.toString(latitude));
                        return parms;
                    }
                };
                requestQueue.add(stringRequest);


            }
        });
    }

    public void updateToast(final double la,final  double lo){
        Page3Activity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String place[] = {"台灣台中市南屯區大觀路","台灣台中市西屯區至善路"};
                final Double latarr[] = {24.149462843698128,24.176133159383703,24.14560676635126};//一中，逢甲夜市,向學路
                final Double lonarr[] = {120.6849656999734,120.6456141985743,120.64196358990795};
                final Double distance[] = new Double[5];
                String dis;

                String address = GPS2City(gc,la,lo);

                for(int i=0;i<place.length;i++){
                    if(address.indexOf(place[i]) != -1){ //裡面含有
                        distance[i]=90.0;
                    }else{
                        distance[i]=110.0;
                    }
                }
                distance[2] = Distance(lonarr[0],latarr[0],lo,la);//一中
                distance[3] = Distance(lonarr[1],latarr[1],lo,la);//逢甲
                distance[4] = Distance(lonarr[2],latarr[2],lo,la);//向學路


                for(int i=0;i<distance.length;i++){

                    if(distance[i]<100){
//                        Vibrator myVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//                        myVibrator.vibrate(5000);
//                        System.out.println("Vibrator");
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                        System.out.println("Vibrator");
                    }
                    if(distance[i] < 1000 )
                        dis= distance[i] + "m" ;
                    else
                        dis= new DecimalFormat("#.00").format(distance[i]/1000) + "km" ;

                    System.out.println("dis = "+distance[i]);
                    System.out.println("換算過的距離 = "+dis);
                    //Toast.makeText(Page3Activity.this, dis, Toast.LENGTH_SHORT).show();


                }





                //Toast.makeText(Page3Activity.this, address, Toast.LENGTH_SHORT).show();

            }
        });
    }
    //經緯度轉地址
    public static String GPS2City(Geocoder gc, double latitude, double longtitude){
        String returnAddress = "";
        try{
            List<Address> lstAddress = gc.getFromLocation(latitude,longtitude,1);
            returnAddress = lstAddress.get(0).getAddressLine(0);
            Log.d("Page3Address",returnAddress);
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnAddress;
    }
    //計算距離
    public Double Distance(double longitude1, double latitude1, double longitude2,double latitude2)
    {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;
        return distance;
    }

    private  String  date(){
        //目前時間
        Date date = new Date();
        //設定日期格式
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));  //加上J個
        //進行轉換
        dateString = sdf.format(date);

        return dateString;
    }


    @SuppressLint("LongLogTag")
    protected void makeCall(final String phone) {
        //Snackbar.make(v,"打電話給緊急連絡人",Snackbar.LENGTH_LONG).setAction("Action",null).show();
        Intent call = new Intent(Intent.ACTION_DIAL);
        Uri u = Uri.parse("tel:"+phone);
        call.setData(u);

        try {
            startActivity(call);
            finish();
            Log.i("Finished making a call...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(Page3Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page3Activity.this,
                    "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
        //startActivity(call        );
    }
    public void readUser(){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    String emergency_phone;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            emergency_phone = response.getString("emergency_contact");
                            makeCall(emergency_phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(Page3Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(Page3Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}