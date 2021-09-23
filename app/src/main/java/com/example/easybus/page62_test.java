package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.HttpUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.core.Context;

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
import java.net.ProtocolException;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class page62_test extends AppCompatActivity{

    String txv2;
    String txv4;
    ArrayList<page62_testName> nameArrayList;
    static String txv1;
    private TextView txv;
    RecyclerView recyclerView;
    page62_testAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page62_test);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        nameArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.page62_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new page62_testAdapter();
        recyclerView.setAdapter(adapter);

        Intent in1 = getIntent();
        txv1 = in1.getStringExtra("txv1");
        txv2 = in1.getStringExtra("txv2");
        txv4 = in1.getStringExtra("txv4");

        txv = (TextView)findViewById(R.id.txvtop);
        txv.setText(txv1);
        new FetchInfo().execute();

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(page62_test.this,Page62.class);
                startActivity(intent);
            }
        });

    }


    public static String Signature(String xData,String AppKey) throws SignatureException{
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
    public class FetchInfo extends AsyncTask<Void,Void,Void>{
        ProgressDialog pd;
        String url2 = "https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/Taichung/";
        final String APIUrl = url2+txv1+"?$format=JSON";
        String response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(page62_test.this);
            pd.setCancelable(false);
            pd.setMessage("Downloading...Please wait!");
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            Toast.makeText(getApplicationContext(),"Connection successful!",Toast.LENGTH_LONG).show();
            try{
                if(response!=null && !response.equals("")){
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()>0){
                        JSONObject jsonObject;
                        for(int i = 0;i<jsonArray.length();i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("StopStatus").equals("0")) {
                                page62_testName testName = new page62_testName();
                                //站名
                                JSONObject jsonObject1 = jsonObject.getJSONObject("StopName");
                                testName.setStopName(jsonObject1.getString("Zh_tw"));

                                //去程/回程
                                testName.setDirection(jsonObject.getString("Direction"));

                                //車牌,預估時間
                                //testName.setEstimate(jsonObject.getString("EstimateTime"));
                                testName.setPlateNumb(jsonObject.getString("PlateNumb"));
                                testName.setEstimate(jsonObject.getString("EstimateTime"));

                                nameArrayList.add(testName);
                            }
                        }
                        adapter.setData(nameArrayList);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error in fetching data.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("page62_json",Log.getStackTraceString(e));
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            response = connect(APIUrl);
            return null;
        }

    }
}


