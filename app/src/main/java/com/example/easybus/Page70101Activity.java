package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.TimedMetaData;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Page70101Activity extends AppCompatActivity {
    TextView question,ans1,ans2,ans3,number,content,nexttxt,backtxt,fcontent;
    String getmail;
    ImageView imga,imgb,imgc,nextbtn,backbtn;
    Dialog Tdialog,Fdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page70101);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        //Tdialog的東西
        Tdialog = new Dialog(Page70101Activity.this);
        Tdialog.setContentView(R.layout.correct_dialog);
        Tdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nexttxt=Tdialog.findViewById(R.id.nexttxt);
        nextbtn=Tdialog.findViewById(R.id.nextbtn);
        content=Tdialog.findViewById(R.id.correct_content);
        //Fdialog的東西
        Fdialog = new Dialog(Page70101Activity.this);
        Fdialog.setContentView(R.layout.cross_dialog);
        Fdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //backtxt=Fdialog.findViewById(R.id.backtxt);
        backbtn=Fdialog.findViewById(R.id.backbtn);
        fcontent=Fdialog.findViewById(R.id.cross_content);
        //原本定義的東西
        question = findViewById(R.id.question);
        number=findViewById(R.id.number);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        imga = findViewById(R.id.imga);
        imgb = findViewById(R.id.imgb);
        imgc = findViewById(R.id.imgc);
        q1("小美在等待公車來時，\n應該注意甚麼呢 ?","1/2","繞過汽車查看公車","在候車線內等待","滑手機不注意公車",imga,imgb,imgc);
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page70101Activity.this,Page7Activity.class);
                startActivity(back);
            }
        });
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
                showFdialog("不對唷!\n這是錯誤示範!");
                Fdialog.show();
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
                showFdialog("不對唷!\n這是錯誤示範!");
                Fdialog.show();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tdialog.dismiss();
                q2("當要上公車時，\n小美應該怎麼做呢 ?","2/2","搶先跑上車","等到車上的人下車後\n再排隊上車","覺得搭公車很有趣，\n所以用跳的上車",imga,imgb,imgc);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fdialog.dismiss();
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
                showFdialog("不對唷!\n這是錯誤示範!");
                Fdialog.show();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("真棒!\n答對了!","恭喜你完成挑戰");
                Tdialog.show();
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("不對唷!\n這是錯誤示範!");
                Fdialog.show();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tdialog.dismiss();
                Intent intent = new Intent(Page70101Activity.this,Page7Activity.class);
                startActivity(intent);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fdialog.dismiss();
            }
        });
    }
}