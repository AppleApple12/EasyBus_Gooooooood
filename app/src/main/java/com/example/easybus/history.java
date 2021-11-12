package com.example.easybus;

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
    public history( double latitude, double longitude) {
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


    /*public void fetch_history(){
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

                                losArrayList.add(longitude);
                                history h =new history(date,latitude,longitude);
                                //history h =new history();
                                h.setDate(date);
                                h.setLatitude(latitude);
                                h.setLongitude(longitude);
                                historyArrayList.add(h);

//                                System.out.println("Get()");
//                                System.out.println("date :"+date);
//                                System.out.println("latitude :"+latitude);
//                                System.out.println("longitude :"+longitude);

                            } catch (JSONException e) {
                                System.out.println("JSONException e :"+e.toString());
                            }
                        }
                        //test();

                        // page11Adapter = new Page11Adapter(Page11Activity.this,historyArrayList);
                        page11Adapter.setData(historyArrayList);
                        page11Adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Page11Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(Page11Activity.this);
        requestQueue2.add(jsonArrayRequest);
    }*/


}
