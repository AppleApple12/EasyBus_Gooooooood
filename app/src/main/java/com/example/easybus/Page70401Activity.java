package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Page70401Activity extends AppCompatActivity {
    Dialog corrdialog,crossdialog;
    ImageView imga,imgb,imgc,nextbtn,backbtn;
    TextView correct_content,nexttxt,cross_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page70401);


        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        imga = (ImageView)findViewById(R.id.imga);
        imgb = (ImageView)findViewById(R.id.imgb);
        imgc = (ImageView)findViewById(R.id.imgc);

        corrdialog = new Dialog(Page70401Activity.this);
        crossdialog = new Dialog(Page70401Activity.this);

        corrdialog.setContentView(R.layout.correct_dialog);
        crossdialog.setContentView(R.layout.cross_dialog);

        corrdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nextbtn = corrdialog.findViewById(R.id.nextbtn);
        correct_content = corrdialog.findViewById(R.id.correct_content);
        nexttxt = corrdialog.findViewById(R.id.nexttxt);


        crossdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        backbtn = crossdialog.findViewById(R.id.backbtn);
        cross_content = crossdialog.findViewById(R.id.cross_content);


        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page70401Activity.this,Page704Activity.class);
                startActivity(back);
            }
        });

        imga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crossdialog.show();
                cross_content.setText("遭受到欺負\n要勇於保護自己");
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crossdialog.dismiss();
                    }
                });

            }
        });
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corrdialog.show();
                correct_content.setText("真棒!\n被欺負時\n就應該要好好保護自己");
                nexttxt.setText("恭喜你完成挑戰");
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next = new Intent(Page70401Activity.this,Page7Activity.class);
                        startActivity(next);
                    }
                });
            }
        });
        imgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corrdialog.show();
                correct_content.setText("真棒!\n答對了!");
                nexttxt.setText("恭喜你完成挑戰");
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next = new Intent(Page70401Activity.this,Page7Activity.class);
                        startActivity(next);
                    }
                });
            }
        });




    }
}