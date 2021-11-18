package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page611 extends AppCompatActivity {
    String Routename,urlOrigin,urlDestination,getmail,url,urlRoutename,previous,current;
    Page611InfoAdaptor adaptor;
    ArrayList<Page611Info> page611infos;
    TextView mOri2,mDes2;
    RequestQueue requestQueue2;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAR3ZSrF2IrlUPdjjAIlXNRaMEJU-wN3CI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page611);
        mOri2=(TextView)findViewById(R.id.ori);
        mDes2=(TextView)findViewById(R.id.des);
        TextView mTxvId=(TextView)findViewById(R.id.txvId);
        ImageButton mBack=(ImageButton)findViewById(R.id.back);
        RecyclerView recyclerView=findViewById(R.id.page611Info);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor=new Page611InfoAdaptor();
        recyclerView.setAdapter(adaptor);
        page611infos=new ArrayList<>();

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //抓取email
        SharedPreferences email = getSharedPreferences("email", MODE_PRIVATE);
        getmail = email.getString("Email", "");

        requestQueue2 = Volley.newRequestQueue(this);
        //浮動按鈕撥打給緊急聯絡人
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });

        //返回搭車列表(page61)
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page61=new Intent(Page611.this,Page61.class);
                startActivity(page61);
            }
        });

        //取值(路線名稱)
        Bundle bundle = getIntent().getExtras();
        Routename= bundle.getString("routename");
        mTxvId.setText(Routename);

        fetchOriDes();
    }

    private void fetchOriDes() {
        String URL =Urls.url1+"/LoginRegister/FetchRouteDetail.php?email="+getmail+"&routename="+Routename;
        StringRequest stringrequest3 = new StringRequest(Request.Method.GET,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String reponse) {
                        try {
                            JSONArray array =new JSONArray(reponse);
                            JSONObject object = array.getJSONObject(0);
                            urlRoutename = object.getString("routename").trim();
                            urlOrigin = object.getString("origin").trim();
                            urlDestination = object.getString("destination").trim();
                            url=DIRECTION_URL_API+"origin="+urlOrigin+"&destination="+urlDestination+"&mode=transit&transit_mode=bus&language=zh-TW&key="+GOOGLE_API_KEY;
                            getData(url,urlOrigin,urlDestination,urlRoutename);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Page611.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(Page611.this).add(stringrequest3);
    }

    private void getData(final String url, String urlOrigin, final String urlDestinatione,final String urlRoutename) { //抓公車JSON
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("載入中...");
        progressDialog.show();
        mOri2.setText(urlOrigin);
        mDes2.setText(urlDestination);

        final JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray ArrayRoutes = response.getJSONArray("routes");
                    JSONArray ArrayLegs =ArrayRoutes.getJSONObject(0).getJSONArray("legs");
                    JSONArray ArraySteps =ArrayLegs.getJSONObject(0).getJSONArray("steps");

                    for(int i=0;i<ArraySteps.length();i++){
                        JSONObject ObjSteps=ArraySteps.getJSONObject(i);
                        String travelMode=ObjSteps.getString("travel_mode");
                        Page611Info b=new Page611Info();

                        if (i==ArraySteps.length()-1) {
                            b.setHtmlinstructions(urlDestination);
                        }else if(travelMode.equals("WALKING")){
                            String Htmlinstructions=ObjSteps.getString("html_instructions");
                            if(Htmlinstructions.equals("永豐棧麗致酒店"))
                                Htmlinstructions="步行到永豐棧酒店";
                            b.setHtmlinstructions(Htmlinstructions.substring(3));
                        }else{
                            String Htmlinstructions=ObjSteps.getJSONObject("transit_details").getJSONObject("arrival_stop").getString("name");
                            if(Htmlinstructions.equals("永豐棧麗致酒店")){
                                Htmlinstructions="永豐棧酒店";
                            }
                            b.setHtmlinstructions(Htmlinstructions);
                        }
                        b.setTravelMode2(travelMode);
                        page611infos.add(b);
                    }
                }catch (JSONException e){
                    Toast.makeText(Page611.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                adaptor.setData(page611infos);
                adaptor.notifyDataSetChanged();
                progressDialog.dismiss();

                //點擊item
                adaptor.setOnItemClick(new Page611InfoAdaptor.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        current=page611infos.get(position).getHtmlinstructions();
                        if(position!=0)
                            previous = page611infos.get(position - 1).getHtmlinstructions();

                        if(page611infos.get(position).getTravelMode2().equals("WALKING")) {
                            Intent it6121 = new Intent(Page611.this, Page6121.class);
                            Bundle bundle = new Bundle();
                            //傳值(routename)
                            if(position!=0) {
                                bundle.putString("routename", urlRoutename);
                                bundle.putString("Previous", previous);
                                bundle.putString("Current", current);
                            }
                            it6121.putExtras(bundle);
                            startActivity(it6121);
                        }else{
                            Intent it612 = new Intent(Page611.this, Page612.class);
                            Bundle bundle = new Bundle();
                            //傳值(routename)
                            if(position!=0) {
                                bundle.putString("routename", urlRoutename);
                                bundle.putString("Previous", previous);
                                bundle.putString("Current", current);
                            }
                            it612.putExtras(bundle);
                            startActivity(it612);
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Page611.this,"發生錯誤1111!",Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest2);
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
            Toast.makeText(Page611.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page611.this,
                    "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
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
                            Toast.makeText(Page611.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page611.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue2.add(jsonObjectRequest);
    }
}