package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Page7Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page7);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //浮動按鈕撥打給緊急聯絡人
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"打電話給緊急連絡人",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                Intent call = new Intent(Intent.ACTION_DIAL);
                Uri u = Uri.parse("tel:"+"0905017139");
                call.setData(u);
                startActivity(call);
            }
        });

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page7Activity.this,Page3Activity.class);
                startActivity(back);
            }
        });
        //學習篇123
        ImageButton ib1 = (ImageButton)findViewById(R.id.button);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page701Activity.class);
                startActivity(it1);
            }
        });
        //禮儀篇
        ImageButton ib2 = (ImageButton)findViewById(R.id.button2);
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page702Activity.class);
                startActivity(it1);
            }
        });
        //安全篇
        ImageButton ib3 = (ImageButton)findViewById(R.id.button3);
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page703Activity.class);
                startActivity(it1);
            }
        });
        //求救篇
        ImageButton ib4 = (ImageButton)findViewById(R.id.button4);
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page704Activity.class);
                startActivity(it1);
            }
        });
    }
}
