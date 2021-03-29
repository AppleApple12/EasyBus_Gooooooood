package com.example.easybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Page5Activity extends AppCompatActivity {
    EditText des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent it = new Intent(Page5Activity.this,Page5011Activity.class);
        startActivity(it);
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
        //跳頁回主頁
        ImageButton btn2 = (ImageButton)findViewById(R.id.back);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page5Activity.this,Page3Activity.class);
                startActivity(it1);
            }
        });

    }
}
