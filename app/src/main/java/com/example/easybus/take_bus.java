package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class take_bus extends AppCompatActivity {
    static ArrayList<HashMap<String,String>> arrayList1 = new ArrayList<>();
    static ArrayList<HashMap<String,String>> arrayList2 = new ArrayList<>();
    List<take_bus_businfo> takeBusBusinfoList;
    take_bus_Adapter take_bus_adapter;
    RecyclerView mrecyclerView;
    String getmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_bus);

        //抓取email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mrecyclerView=findViewById(R.id.take_bus_list);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        takeBusBusinfoList = new ArrayList<>();

        String image[] = {"busdetails","business"};
        final String text[] = {" 公  車  查  詢 ","   上    班   "};
        for(int i = image.length-1;i>=0;i--){
            take_bus_businfo b =new take_bus_businfo();
            b.setRoutename(text[i]);
            b.setImage(image[i]);
            takeBusBusinfoList.add(b);
        }///
        //getbus();

        String URL =Urls.url1+"/LoginRegister/fetchbusinfo.php?email="+getmail;
        //Request.Method.GET,URL,null我自己加的
        StringRequest stringrequest = new StringRequest(Request.Method.GET,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String reponse) {//////

                            try {
                                JSONArray array =new JSONArray(reponse);
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
                        take_bus_adapter = new take_bus_Adapter(take_bus.this,takeBusBusinfoList);
                        mrecyclerView.setAdapter(take_bus_adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(take_bus.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
         Volley.newRequestQueue(take_bus.this).add(stringrequest);
    }

    private void getbus(){

    }

}