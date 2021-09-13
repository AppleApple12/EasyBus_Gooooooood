package com.example.easybus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class BusTest extends AppCompatActivity {
    String APIUrl = "https://ptx.transportdata.tw/MOTC/v2/Bus/RealTimeByFrequency/City/Taichung?$format=JSON";
    private TextView txv;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_test);

        result = "";
        txv = (TextView)findViewById(R.id.txv_result);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest();
            }
        }).start();
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

    private void doRequest(){

        HttpURLConnection connection = null;
        String APPID = "dd1526c197c14838857dfba8c2ea8335";
        String APPKey = "C_iCBjqtaBK275sskxotWRmJ2Fc";
        String xdate = getServerTime();
        String SignDate = "x-date: " + xdate;
        String Signature = "";
        String respond = "";

        BufferedReader reader = null;

        try {
            Signature = Signature(SignDate, APPKey);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        String sAuth = "hmac username=\"" + APPID + "\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"" + Signature + "\"";

        try {
            URL url = new URL(APIUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", sAuth);
            connection.setRequestProperty("x-date", xdate);
            connection.setDoInput(true); //允許輸入流，即允許下載

            respond = connection.getResponseCode() + " " + connection.getResponseMessage();

            result ="no";
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            final StringBuilder response = new StringBuilder();
            String line;

            while ((line=reader.readLine())!=null) {
                response.append(line+"\n");
            }
            reader.close();
            result = response.toString();
            //Log.d("Page62_result",result);
            getData();

        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("Page62_test_1",Log.getStackTraceString(e));
        }catch(Exception e){
            e.printStackTrace();
            Log.e("Page62_test_2",Log.getStackTraceString(e));
        }
        finally {
            if (reader!=null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                    Log.e("Page62_test_2_message",Log.getStackTraceString(e));
                }
            }
        }

        if(connection!=null){
            connection.disconnect();
        }

    }

    private void getData() {
                try{
                    JSONArray array = new JSONArray(result);

                    for(int i=0;i<=array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        String id=jsonObject.getString("RouteID");
                        Log.d("",id);
                        //String travelMode=ObjSteps.getString("RouteID");
                    }
                }catch(JSONException e){
                    //Toast.makeText(BusTest.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

}