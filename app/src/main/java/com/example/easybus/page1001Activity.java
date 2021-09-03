package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class page1001Activity extends AppCompatActivity {
    private calendar2 cal2;
    String dayStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1001);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        dayStr = intent.getStringExtra("dayStr");
        System.out.println("dayStr : "+dayStr);
        //跳頁回家長主頁
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(page1001Activity.this,Page4Activity.class);
                startActivity(it1);
            }
        });

        cal2 = (calendar2)findViewById(R.id.cal2);

        //TODO模擬請求當月數據
        final List<page1001Activity.DayFinish2> list = new ArrayList<>();
        list.add(new page1001Activity.DayFinish2(1,2,2));
        list.add(new page1001Activity.DayFinish2(2,1,2));
        list.add(new page1001Activity.DayFinish2(3,0,2));
        list.add(new page1001Activity.DayFinish2(4,2,2));
        list.add(new page1001Activity.DayFinish2(5,2,2));
        list.add(new page1001Activity.DayFinish2(6,2,2));
        list.add(new page1001Activity.DayFinish2(7,2,2));
        list.add(new page1001Activity.DayFinish2(8,0,2));
        list.add(new page1001Activity.DayFinish2(9,1,2));
        list.add(new page1001Activity.DayFinish2(8,0,2));
        list.add(new page1001Activity.DayFinish2(9,1,2));
        list.add(new page1001Activity.DayFinish2(10,2,2));
        list.add(new page1001Activity.DayFinish2(11,5,2));
        list.add(new page1001Activity.DayFinish2(12,2,2));
        list.add(new page1001Activity.DayFinish2(13,2,2));
        list.add(new page1001Activity.DayFinish2(14,3,2));
        list.add(new page1001Activity.DayFinish2(15,2,2));
        list.add(new page1001Activity.DayFinish2(14,3,2));
        list.add(new page1001Activity.DayFinish2(15,2,2));
        list.add(new page1001Activity.DayFinish2(16,1,2));
        list.add(new page1001Activity.DayFinish2(17,0,2));
        list.add(new page1001Activity.DayFinish2(18,2,2));
        list.add(new page1001Activity.DayFinish2(19,2,2));
        list.add(new page1001Activity.DayFinish2(20,0,2));
        list.add(new page1001Activity.DayFinish2(21,2,2));
        list.add(new page1001Activity.DayFinish2(22,1,2));
        list.add(new page1001Activity.DayFinish2(23,2,0));
        list.add(new page1001Activity.DayFinish2(24,0,2));
        list.add(new page1001Activity.DayFinish2(25,2,2));
        list.add(new page1001Activity.DayFinish2(26,2,2));
        list.add(new page1001Activity.DayFinish2(27,2,2));
        list.add(new page1001Activity.DayFinish2(28,2,2));
        list.add(new page1001Activity.DayFinish2(29,2,2));
        list.add(new page1001Activity.DayFinish2(30,2,2));
        list.add(new page1001Activity.DayFinish2(31,2,2));//跳頁回家長主頁

        cal2.setRenwu("2021","2021-09",list);
        cal2.setOnClickListen(new calendar2.onClickListener() {
            @Override
            public void onLeftRowClick() {
                Toast.makeText(page1001Activity.this,"點擊減箭頭",Toast.LENGTH_SHORT).show();
                cal2.monthChange(-1);
                if(cal2.m.equals("JAN")){
                    cal2.yearChange(-1);
                }
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            Thread.sleep(1000);
                            page1001Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cal2.setRenwu(list);
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                }.start();
            }

            @Override
            public void onRightRowClick() {
                Toast.makeText(page1001Activity.this,"點擊加箭頭",Toast.LENGTH_SHORT).show();
                cal2.monthChange(1);
                if(cal2.m.equals("DEC")){
                    cal2.yearChange(1);
                }
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            Thread.sleep(1000);
                            page1001Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cal2.setRenwu(list);
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                }.start();
            }

            @Override
            public void onTitleClick(String monStr, Date month) {

            }

            @Override
            public void onWeekClick(int weekIndex, String weekStr) {

            }

            @Override
            public void onDayClick(int day, String dayStr, DayFinish2 finish) {

            }
        });
    }

    public class DayFinish2 {
        int day,all,finish;
        public DayFinish2(int day,int finish,int all){
            this.day = day;
            this.all = all;
            this.finish = finish;
        }
    }
}