package com.example.easybus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Page601 extends AppCompatActivity {

    static String line,linename,stampnum,c_name,e_name,longitude,latitude,comeback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page601);
        Intent in1 = this.getIntent();
        line = in1.getStringExtra("line");
        linename = in1.getStringExtra("linename");
        stampnum = in1.getStringExtra("stampnum");
        c_name = in1.getStringExtra("c_name");
        e_name = in1.getStringExtra("e_name");
        longitude = in1.getStringExtra("longitude");
        latitude = in1.getStringExtra("latitude");
        comeback = in1.getStringExtra("comeback");

        TextView txv1 = (TextView)findViewById(R.id.txv1);
        txv1.setText("路線："+line
                +"\n路線名稱：" +linename
                +"\n站序：" +stampnum
                +"\n中文站牌名稱；"+c_name
                +"\n英文站牌名稱：" +e_name
                +"\n經度：" +longitude
                +"\n緯度：" +latitude
                +"\n來回" +comeback);

    }
}