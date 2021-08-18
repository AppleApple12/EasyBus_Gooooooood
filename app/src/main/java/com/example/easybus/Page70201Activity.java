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

public class Page70201Activity extends AppCompatActivity {
    TextView question,ans1,ans2,ans3,number,content,nexttxt,backtxt,fcontent;
    String getmail;
    ImageView imga,imgb,imgc,nextbtn,backbtn;
    Dialog Tdialog,Fdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page70201);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        //Tdialog的東西
        Tdialog = new Dialog(Page70201Activity.this);
        Tdialog.setContentView(R.layout.correct_dialog);
        Tdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nexttxt=Tdialog.findViewById(R.id.nexttxt);
        nextbtn=Tdialog.findViewById(R.id.nextbtn);
        content=Tdialog.findViewById(R.id.correct_content);
        //Fdialog的東西
        Fdialog = new Dialog(Page70201Activity.this);
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
        q1("小豪鼻子想打噴嚏時，\n該怎麼做才好呢?","1/2","直接大聲打噴嚏","對著窗戶打噴嚏沒關係","遮住口鼻後，\n再打噴嚏",imga,imgb,imgc);
    }
    /////正確的 dialog text內容
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
                showFdialog("不可以唷!\n這樣可能會\n把病毒傳染給其他人!");
                Fdialog.show();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("這是不對的!\n要記得用手遮住口鼻!");
                Fdialog.show();
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("很棒!\n是正確的作法!!","下一題");
                Tdialog.show();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tdialog.dismiss();
                q2("小豪乘車時想看影片，\n但沒戴耳機，\n請問他應該怎麼做 ?","2/2"," 將音量調小，\n不要影響到其他乘客","將音量調最大，\n這樣看比較享受","將音量關閉，\n沒有聲音也沒關係",imga,imgb,imgc);
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
                showTdialog("對了!\n做得很好!","恭喜你完成挑戰");
                Tdialog.show();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFdialog("不可以唷!\n這樣會影響到其他的乘客");
                Fdialog.show();
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("答對了!\n雖然沒有聲音有點無聊，\n但就不會打擾到別人了!","恭喜你完成挑戰");
                Tdialog.show();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tdialog.dismiss();
                Intent intent = new Intent(Page70201Activity.this,Page7Activity.class);
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