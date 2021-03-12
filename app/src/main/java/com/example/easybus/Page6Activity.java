package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class Page6Activity extends AppCompatActivity {
    String TAG = Page6Activity.class.getSimpleName()+"My";

    ArrayList<HashMap<String ,String>> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page6);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        catchData();


    }

    private void catchData(){
        final String catchdata = "http://www.json-generator.com/api/json/get/ceFZlTiUJe?indent=2";
        final ProgressDialog dialog = ProgressDialog.show(this,"讀取中","請稍後",true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(catchdata);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    InputStream is = connection.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    String line = in.readLine();
                    StringBuffer json = new StringBuffer();
                    while(line!=null){
                        json.append(line);
                        line = in.readLine();
                    }
                    JSONArray jsonArray = new JSONArray(String.valueOf(json));
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String gps_time = jsonObject.getString("GPS_Time");
                        String direction = jsonObject.getString("Direction");
                        String routeid = jsonObject.getString("RouteID");
                        String platenumb = jsonObject.getString("PlateNumb");
                        String y = jsonObject.getString("Y");
                        String x = jsonObject.getString("X");

                        HashMap<String,String > hashMap = new HashMap<>();
                        hashMap.put("GPS_Time",gps_time);
                        hashMap.put("Direction",direction);
                        hashMap.put("RouteID",routeid);
                        hashMap.put("PlateNumb",platenumb);
                        hashMap.put("Y",y);
                        hashMap.put("X",x);

                        arrayList.add(hashMap);
                    }

                    Log.d(TAG,"catch data:"+arrayList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            RecyclerView recyclerView;
                            MyAdapter myAdapter;
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Page6Activity.this));
                            recyclerView.addItemDecoration(new DividerItemDecoration(Page6Activity.this,DividerItemDecoration.VERTICAL));
                            myAdapter = new MyAdapter();
                            recyclerView.setAdapter(myAdapter);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_gps,tv_direction,tv_routeid,tv_platenumb,tv_y,tv_x;
            public ViewHolder(@NonNull View itemView){
                super(itemView);
                tv_gps = findViewById(R.id.gps_time);
                tv_direction = findViewById(R.id.direction);
                tv_routeid = findViewById(R.id.routeid);
                tv_platenumb = findViewById(R.id.platenumb);
                tv_y = findViewById(R.id.y);
                tv_x = findViewById(R.id.x);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder,int position){
            holder.tv_gps.setText(arrayList.get(position).get("GPS_Time"));
            holder.tv_direction.setText("目的地："+arrayList.get(position).get("Direction"));
            holder.tv_routeid.setText("車號："+arrayList.get(position).get("RouteID"));
            holder.tv_platenumb.setText("車牌號："+arrayList.get(position).get("PlateNumb"));
            holder.tv_y.setText("Y："+arrayList.get(position).get("Y"));
            holder.tv_x.setText("X"+arrayList.get(position).get("X"));
        }

        @Override
        public int getItemCount(){
            return arrayList.size();
        }
    }


}
