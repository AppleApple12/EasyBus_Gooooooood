package com.example.easybus;
/*搭車*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.net.URLDecoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Page612 extends AppCompatActivity {
    String prevoius,current,BusLine,direction,prevoius2,BusLine2,routename,getmail;
    RequestQueue requestQueue;
    String Url2="";
    Page612ArticleAdapter adaptor;
    ArrayList<Page6article> articles612;
    ArrayList<Page6article> articles612R;
    FetchInfo fetchInfo;
    boolean flagP=false;
    boolean flagR=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page612);
        TextView mTxvId=(TextView)findViewById(R.id.txvId);
        ImageView mBack=findViewById(R.id.view);
        RecyclerView recyclerView=findViewById(R.id.page612);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor=new Page612ArticleAdapter();
        recyclerView.setAdapter(adaptor);
        articles612R=new ArrayList<>();
        articles612=new ArrayList<>();

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //取值routename
        Bundle bundle = getIntent().getExtras();
        routename=bundle.getString("Routename");
        prevoius= bundle.getString("Previous");
        current= bundle.getString("Current");
        mTxvId.setText("搭車至"+current);

        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        requestQueue = Volley.newRequestQueue(this);
        //浮動按鈕撥打給緊急聯絡人
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });

        //返回搭車列表(page611)
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page611=new Intent(Page612.this,Page611.class);
                startActivity(page611);
                finish();
            }
        });
        getData();
    }

    private void getData() {
        try{
            fetchInfo=new FetchInfo();
            fetchInfo.execute();
        }catch (Exception e){
            Log.d("Page612_interfaceError",Log.getStackTraceString(e));
        }
    }

    public class FetchInfo extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;
        String url="https://ptx.transportdata.tw/MOTC/v2/Bus/DisplayStopOfRoute/City/Taichung?$format=JSON";
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page612.this);
            pd.setCancelable(false);
            pd.setMessage("加載中...請稍等!");
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            String Nprevoius=prevoius.replace('[','(');
            String Nprevoius2=Nprevoius.replace(']',')');
            String Ncurrent=current.replace('[','(');
            String Ncurrent2=Ncurrent.replace(']',')');
            Log.d("起點",Nprevoius2);
            Log.d("終點",Ncurrent2);

            try{
                if(response!=null && !response.equals("") ){
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String busline=jsonObject.getJSONObject("RouteName").getString("Zh_tw");

                        Page6article articleB = new Page6article();
                        JSONArray ArrayStops=jsonArray.getJSONObject(i).getJSONArray("Stops");
                        for(int j=0;j<ArrayStops.length();j++){
                            JSONObject ObjStops=ArrayStops.getJSONObject(j);
                            String StopName=ObjStops.getJSONObject("StopName").getString("Zh_tw");

                            if(StopName.equals(Nprevoius2)){
                                flagP=true;
                            }
                            if(flagP && StopName.equals(Ncurrent2)){
                                String direction=jsonObject.getString("Direction");
                                articleB.setLine(busline);
                                articleB.setCome_back(direction);
                                articleB.setPrevious(Nprevoius2);
                                articleB.setCurrent(Ncurrent2);
                                articles612.add(articleB);
                                Log.d("公車資訊",busline+"  "+direction+"  "+Nprevoius2+"  "+Ncurrent2);
                            }else if(flagP && j==ArrayStops.length()-1){
                                flagP=false;
                            }
                            Log.d("Page612", String.valueOf(flagP));
                      }
                    }

                    if(articles612.size()!=0){
                        for(int i=0;i<articles612.size();i++){
                            BusLine=articles612.get(i).line;
                            Url2+="https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/Taichung/"+BusLine+"?$format=JSON ";
                        }
                        callAsynchronousTask();
                    }else{
                        AlertDialog.Builder alertDialog =
                                new AlertDialog.Builder(Page612.this);
                        alertDialog.setTitle("無資料");
                        alertDialog.setMessage("查無公車動態...");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent it612 = new Intent(Page612.this, Page612.class);
                                startActivity(it612);
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
                Log.d("page612_json",Log.getStackTraceString(e));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            response = connect(url);
            return null;
        }
    }

    public void callAsynchronousTask(){
        final Handler hanlder=new Handler();
        Timer timer=new Timer();
        TimerTask doAsynchronousTask = new TimerTask(){
            @Override
            public void run() {
                hanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            new fetchInfo02().execute();
                        }catch(Exception e){
                            Log.d("Page612Async",Log.getStackTraceString(e));
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask,0,60000);
    }

    public class fetchInfo02 extends AsyncTask<String,Void,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // pd = new ProgressDialog(Page612.this);
           // pd.setCancelable(false);
           // pd.setMessage("Downloading...Please wait!");
           // pd.setProgress(0);
           // pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //pd.dismiss();
            adaptor.setData(articles612R);
            adaptor.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... params) {
            articles612R.clear();
            System.out.println(Url2);
            String tokens[]=Url2.split(" ");
            int count=0;
            for(String toke:tokens){
                String response = connect(toke);
                BusLine2=articles612.get(count).line;
                direction=articles612.get(count).come_back;
                prevoius2=articles612.get(count).previous;
                
                try{
                    if (response != null && !response.equals("")) {
                        JSONArray jsonArray2 = new JSONArray(response);

                        for(int j=0;j<jsonArray2.length();j++){
                            JSONObject jsonObject=jsonArray2.getJSONObject(j);
                            String RouteName=jsonObject.getJSONObject("RouteName").getString("Zh_tw");
                            String direction2= jsonObject.getString("Direction");

                            if(direction2.equals(direction) && RouteName.equals(BusLine2)){
                                Page6article articleA = new Page6article();
                                String StopName=jsonObject.getJSONObject("StopName").getString("Zh_tw");
                                String stopStatus=jsonObject.getString("StopStatus");

                                if(StopName.equals(prevoius2) && stopStatus.equals("0")){
                                    try{
                                        String EstimateTime=jsonObject.getString("EstimateTime");
                                        int EstimateTime2=Integer.parseInt(EstimateTime);
                                        String EstimateTime3=String.valueOf(EstimateTime2/60);
                                        articleA.setTime(EstimateTime3);
                                        //Log.d("預計時間",RouteName+"  "+EstimateTime);
                                    }catch (JSONException e){
                                        Log.d("page612_Estimate",Log.getStackTraceString(e));
                                    }
                                    flagR=true;
                                }else if(StopName.equals(prevoius2) && stopStatus.equals("1")){
                                    try{
                                        String NextBusTime=jsonObject.getString("NextBusTime");
                                        int beginIndex=NextBusTime.indexOf("T");
                                        int middle=NextBusTime.indexOf(":");
                                        int endIndex=NextBusTime.indexOf(":",middle+1);
                                        String nextbustime=NextBusTime.substring(beginIndex+1,endIndex);
                                        articleA.setTime(nextbustime);
                                        //Log.d("下一班",RouteName+"  "+nextbustime);
                                    }catch (JSONException e){
                                        articleA.setTime("未發車");
                                        //Log.d("沒車車了",RouteName+"  未發車");
                                    }
                                    flagR=true;
                                }else if(StopName.equals(prevoius2) && stopStatus.equals("3")){
                                    String NoNextBus="末班駛離";
                                    articleA.setTime(NoNextBus);
                                    //Log.d("NOOOOO",RouteName+"  末班駛離");
                                    flagR=true;
                                }
                                if(flagR){
                                    articleA.setLine(RouteName);
                                    articles612R.add(articleA);
                                    flagR=false;
                                }
                            }
                        }
                    }
                }catch(JSONException e) {
                    Log.d("page612_json",Log.getStackTraceString(e));
                }
                count++;
            }
            return null;
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
            //Toast.makeText(Page5012Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page612.this,"請重撥！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Page612.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page612.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}