package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page62 extends AppCompatActivity {
    String  url = "https://datacenter.taichung.gov.tw/swagger/OpenData/5e48458d-6141-4002-a2de-4af48a683623";
    RecyclerView recyclerView;
    Page62Adapter adapter;
    ArrayList<Page62bus> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page62);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //跳回主頁
        ImageButton btn2 = (ImageButton)findViewById(R.id.back);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page62.this,Page61.class);
                startActivity(it1);
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Page62Adapter();
        recyclerView.setAdapter(adapter);
        arrayList = new ArrayList<>();
        getData();
    }

    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0;i<response.length();i++) {
                        JSONObject JO = response.getJSONObject(i);
                        Page62bus arraylist2 = new Page62bus();
                        arraylist2.setRouteID(JO.getString("RouteID"));
                        arraylist2.setDirection(JO.getString("Direction"));
                        arraylist2.setPlateNumb(JO.getString("PlateNumb"));
                        arraylist2.setGPS_Time(JO.getString("GPS_Time"));
                        arraylist2.setX(JO.getString("X"));
                        arraylist2.setY(JO.getString("Y"));
                        arrayList.add(arraylist2);

                    }
                }catch (JSONException e){
                    Toast.makeText(Page62.this,"JSON is not valid!",Toast.LENGTH_LONG).show();
                }
                adapter.setData(arrayList);
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Page62.this,"Error occurred!",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}