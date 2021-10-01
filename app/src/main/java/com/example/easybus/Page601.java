package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Page601 extends AppCompatActivity {

    String txv2,txv4,txv1;
    RecyclerView recyclerView;
    MyListAdapter myListAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page601);
        //隱藏action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //回前頁
        ImageButton btn = (ImageButton)findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(Page601.this,Page6Activity.class);
                startActivity(in2);
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
        TextView textView = (TextView)findViewById(R.id.txvtop);
        textView.setText(txv1);

        new fetchDetail().execute();

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
        String result1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page601.this);
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
                if(result1!=null || !result1.equals("")){
                    arrayList.clear();
                    JSONArray jsonArray  = new JSONArray(API1);
                    if(jsonArray.length()>0){
                        Log.d("Page6_array", String.valueOf(jsonArray.length()));
                        JSONObject jsonObject;
                        for(int i = 0;i<jsonArray.length();i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String,String> hashMap = new HashMap<>();
                            if(jsonObject.getString("Direction").equals("0")){
                                JSONArray jsonArray1 = jsonObject.getJSONArray("Stops");
                                if(jsonArray1.length()>0){
                                    for(int j = 0;j<jsonArray1.length();j++){
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("StopName");
                                        String stopName = jsonObject2.getString("Zh_tw");

                                        String stopSequence = jsonObject1.getString("StopSequence");
                                        hashMap.put("txv",stopSequence+" "+stopName);
                                        arrayList.add(hashMap);
                                    }
                                    myListAdapter.setData(arrayList);
                                    myListAdapter.notifyDataSetChanged();
                                    break;
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
            result1 = Page62.connect(API1);
            return null;
        }
    }
}