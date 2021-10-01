package com.example.easybus;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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

interface AsyncResponse{
    void onDataReceviedSuccess(ArrayList<HashMap<String,String>> hashMapArrayList);
    void onDataReceivedFaild();
}

public class Page62 extends AppCompatActivity {
    //new
    RecyclerView recyclerView;
    Page6ArticleAdapter adapter;
    ArrayList<Page6article> articles;
    ArrayList<Page6article> articles1;

    String  url = "https://datacenter.taichung.gov.tw/swagger/OpenData/6af70a9e-4afc-4f54-bf56-01dd84ee8972";

    ArrayList<HashMap<String,String>> nameArrayList = new ArrayList<>();
    ArrayList<HashMap<String,String>> resultArrayList = new ArrayList<>();
    FetchInfo fetchInfo;
    String txv1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent it = new Intent(Page62.this,Page61.class);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page62);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("公車查詢");
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rvPrograms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Page6ArticleAdapter();
        recyclerView.setAdapter(adapter);

        articles = new ArrayList<>();
        articles1 = new ArrayList<>();
        getData();
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });*/
        adapter.setOnItemClick(new Page6ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                txv1 = articles1.get(position).line;
                try {
                    fetchInfo= new FetchInfo();
                    fetchInfo.execute();
                    fetchInfo.setOnAsyncResponse(new AsyncResponse() {
                        @Override
                        public void onDataReceviedSuccess(ArrayList<HashMap<String, String>> hashMapArrayList) {
                            resultArrayList = hashMapArrayList;

                            Intent it1 = new Intent(Page62.this,Page62_combination.class);
                            String str1 = txv1;
                            String str2 = "";
                            String str4 = "";
                            for(int i = 0;i<articles.size();i++){
                                if(articles.get(i).line.equals(str1)&&articles.get(i).come_back.equals("去程")){
                                    Boolean flag = false;
                                    for (int j = 0;j<resultArrayList.size();j++){
                                        Set<String> key = resultArrayList.get(j).keySet();
                                        for(String value:key){
                                            String spare[] = value.split(" ");
                                            if(spare[0].equals("0")&&spare[1].indexOf(articles.get(i).chinese_stamp)!=-1){
                                                flag = true;
                                                if(resultArrayList.get(j).get(value).equals("末班駛離")||resultArrayList.get(j).get(value).indexOf(":")!=-1){
                                                    str2 +=resultArrayList.get(j).get(value)+" " + articles.get(i).chinese_stamp + "\n";
                                                }else {
                                                    int num = Integer.parseInt(resultArrayList.get(j).get(value)) / 60;
                                                    if (num <= 1) {
                                                        str2 += "即將到站 " + articles.get(i).chinese_stamp + "\n";
                                                    } else {
                                                        str2 += num + "分 " + articles.get(i).chinese_stamp + "\n";
                                                    }
                                                }
                                                break;
                                            }
                                            Log.d("Page62",j+":  destination: "+spare[0]+",stopname: "+spare[1]+"    /   "+resultArrayList.get(j).get(value));
                                        }
                                        if(flag){
                                            break;
                                        }
                                    }
                                    if(!flag) {
                                        //str2 += articles.get(i).stamp_num + " " + articles.get(i).chinese_stamp + "\n";
                                        str2 +="已過站 " + articles.get(i).chinese_stamp + "\n";
                                    }
                                }
                                if(articles.get(i).line.equals(str1)&&articles.get(i).come_back.equals("回程")){
                                    Boolean flag = false;
                                    for (int j = 0;j<resultArrayList.size();j++){
                                        Set<String> key = resultArrayList.get(j).keySet();
                                        for(String value:key){
                                            String spare[] = value.split(" ");
                                            if(spare[0].equals("1")&&spare[1].indexOf(articles.get(i).chinese_stamp)!=-1){
                                                flag = true;
                                                if(resultArrayList.get(j).get(value).equals("末班駛離")||resultArrayList.get(j).get(value).indexOf(":")!=-1){
                                                    str4+=resultArrayList.get(j).get(value)+" "+articles.get(i).chinese_stamp+"\n";
                                                }else {
                                                    int num = Integer.parseInt(resultArrayList.get(j).get(value)) / 60;
                                                    if (num <= 1) {
                                                        str4 += "即將到站 " + articles.get(i).chinese_stamp + "\n";
                                                    } else {
                                                        str4 += num + "分 " + articles.get(i).chinese_stamp + "\n";
                                                    }
                                                }
                                                break;
                                            }
                                            Log.d("Page62",j+":  destination: "+spare[0]+",stopname: "+spare[1]+"    /   "+resultArrayList.get(j).get(value));
                                        }
                                        if(flag){
                                            break;
                                        }
                                    }
                                    if(!flag) {
                                        //str4+=articles.get(i).stamp_num+" "+articles.get(i).chinese_stamp+"\n";
                                        str4+="已過站 "+articles.get(i).chinese_stamp+"\n";
                                    }

                                }
                            }

                            it1.putExtra("txv1",str1);
                            it1.putExtra("txv2",str2);
                            it1.putExtra("txv4",str4);
                            startActivity(it1);
                        }

                        @Override
                        public void onDataReceivedFaild() {
                            Log.d("Page62_interface","Failed!");
                        }
                    });
                }catch (Exception e){
                    Log.d("Page62_interfaceError",Log.getStackTraceString(e));
                }


            }
        });


    }

    public void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0;i<response.length();i++) {
                        JSONObject JO = response.getJSONObject(i);
                        Page6article article = new Page6article();
                        article.setLine(JO.getString("路線"));
                        article.setLine_name(JO.getString("路線名稱"));
                        article.setStamp_num(JO.getString("站序"));
                        article.setChinese_stamp(JO.getString("中文站點名稱"));
                        article.setLongitude(JO.getString("經度"));
                        article.setLatitude(JO.getString("緯度"));
                        article.setCome_back(JO.getString("去回"));
                        article.setEnglish_stamp(JO.getString("英文站點名稱"));
                        articles.add(article);


                        if(i==0) {
                            articles1.add(article);
                        }else {
                            String string= JO.getString("路線");
                            Boolean flag = true;
                            for(int j = 0;j<articles1.size();j++){
                                if(articles1.get(j).getLine().equals(string)){
                                    flag = false;
                                }
                            }
                            if(flag){
                                articles1.add(article);
                            }
                        }


                    }
                }catch (JSONException e){
                    Toast.makeText(Page62.this,"JSON is not valid!",Toast.LENGTH_LONG).show();
                }
                adapter.setData(articles1);
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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

    public class FetchInfo extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        String url2 = "https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/Taichung/";
        final String APIUrl = url2+txv1+"?$format=JSON";
        String response;
        public AsyncResponse asyncResponse;
        public void setOnAsyncResponse(AsyncResponse asyncResponse){
            this.asyncResponse = asyncResponse;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page62.this);
            pd.setCancelable(false);
            pd.setMessage("Downloading...Please wait!");
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            //Toast.makeText(getApplicationContext(),"Connection successful!",Toast.LENGTH_LONG).show();
            try {
                if(response!=null && !response.equals("")){
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()>0){
                        JSONObject jsonObject;
                        for(int i = 0;i<jsonArray.length();i++) {
                            jsonObject = jsonArray.getJSONObject(i);
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
                                hashMap.put(destination+" "+stopname,estimate);
                                nameArrayList.add(hashMap);
                            }
                            if(jsonObject.getString("StopStatus").equals("1")) {
                                //站名
                                JSONObject jsonObject1 = jsonObject.getJSONObject("StopName");
                                String stopname = jsonObject1.getString("Zh_tw");
                                int destination =jsonObject.getInt("Direction");

                                //車牌,預估時間
                                //testName.setEstimate(jsonObject.getString("EstimateTime"));
                                //testName.setPlateNumb(jsonObject.getString("PlateNumb"));
                                String string = jsonObject.getString("NextBusTime");
                                int beginIndex = string.indexOf("T");
                                int middle = string.indexOf(":");
                                int endIndex = string.indexOf(":",middle+1);
                                String estimate = string.substring(beginIndex+1,endIndex);
                                hashMap.put(destination+" "+stopname,estimate);
                                nameArrayList.add(hashMap);
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
                                nameArrayList.add(hashMap);
                            }

                        }
                        asyncResponse.onDataReceviedSuccess(nameArrayList);
                    }

                }
            }catch (JSONException e) {
                e.printStackTrace();
                Log.d("page62_json",Log.getStackTraceString(e));
                asyncResponse.onDataReceivedFaild();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            response = connect(APIUrl);
            return null;
        }
    }
}