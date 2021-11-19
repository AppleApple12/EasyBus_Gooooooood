package com.example.easybus;
/*搭車 列表*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Page61 extends AppCompatActivity {

    Page61Holder myAdapter;
    static ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    static ArrayList<HashMap<String, Integer>> arrayList2 = new ArrayList<>();
    String getmail,routename,routename2;
    List<take_bus_businfo> takeBusBusinfoList;
    take_bus_Adapter take_bus_adapter;
    RecyclerView mrecyclerView;
    RequestQueue requestQueue,requestQueue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page61);
        //抓取email
        SharedPreferences email = getSharedPreferences("email", MODE_PRIVATE);
        getmail = email.getString("Email", "");
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //跳回主頁
        ImageButton btn2 = (ImageButton) findViewById(R.id.back);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page61.this, Page3Activity.class);
                startActivity(it1);
            }
        });
        requestQueue2 = Volley.newRequestQueue(this);
        //浮動按鈕撥打給緊急聯絡人
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mrecyclerView=findViewById(R.id.take_bus_list);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        takeBusBusinfoList = new ArrayList<>();
        String image[] = {"busdetails"};
        final String text[] = {" 公  車  查  詢 "};
        for(int i = image.length-1;i>=0;i--){
            take_bus_businfo b =new take_bus_businfo();
            b.setRoutename(text[i]);
            b.setImage(image[i]);
            takeBusBusinfoList.add(b);
        }

        String URL =Urls.url1+"/LoginRegister/fetchbusinfo.php?email="+getmail;
        //Request.Method.GET,URL,null我自己加的
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            //JSONArray array =new JSONArray(reponse);
                            for(int i =0;i<array.length();i++){
                                JSONObject object = array.getJSONObject(i);

                                String routename = object.getString("routename").trim();
                                String image = object.getString("image").trim();

                                take_bus_businfo b =new take_bus_businfo();
                                b.setRoutename(routename);
                                b.setImage(image);
                                takeBusBusinfoList.add(b);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        take_bus_adapter = new take_bus_Adapter(Page61.this,takeBusBusinfoList);
                        mrecyclerView.setAdapter(take_bus_adapter);
                        take_bus_adapter.setOnItemClick(new take_bus_Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final String name=takeBusBusinfoList.get(position).getRoutename();
                                if(name.equals(" 公  車  查  詢 ")){
                                    Intent it1 = new Intent(Page61.this,Page6Activity.class);
                                    startActivity(it1);
                                }else{
                                    String URL =Urls.url1+"/LoginRegister/SearchRouteN.php?email="+getmail+"&routename="+name;
                                    StringRequest stringrequest2 = new StringRequest(Request.Method.GET,URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String reponse) {
                                                    try {
                                                        JSONArray array =new JSONArray(reponse);
                                                        JSONObject object = array.getJSONObject(0);
                                                        routename2 = object.getString("routename").trim();
                                                        Log.d("路線名稱",routename2);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }finally {
                                                        if(name.equals(routename2)) {
                                                            Intent it2 = new Intent(Page61.this, Page611.class);
                                                            Bundle bundle = new Bundle();
                                                            //傳值(origin,destination)
                                                            bundle.putString("routename", routename2);
                                                            it2.putExtras(bundle);
                                                            startActivity(it2);
                                                        }
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Page61.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Volley.newRequestQueue(Page61.this).add(stringrequest2);
                                }
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Page61.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(Page61.this);
        requestQueue.add(request);

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
            Toast.makeText(Page61.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page61.this,
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
                            Toast.makeText(Page61.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page61.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue2.add(jsonObjectRequest);
    }
}