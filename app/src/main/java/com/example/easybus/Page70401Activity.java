package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Page70401Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page70401);
        Dialog corrdialog,crossdialog;

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        ImageView imga = (ImageView)findViewById(R.id.imga);
        
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page70401Activity.this,Page704Activity.class);
                startActivity(back);
            }
        });




    }
}