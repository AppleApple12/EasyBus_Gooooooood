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
import android.widget.ImageView;
import android.widget.TextView;

public class Page70101Activity extends AppCompatActivity {
    TextView question,ans1,ans2,ans3,number,content,nexttxt;
    String getmail;
    ImageView imga,imgb,imgc,nextbtn;
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

        question = findViewById(R.id.question);
        number=findViewById(R.id.number);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        imga = findViewById(R.id.imga);
        imgb = findViewById(R.id.imgb);
        imgc = findViewById(R.id.imgc);
        q1("小美在等待公車來時，\n應該注意甚麼呢 ?","1/2","繞過汽車查看公車","在候車線內等待","滑手機不注意公車");



    }
    private void showTdialog(final String T,final String next){
        nexttxt.setText(next);
        content.setText(T);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tdialog.dismiss();
                q2("當要上公車時，\n小美應該怎麼做呢 ?\n","2/2","搶先跑上車","等到車上的人下車後排隊上車","覺得搭公車很有趣，\n因此用跳的上車");
            }
        });
    }
    private void q1(final String q,final String num,final String a1,final String a2,final String a3){
        question.setText(q);
        number.setText(num);
        ans1.setText(a1);
        ans2.setText(a2);
        ans3.setText(a3);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("真棒!\n答對了!","下一題");
                Tdialog.show();
            }
        });

    }
    private void q2(final String q,final String num,final String a1,final String a2,final String a3){
        question.setText(q);
        number.setText(num);
        ans1.setText(a1);
        ans2.setText(a2);
        ans3.setText(a3);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTdialog("真棒!答對了!","恭喜你完成挑戰");
                Tdialog.dismiss();
                Intent intent = new Intent(Page70101Activity.this,Page7Activity.class);
                startActivity(intent);
            }/////
        });
    }
}