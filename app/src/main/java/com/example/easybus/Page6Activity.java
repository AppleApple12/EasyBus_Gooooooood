package com.example.easybus;
/*要*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Page6Activity extends AppCompatActivity{

    RecyclerView recyclerView;
    Page6ArticleAdapter adapter;
    ArrayList<Page6article> articles = new ArrayList<>(); ;
    ArrayList<Page6article> articles1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent it = new Intent(Page6Activity.this,Page61.class);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page6);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("公車查詢");
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rvPrograms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Page6ArticleAdapter();
        recyclerView.setAdapter(adapter);

        //articles = new ArrayList<>();
        articles1 = new ArrayList<>();
        new fetchData().execute();

        adapter.setOnItemClick(new Page6ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it1 = new Intent(Page6Activity.this,Page601.class);
                TextView str1 = (TextView) view.findViewById(R.id.txvline);
                String string = str1.getText().toString();
                //String str1 = articles1.get(position).line;
                it1.putExtra("txv1",string);
                startActivity(it1);
            }
        });


    }

    public class fetchData extends AsyncTask<Void,Void,Void>{
        ProgressDialog pd;
        String result;
        String API = "https://ptx.transportdata.tw/MOTC/v2/Bus/Route/City/Taichung?$format=JSON";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page6Activity.this);
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
            try{
                if(result!=null && !result.equals("")){
                    JSONArray jsonArray = new JSONArray(result);
                    if(jsonArray.length()>0){
                        JSONObject jsonObject;
                        for(int i = 0;i<jsonArray.length();i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            Page6article article = new Page6article();

                            JSONObject jsonObject1 = jsonObject.getJSONObject("RouteName");
                            String routename = jsonObject1.getString("Zh_tw");
                            article.setLine(routename);

                            String departureStopName = jsonObject.getString("DepartureStopNameZh");
                            String destinationName = jsonObject.getString("DestinationStopNameZh");
                            article.setLine_name(departureStopName+" - "+destinationName);
                            articles.add(article);
                        }
                        adapter.setData(articles);
                        adapter.notifyDataSetChanged();
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
                Log.d("page62_json",Log.getStackTraceString(e));
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            result = connect(API);
            return null;
        }
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


}
