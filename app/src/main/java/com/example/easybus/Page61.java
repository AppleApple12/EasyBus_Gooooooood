package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Page61 extends AppCompatActivity {
    //
    Page61Holder myAdapter;
    static ArrayList<HashMap<String,String>> arrayList1 = new ArrayList<>();
    static ArrayList<HashMap<String,Integer>> arrayList2 = new ArrayList<>();
    RecyclerView recyclerView,mrecyclerView;
    String getmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page61);
        //抓取email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //跳回主頁
        ImageButton btn2 = (ImageButton)findViewById(R.id.back);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page61.this,Page3Activity.class);
                startActivity(it1);
                arrayList1.clear();
                arrayList2.clear();
            }
        });



        int image[] = {R.drawable.busdetails,R.drawable.business,R.drawable.waitingbus};
        final String text[] = {" 公  車  查  詢 ","   上    班   ","   搭    車   "};
        for(int i = image.length-1;i>=0;i--){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("busname",text[i]);
            arrayList1.add(hashMap);
            HashMap<String,Integer> hashMap2 = new HashMap<>();
            hashMap2.put("busphoto",image[i]);
            arrayList2.add(hashMap2);
        }///
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myAdapter = new Page61Holder();
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClick(new Page61Holder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(arrayList1.get(position).get("busname").equals(" 公  車  查  詢 ")){
                    Intent it1 = new Intent(Page61.this,Page6Activity.class);
                    startActivity(it1);
                    arrayList1.clear();
                    arrayList2.clear();
                }else if (arrayList1.get(position).get("busname").equals("   上    班   ")){
                    Intent it1 = new Intent(Page61.this,Page62.class);
                    startActivity(it1);
                    arrayList1.clear();
                    arrayList2.clear();
                }else{
                    Intent it1 = new Intent(Page61.this,take_bus.class);
                    startActivity(it1);
                    arrayList1.clear();
                    arrayList2.clear();
                }

            }
        });
    }

}