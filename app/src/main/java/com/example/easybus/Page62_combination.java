package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/*
interface AsyncResponse{
    void onDataReceviedSuccess(ArrayList<HashMap<String,String>> hashMapArrayList);
    void onDataReceivedFaild();
}

 */

public class Page62_combination extends AppCompatActivity {
    String txv2,txv4,txv1;
    RecyclerView recyclerView;
    MyAdapter myListAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String,String>> nameArrayList = new ArrayList<>();
    ArrayList<HashMap<String,String>> resultArrayList = new ArrayList<>();
    RequestQueue requestQueue2;
    String getmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page62_combination);

        //隱藏action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //抓取email
        SharedPreferences email = getSharedPreferences("email", MODE_PRIVATE);
        getmail = email.getString("Email", "");

        //回前頁
        ImageButton btn = (ImageButton)findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(Page62_combination.this,Page62.class);
                startActivity(in2);

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

        Intent in1 = getIntent();
        txv1 = in1.getStringExtra("txv1");
        txv2 = in1.getStringExtra("txv2");
        txv4 = in1.getStringExtra("txv4");

        final String str1[] = txv2.split(" |\n");
        final String str4[] = txv4.split(" |\n");
        String destination = "";
        for(int i = 0;i<str1.length;i+=2){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("txv",str1[i]+" "+str1[i+1]);
            arrayList.add(hashMap);
            destination = str1[i+1];
        }

        final int[] flag = {1};
        final Button btn1 = (Button)findViewById(R.id.btn_change);
        btn1.setText("往"+destination);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]==1){
                    String des="";
                    arrayList.clear();
                    for(int i = 0;i<str4.length;i+=2){
                        HashMap<String,String> hashMap1 = new HashMap<>();
                        hashMap1.put("txv",str4[i]+" "+str4[i+1]);
                        arrayList.add(hashMap1);
                        des=str4[i+1];
                    }
                    btn1.setText("往"+des);
                    myListAdapter.notifyDataSetChanged();
                    flag[0] = 0;
                }else{
                    String des="";
                    arrayList.clear();
                    for(int i = 0;i<str1.length;i+=2){
                        HashMap<String,String> hashMap1 = new HashMap<>();
                        hashMap1.put("txv",str1[i]+" "+str1[i+1]);
                        arrayList.add(hashMap1);
                        des=str1[i+1];
                    }
                    btn1.setText("往"+des);
                    myListAdapter.notifyDataSetChanged();
                    flag[0] = 1;
                }

            }
        });
        //取前頁值
        TextView textView = (TextView)findViewById(R.id.txvtop);
        textView.setText(txv1);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myListAdapter = new MyAdapter();
        recyclerView.setAdapter(myListAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        ArrayList<page62_testName> page62TestNames;
        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview02_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
            String str1 = arrayList.get(position).get("txv");
            String str2[] = str1.split(" ");
            holder.txv.setText(str2[1]);

            if(str2[0].equals("已過站")||str2[0].indexOf(":")!=-1){
                holder.txv_2.setTextColor(Color.parseColor("#ffffff"));
                holder.txv_2.setBackgroundResource(R.drawable.bus_pass);
            }else if(str2[0].equals("即將到站")){
                holder.txv_2.setTextColor(Color.parseColor("#ffffff"));
                holder.txv_2.setBackgroundResource(R.drawable.bus_in);
            }else if(str2[0].equals("末班駛離")){
                holder.txv_2.setTextColor(Color.parseColor("#f0f0f0"));
                holder.txv_2.setBackgroundResource(R.drawable.bus_final);
            } else{
                holder.txv_2.setTextColor(Color.parseColor("#000000"));
                holder.txv_2.setBackgroundResource(R.drawable.page601_btn);
            }
            holder.txv_2.setText(str2[0]);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView txv,txv_2;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txv = itemView.findViewById(R.id.txv);
                txv_2 = itemView.findViewById(R.id.txv_line);
            }
        }
        public void setData(ArrayList<page62_testName> page62TestNames){
            this.page62TestNames = page62TestNames;

        }
    }

    public static String Signature(String xData,String AppKey) throws SignatureException {
        String result ;
        try{
            SecretKeySpec secretKeySpec = new SecretKeySpec(AppKey.getBytes("UTF-8"),"HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            byte[] rawHmac = mac.doFinal(xData.getBytes());
            result = Base64.encodeToString(rawHmac, Base64.NO_WRAP);
            result=result.replace("\n", "");
        } catch (Exception e) {
            throw new SignatureException("Fail to generate HMAC"+e);
        }
        return result;
    }

    private static String getServerTime() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private static String getAuthorization(){
        String APPID = "dd1526c197c14838857dfba8c2ea8335";
        String APPKey = "C_iCBjqtaBK275sskxotWRmJ2Fc";
        String xdate = getServerTime();
        String SignDate = "x-date: " + xdate;
        String Signature = "";

        try {
            Signature = Signature(SignDate, APPKey);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        String sAuth = "hmac username=\"" + APPID + "\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"" + Signature + "\"";
        return sAuth;
    }

    public static String connect(String jsonURL) {
        String response = "";
        try {
            URL url = new URL(jsonURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", getAuthorization());
            connection.setRequestProperty("x-date", getServerTime());
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true); //允許輸入流，即允許下載

            InputStream is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuffer jsonData = new StringBuffer();

            while((line=br.readLine())!=null){
                jsonData.append(line+"\n");
            }
            response = jsonData.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
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
            Toast.makeText(Page62_combination.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page62_combination.this,
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
                            Toast.makeText(Page62_combination.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page62_combination.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue2.add(jsonObjectRequest);
    }
}