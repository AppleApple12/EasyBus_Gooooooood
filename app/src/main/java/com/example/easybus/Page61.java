package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Page61 extends AppCompatActivity {
    Page61Holder myAdapter;
    static ArrayList<HashMap<String,String>> arrayList1 = new ArrayList<>();
    static ArrayList<HashMap<String,Integer>> arrayList2 = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page61);
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
        int image[] = {R.drawable.busdetails};
        //int image[] = {R.drawable.busdetails,R.drawable.business};
        //final String text[] = {" 公  車  查  詢 ","   上    班   "};
        final String text[] = {" 公  車  查  詢 "};
        for(int i = image.length-1;i>=0;i--){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("busname",text[i]);
            arrayList1.add(hashMap);
            HashMap<String,Integer> hashMap2 = new HashMap<>();
            hashMap2.put("busphoto",image[i]);
            arrayList2.add(hashMap2);
        }
        recyclerView = findViewById(R.id.take_bus_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myAdapter = new Page61Holder();
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClick(new Page61Holder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(arrayList1.get(position).get("busname").equals(" 公  車  查  詢 ")){
                    Intent it1 = new Intent(Page61.this,Page62.class);
                    startActivity(it1);
                    arrayList1.clear();
                    arrayList2.clear();
                }else{
                    Intent it1 = new Intent(Page61.this,Page62.class);
                    startActivity(it1);
                    arrayList1.clear();
                    arrayList2.clear();
                }

            }
        });
    }


}