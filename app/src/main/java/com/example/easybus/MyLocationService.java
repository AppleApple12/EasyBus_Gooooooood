package com.example.easybus;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MyLocationService extends BroadcastReceiver {
    String TAG = "MyLocationService";
    String getmail, dateString;
    double latitude, longitude;
    RequestQueue requestQueue;

    private  void date(){
        //目前時間
        Date date = new Date();
        //設定日期格式
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));  //加上J個
        //進行轉換
        dateString = sdf.format(date);
        System.out.println(dateString);
    }

    public static final String ACTION_PROCESS_UPDATE = "com.example.easybus.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        System.out.println("action :"+action);
        if(intent != null){

            //System.out.println("action : "+action);
            if(ACTION_PROCESS_UPDATE.equals(action)){

                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    Location location = result.getLastLocation();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String location_string = new StringBuilder(""+location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();
                    System.out.println("Page3Activity.getInstance().getmail :"+Page3Activity.getInstance().getmail);
                    System.out.println("Page3Activity.getInstance().dateString :"+Page3Activity.getInstance().dateString);
                    try{
                        Page3Activity.getInstance().UpdateUser2(Page3Activity.getInstance().getmail,longitude,latitude);  ;

                    }catch(Exception ex){
                        Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                        System.out.println("ex :"+ex.toString());
                    }
                }
            }
        }
    }

}