package com.example.easybus;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Page6Activity extends AppCompatActivity{

    RecyclerView recyclerView;
    Page6ArticleAdapter adapter;
    ArrayList<Page6article> articles;
    //SearchView sv;
    String  url = "https://datacenter.taichung.gov.tw/swagger/OpenData/6af70a9e-4afc-4f54-bf56-01dd84ee8972";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page6);
        //隱藏title bar
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        recyclerView = findViewById(R.id.rvPrograms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Page6ArticleAdapter();
        recyclerView.setAdapter(adapter);

        articles = new ArrayList<>();
        getData();

        ImageButton btnback = (ImageButton)findViewById(R.id.back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1 = new Intent(Page6Activity.this,Page3Activity.class);
                startActivity(it1);
            }
        });

        //sv = (SearchView)findViewById(R.id.searchView);

    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0;i<response.length();i++){
                        JSONObject JO = response.getJSONObject(i);
                        Page6article article = new Page6article();
                        article.setLine(JO.getString("路線"));
                        article.setLine_name(JO.getString("路線名稱"));
                        //article.setStamp_num(JO.getString("站序"));
                        article.setChinese_stamp(JO.getString("中文站點名稱"));
                        //article.setLongitude(JO.getString("經度"));
                        //article.setLatitude(JO.getString("緯度"));
                        article.setCome_back(JO.getString("去回"));
                        //article.setEnglish_stamp(JO.getString("英文站點名稱"));
                        articles.add(article);
                    }
                }catch (JSONException e){
                    Toast.makeText(Page6Activity.this,"JSON is not valid!",Toast.LENGTH_LONG).show();
                }
                adapter.setData(articles);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Page6Activity.this,"Error occurred!",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text){
        ArrayList<Page6article> filteredList = new ArrayList<>();
        for (Page6article item:articles){
            if(item.getLine().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"No Data Found....",Toast.LENGTH_SHORT);
        }else{
            adapter.filterList(filteredList);
        }
    }
}
