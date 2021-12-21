package com.example.easybus;
/*搭車*/
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Page601 extends AppCompatActivity {
    String txv2,txv4,txv1,destination,getmail;
    RecyclerView recyclerView;
    MyListAdapter myListAdapter;
    ArrayList<HashMap<String,String>> arrayList ;
    ArrayList<HashMap<String,String>> arr_estimate;
    Button btn1;
    Boolean flag;
    RequestQueue requestQueue;
    Timer timer;
    TimerTask doAsynchronousTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page601);
        //隱藏action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //回前頁
        ImageView btn = findViewById(R.id.view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent in1 = getIntent();
        txv1 = in1.getStringExtra("txv1");

        //取前頁值
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter();
        recyclerView.setAdapter(myListAdapter);
        TextView textView = (TextView)findViewById(R.id.txvtoptext);
        textView.setText(txv1);
        btn1 = (Button)findViewById(R.id.btn_change);

        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        requestQueue = Volley.newRequestQueue(this);
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });

        //new fetchDetail().execute();
        callAsynchronousTask();
        flag = false;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                if(flag){
                    flag = false;
                }else{
                    flag = true;
                }
                callAsynchronousTask();
            }
        });


    }
    public void callAsynchronousTask(){
        final Handler handler = new Handler();
        timer = new Timer();
        doAsynchronousTask = new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    try {
                        fetchDetail fetch = new fetchDetail();
                        fetch.execute();
                    }catch (Exception e){
                        Log.d("Page601Async",Log.getStackTraceString(e));
                    }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask,0,60000);
    }

    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        ArrayList<HashMap<String,String>> hashMapArrayList;

        public MyListAdapter(){
            hashMapArrayList = new ArrayList<>();
        }
        @NonNull
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview02_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {
            String str1 = hashMapArrayList.get(position).get("txv");
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
                holder.txv_2.setBackgroundResource(R.drawable.bus_wait);
            }
            holder.txv_2.setText(str2[0]);
        }

        @Override
        public int getItemCount() {
            return hashMapArrayList.size();
        }

        public void setData(ArrayList<HashMap<String,String>> hashMapArrayList){
            this.hashMapArrayList = hashMapArrayList;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView txv,txv_2;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txv = itemView.findViewById(R.id.txv);
                txv_2 = itemView.findViewById(R.id.txv_line);
            }
        }

    }

    public class fetchDetail extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;
        String API1 = "https://ptx.transportdata.tw/MOTC/v2/Bus/DisplayStopOfRoute/City/Taichung/"+txv1+"?$format=JSON";
        String API2 = "https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/Taichung/"+txv1+"?$format=JSON";
        String result1,result2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page601.this);
            pd.setCancelable(false);
            pd.setMessage("加載中...請稍等!");
            pd.setProgress(0);
            if(!isCancelled() && Page601.this!=null && !Page601.this.isFinishing()){
                pd.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Toast.makeText(getApplicationContext(),"Connection successful!",Toast.LENGTH_LONG).show();
            arrayList = new ArrayList<>();
            arr_estimate = new ArrayList<>();
            try{
                if(result2!=null||!result2.equals("")){
                    pd.dismiss();
                    JSONArray jsonArray = new JSONArray(result2);
                    if(jsonArray.length()>0){
                        JSONObject jsonObject;
                        for(int i = 0;i<jsonArray.length();i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getJSONObject("RouteName").getString("Zh_tw").equals(txv1)){
                                HashMap<String,String> hashMap = new HashMap<>();
                                if(jsonObject.getString("StopStatus").equals("0")) {
                                    //站名
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("StopName");
                                    String stopname = jsonObject1.getString("Zh_tw");
                                    int destination =jsonObject.getInt("Direction");

                                    //車牌,預估時間
                                    //testName.setEstimate(jsonObject.getString("EstimateTime"));
                                    //testName.setPlateNumb(jsonObject.getString("PlateNumb"));
                                    String estimate = jsonObject.getString("EstimateTime");
                                    int num = Integer.parseInt(estimate)/60;
                                    if(num<=1){
                                        hashMap.put(destination + " " + stopname, "即將到站");
                                    }else {
                                        hashMap.put(destination + " " + stopname, num + "分");
                                    }
                                    arr_estimate.add(hashMap);
                                }
                                if(jsonObject.getString("StopStatus").equals("1")) {
                                    //站名
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("StopName");
                                    String stopname = jsonObject1.getString("Zh_tw");
                                    int destination =jsonObject.getInt("Direction");

                                    //車牌,預估時間
                                    //testName.setEstimate(jsonObject.getString("EstimateTime"));
                                    //testName.setPlateNumb(jsonObject.getString("PlateNumb"));
                                    try {
                                        String string = jsonObject.getString("NextBusTime");
                                        int beginIndex = string.indexOf("T");
                                        int middle = string.indexOf(":");
                                        int endIndex = string.indexOf(":", middle + 1);
                                        String estimate = string.substring(beginIndex + 1, endIndex);
                                        hashMap.put(destination + " " + stopname, estimate);
                                    }catch (Exception e){
                                        Log.d("Page601NextBus",i+stopname);
                                        hashMap.put(destination + " " + stopname, "未發車");
                                    }

                                    arr_estimate.add(hashMap);
                                }
                                if(jsonObject.getString("StopStatus").equals("3")) {
                                    //站名
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("StopName");
                                    String stopname = jsonObject1.getString("Zh_tw");
                                    int destination =jsonObject.getInt("Direction");

                                    //車牌,預估時間
                                    //testName.setEstimate(jsonObject.getString("EstimateTime"));
                                    //testName.setPlateNumb(jsonObject.getString("PlateNumb"));
                                    String estimate = "末班駛離";
                                    hashMap.put(destination+" "+stopname,estimate);
                                    arr_estimate.add(hashMap);
                                }
                            }

                        }
                    }
                }
                if(result1!=null || !result1.equals("")){
                    arrayList.clear();
                    JSONArray jsonArray  = new JSONArray(result1);
                    if(jsonArray.length()>0){
                        JSONObject jsonObject;
                        for(int i = 0;i<jsonArray.length();i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getJSONObject("RouteName").getString("Zh_tw").equals(txv1)){
                                if(jsonObject.getString("Direction").equals("0")&&flag==false){
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("Stops");
                                    if(jsonArray1.length()>0){
                                        for(int j = 0;j<jsonArray1.length();j++){
                                            HashMap<String,String> hashMap = new HashMap<>();
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("StopName");
                                            String stopName = jsonObject2.getString("Zh_tw");

                                            String stopSequence = jsonObject1.getString("StopSequence");
                                            if(arr_estimate.size()>0) {
                                                for (int k = 0; k < arr_estimate.size(); k++) {
                                                    Set<String> key = arr_estimate.get(k).keySet();
                                                    for(String value:key){
                                                        String spare[] = value.split(" ");
                                                        if(spare[0].equals("0") && spare[1].equals(stopName)){
                                                            if(stopSequence.indexOf("分")==-1 && !stopSequence.equals("即將到站") && stopSequence.indexOf(":")==-1 && !stopSequence.equals("末班駛離") && !stopSequence.equals("未發車")){
                                                                stopSequence = arr_estimate.get(k).get(value);
                                                            }else if(stopSequence.equals("末班駛離")){
                                                                if(!arr_estimate.get(k).get(value).equals("末班駛離")){
                                                                    stopSequence = arr_estimate.get(k).get(value);
                                                                }
                                                            }
                                                            else if(stopSequence.indexOf(":")!=-1){
                                                                if(arr_estimate.get(k).get(value).equals("即將到站") || arr_estimate.get(k).get(value).indexOf("分")!=-1){
                                                                    stopSequence = arr_estimate.get(k).get(value);
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                            hashMap.put("txv",stopSequence+" "+stopName);
                                            arrayList.add(hashMap);
                                            destination = stopName;
                                        }
                                        btn1.setText("往"+destination);
                                        myListAdapter.setData(arrayList);
                                        myListAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }else if(jsonObject.getString("Direction").equals("1")&&flag==true){
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("Stops");

                                    if(jsonArray1.length()>0){
                                        for(int j = 0;j<jsonArray1.length();j++){
                                            HashMap<String,String> hashMap = new HashMap<>();
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("StopName");
                                            String stopName = jsonObject2.getString("Zh_tw");

                                            String stopSequence = jsonObject1.getString("StopSequence");
                                            if(arr_estimate.size()>0) {
                                                for (int k = 0; k < arr_estimate.size(); k++) {
                                                    Set<String> key = arr_estimate.get(k).keySet();
                                                    for(String value:key){
                                                        String spare[] = value.split(" ");
                                                        if(spare[0].equals("1") && spare[1].equals(stopName)){
                                                            if(stopSequence.indexOf("分")==-1 && !stopSequence.equals("即將到站") && stopSequence.indexOf(":")==-1 && !stopSequence.equals("末班駛離") && !stopSequence.equals("更新中")){
                                                                stopSequence = arr_estimate.get(k).get(value);
                                                            }else if(stopSequence.equals("末班駛離")){
                                                                if(!arr_estimate.get(k).get(value).equals("末班駛離")){
                                                                    stopSequence = arr_estimate.get(k).get(value);
                                                                }
                                                            }
                                                            else if(stopSequence.indexOf(":")!=-1){
                                                                if(arr_estimate.get(k).get(value).equals("即將到站") || arr_estimate.get(k).get(value).indexOf("分")!=-1){
                                                                    stopSequence = arr_estimate.get(k).get(value);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            hashMap.put("txv",stopSequence+" "+stopName);
                                            arrayList.add(hashMap);
                                            destination = stopName;
                                        }
                                        btn1.setText("往"+destination);
                                        myListAdapter.setData(arrayList);
                                        myListAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }

                        }


                    }
                }
            }catch (JSONException e){
                Log.d("Page6_error",Log.getStackTraceString(e));
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            result1 = connect(API1);
            result2 = connect(API2);

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
            Toast.makeText(Page601.this,"請重撥！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Page601.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page601.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
