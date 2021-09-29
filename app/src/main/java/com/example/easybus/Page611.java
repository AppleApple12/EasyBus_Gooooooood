package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page611 extends AppCompatActivity {
    String Routename,urlOrigin,urlDestination,getmail,url;
    Page611InfoAdaptor adaptor;
    ArrayList<Page611Info> page611infos;
    TextView mOrides;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyCr_-3KbvHxSm9Gb38l7M2E_b8qzwHhcTI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page611);
        mOrides=(TextView)findViewById(R.id.orides);
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

        //返回搭車列表(page61)
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page61=new Intent(Page611.this,Page61.class);
                startActivity(page61);
            }
        });

        //傳值(路線名稱)
        Bundle bundle = getIntent().getExtras();
        Routename= bundle.getString("routename");
        mTxvId.setText(Routename);

        fetchOriDes();
    }

    private void fetchOriDes() {
        String URL =Urls.url1+"/LoginRegister/FetchRouteDetail.php?email="+getmail+"&routename="+Routename;
        //Request.Method.GET,URL,null我自己加的
        StringRequest stringrequest3 = new StringRequest(Request.Method.GET,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String reponse) {
                        try {
                            JSONArray array =new JSONArray(reponse);
                            JSONObject object = array.getJSONObject(0);
                            urlOrigin = object.getString("origin").trim();
                            urlDestination = object.getString("destination").trim();
                            url=DIRECTION_URL_API+"origin="+urlOrigin+"&destination="+urlDestination+"&mode=transit&transit_mode=bus&language=zh-TW&key="+GOOGLE_API_KEY;
                            getData(url,urlOrigin,urlDestination);
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

    private void getData(final String url, String urlOrigin, final String urlDestination) { //抓公車JSON
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("載入中...");
        progressDialog.show();
        mOrides.setText(urlOrigin+" -> "+urlDestination);
        
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
                            //Log.d("終點站",urlDestination);
                        }else if(travelMode.equals("WALKING")){
                            String Htmlinstructions=ObjSteps.getString("html_instructions");
                            b.setHtmlinstructions(Htmlinstructions.substring(3));
                        }else{
                            b.setHtmlinstructions (ObjSteps.getJSONObject("transit_details").getJSONObject("arrival_stop").getString("name"));
                        }
                        page611infos.add(b);
                    }
                }catch (JSONException e){
                    Toast.makeText(Page611.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                adaptor.setData(page611infos);
                adaptor.notifyDataSetChanged();
                progressDialog.dismiss();
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
}