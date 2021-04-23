package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

public class Page5012Activity extends AppCompatActivity{

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyA_-6P0XDrkAv02T3ZhnvzztyFokKFx64M";
    String url,urlOrigin,urlDestination;
    RecyclerView recyclerView;
    BusInfoAdaptor adaptor;
    ArrayList<BusInfo> businfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5012);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //取值(origin,destination)
        Bundle bundle = getIntent().getExtras();
        urlOrigin= bundle.getString("origin2");
        urlDestination= bundle.getString("destination2");

        url=DIRECTION_URL_API+"origin="+urlOrigin+"&destination="+urlDestination+"&mode=transit&transit_mode=bus&key="+GOOGLE_API_KEY;

        recyclerView=findViewById(R.id.busInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor=new BusInfoAdaptor();
        recyclerView.setAdapter(adaptor);
        businfos=new ArrayList<>();
        getData();
    }

    private void getData() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("載入中...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONObject jsonObject=new JSONObject();
                    JSONObject routesObj=jsonObject.getJSONObject("routes");
                    JSONArray legsArray= routesObj.getJSONArray("legs");
                    //JSONObject stepsObj=legsArray.getJSONObject("steps");
                    //JSONObject stepsArray=stepsObj.getJSONArray("steps");
                    for(int i=0;i<legsArray.length();i++){ //改!!!!!!!!!!!!!
                        //JSONObject jsonSteps=stepsArray.getJSONObject(i);
                        //BusInfo busInfo=new BusInfo();
                        //busInfo.setHtmlinstructions(jsonObject.getString("htmlinstructions"));
                        //busInfo.setName(jsonObject.getString("name"));
                        //businfos.add(busInfo);
                    }
                }catch (JSONException e){
                    Toast.makeText(Page5012Activity.this,"發生錯誤",Toast.LENGTH_LONG).show();
                }
                adaptor.setData(businfos);
                adaptor.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Page5012Activity.this,"發生錯誤1111!",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}