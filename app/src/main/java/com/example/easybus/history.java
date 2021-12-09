package com.example.easybus;
/*歷史足跡*/
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class history extends Application {
    String date;          //日期
    double latitude;      //緯度
    double longitude;     //經度
    public history(){}
    public history( String date,double latitude, double longitude) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    private static final String NAME = "history";

}
