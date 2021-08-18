package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Page70301Activity extends AppCompatActivity {
    TextView question,ans1,ans2,ans3,number,content,nexttxt,backtxt,fcontent;
    String getmail;
    ImageView imga,imgb,imgc,nextbtn,backbtn;
    Dialog Tdialog,Fdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page70301);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        //Tdialog的東西
        Tdialog = new Dialog(Page70301Activity.this);
        Tdialog.setContentView(R.layout.correct_dialog);
        Tdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nexttxt=Tdialog.findViewById(R.id.nexttxt);
        nextbtn=Tdialog.findViewById(R.id.nextbtn);
        content=Tdialog.findViewById(R.id.correct_content);
        //Fdialog的東西
        Fdialog = new Dialog(Page70301Activity.this);
        Fdialog.setContentView(R.layout.cross_dialog);
        Fdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        backtxt=Fdialog.findViewById(R.id.backtxt);
        backbtn=Fdialog.findViewById(R.id.backbtn);
        fcontent=Fdialog.findViewById(R.id.cross_content);


        question = findViewById(R.id.question);
        number=findViewById(R.id.number);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        imga = findViewById(R.id.imga);
        imgb = findViewById(R.id.imgb);
        imgc = findViewById(R.id.imgc);
        q1("小惠乘車時想吹風，\n於是想打開窗戶探出頭","1/2","將窗戶打開，探出頭享受風的吹拂","不能將窗戶打開探出頭或者伸出手","窗戶開一半沒關係，不會有危險",imga,imgb,imgc);

    }
    //正確的 dialog text內容
    private void showTdialog(final String T,final String next){
        nexttxt.setText(next);
        content.setText(T);
    }
    //錯誤的 dialog text內容
    private void showFdialog(final String F){
        fcontent.setText(F);
    }

    //第一題
    private void q1(final String q, final String num, final String a1, final String a2, final String a3, ImageView a,ImageView b,ImageView c){
        question.setText(q);
        number.setText(num);
        ans1.setText(a1);
        ans2.setText(a2);
        ans3.setText(a3);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("不可以開窗戶!\n這樣非常危險");
                Fdialog.show();
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fdialog.dismiss();
                    }
                });
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("真棒!\n答對了!","下一題");
                Tdialog.show();
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("不可以開窗戶!\n這樣非常危險");
                Fdialog.show();
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fdialog.dismiss();
                    }
                });
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tdialog.dismiss();
                q2("小惠在公車上奔跑是對的示範嗎 ?","2/2","對的，只要沒有跌倒就沒關係","對的，能及時下車就沒問題","錯的，在公車上奔跑非常危險",imga,imgb,imgc);
            }
        });

    }
    //第二題
    private void q2(final String q,final String num,final String a1,final String a2,final String a3, ImageView a,ImageView b,ImageView c){
        question.setText(q);
        number.setText(num);
        ans1.setText(a1);
        ans2.setText(a2);
        ans3.setText(a3);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("公車上不能奔跑!\n這樣非常危險");
                Fdialog.show();
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fdialog.dismiss();
                    }
                });
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("公車上不能奔跑!\n這樣非常危險");
                Fdialog.show();
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fdialog.dismiss();
                    }
                });
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("真棒!\n答對了!","恭喜你完成挑戰");
                Tdialog.show();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tdialog.dismiss();
                Intent intent = new Intent(Page70301Activity.this,Page7Activity.class);
                startActivity(intent);
            }
        });
    }
}
