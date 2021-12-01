package com.example.easybus;
/*需求者背景定位*/
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.system.ErrnoException;
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

    double latitude, longitude;
    public static final String ACTION_PROCESS_UPDATE = "com.example.easybus.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        //if(intent != null){
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
                    try{
                        Page3Activity.getInstance().UpdateUser2(Page3Activity.getInstance().getmail,longitude,latitude);
                        Page3Activity.getInstance().updateToast(latitude,longitude);
                       // Page3Activity.getInstance().updateToast(location_string);

                    }catch(Exception ex){
                        Page3Activity.getInstance().UpdateUser2(Page3Activity.getInstance().getmail,longitude,latitude);
                        Page3Activity.getInstance().updateToast(latitude,longitude);
                        //Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(ex.toString());
                    }
                }
            }
        //}
    }

}