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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
        txv2 = in1.getStringExtra("txv2");
        txv4 = in1.getStringExtra("txv4");

        final String str1[] = txv2.split(" |\n");
        final String str4[] = txv4.split(" |\n");
        for(int i = 0;i<str1.length;i+=2){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("txv",str1[i]+" "+str1[i+1]);
            arrayList.add(hashMap);
        }
        final int[] flag = {1};
        final Button btn1 = (Button)findViewById(R.id.btn_change);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]==1){
                    btn1.setText("回程");
                    arrayList.clear();
                    for(int i = 0;i<str4.length;i+=2){
                        HashMap<String,String> hashMap1 = new HashMap<>();
                        hashMap1.put("txv",str4[i]+" "+str4[i+1]);
                        arrayList.add(hashMap1);
                    }
                    myListAdapter.notifyDataSetChanged();
                    flag[0] = 0;
                }else{
                    btn1.setText("去程");
                    arrayList.clear();
                    for(int i = 0;i<str1.length;i+=2){
                        HashMap<String,String> hashMap1 = new HashMap<>();
                        hashMap1.put("txv",str1[i]+" "+str1[i+1]);
                        arrayList.add(hashMap1);
                    }
                    myListAdapter.notifyDataSetChanged();
                    flag[0] = 1;
                }

            }
        });
        //取前頁值
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter();
        recyclerView.setAdapter(myListAdapter);
        TextView textView = (TextView)findViewById(R.id.txvtop);
        textView.setText(txv1);

    }

    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

        @NonNull
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview02_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {
            holder.txv.setText(arrayList.get(position).get("txv"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView txv;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txv = itemView.findViewById(R.id.txv);
            }
        }

    }
}