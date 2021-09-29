package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Page10Activity extends AppCompatActivity {

    private calendar cal;
    public Date yyyy,mm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page10);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

      //  List<String> d = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat ysdf=new SimpleDateFormat("yyyy");
        SimpleDateFormat Msdf=new SimpleDateFormat("yyyy-MM");
        String ydate=ysdf.format(date);
        //yyyy = str2Date(ydate);
        //System.out.println("yyyyyyyyyy :"+yyyy);
        String Mdate=Msdf.format(date);
        //mm = str2Date(Mdate);
       // System.out.println("ydate : "+ydate);
       // for(String retval:date.split("-"))
         //   d.add(retval);

        //跳頁回家長主頁
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page10Activity.this,Page4Activity.class);
                startActivity(it1);
            }
        });

        cal = (calendar)findViewById(R.id.cal);

        //TODO模擬請求當月數據
        final List<DayFinish> list = new ArrayList<>();
        list.add(new DayFinish(1,2,2));
        list.add(new DayFinish(2,1,2));
        list.add(new DayFinish(3,0,2));
        list.add(new DayFinish(4,2,2));
        list.add(new DayFinish(5,2,2));
        list.add(new DayFinish(6,2,2));
        list.add(new DayFinish(7,2,2));
        list.add(new DayFinish(8,0,2));
        list.add(new DayFinish(9,1,2));
        list.add(new DayFinish(8,0,2));
        list.add(new DayFinish(9,1,2));
        list.add(new DayFinish(10,2,2));
        list.add(new DayFinish(11,5,2));
        list.add(new DayFinish(12,2,2));
        list.add(new DayFinish(13,2,2));
        list.add(new DayFinish(14,3,2));
        list.add(new DayFinish(15,2,2));
        list.add(new DayFinish(14,3,2));
        list.add(new DayFinish(15,2,2));
        list.add(new DayFinish(16,1,2));
        list.add(new DayFinish(17,0,2));
        list.add(new DayFinish(18,2,2));
        list.add(new DayFinish(19,2,2));
        list.add(new DayFinish(20,0,2));
        list.add(new DayFinish(21,2,2));
        list.add(new DayFinish(22,1,2));
        list.add(new DayFinish(23,2,0));
        list.add(new DayFinish(24,0,2));
        list.add(new DayFinish(25,2,2));
        list.add(new DayFinish(26,2,2));
        list.add(new DayFinish(27,2,2));
        list.add(new DayFinish(28,2,2));
        list.add(new DayFinish(29,2,2));
        list.add(new DayFinish(30,2,2));
        list.add(new DayFinish(31,2,2));
        /*String nowday[]=new String[2];
        int n = d.size();
        int i=1;
        while (n==0){
            nowday[n]=d.toString();
            n--;
        }
        System.out.println("nowday : "+nowday);*/
        cal.setRenwu(ydate,Mdate,list);
        cal.setOnClickListen(new calendar.onClickListener() {
            @Override
            public void onLeftRowClick() {
                Toast.makeText(Page10Activity.this,"點擊減箭頭",Toast.LENGTH_SHORT).show();
                cal.monthChange(-1);
                if(cal.m.equals("JAN")){
                    cal.yearChange(-1);
                }
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            Thread.sleep(1000);
                            Page10Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cal.setRenwu(list);
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                }.start();

            }

            @Override
            public void onRightRowClick() {
                Toast.makeText(Page10Activity.this,"點擊加箭頭",Toast.LENGTH_SHORT).show();
                cal.monthChange(1);
                if(cal.m.equals("DEC")){
                    cal.yearChange(1);
                }
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            Thread.sleep(1000);
                            Page10Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cal.setRenwu(list);
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                }.start();

            }

            @Override
            public void onTitleClick(String monStr, Date month) {
                Toast.makeText(Page10Activity.this,"點擊了標題："+monStr,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWeekClick(int weekIndex, String weekStr) {
                Toast.makeText(Page10Activity.this,"點擊了星期："+weekStr,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDayClick(int day, String dayStr, DayFinish finish) {
                Toast.makeText(Page10Activity.this,"點擊了日期："+dayStr,Toast.LENGTH_SHORT).show();
                Log.w("","點擊了日期："+dayStr);
                Intent intent =new Intent(Page10Activity.this,page101Activity.class);
                intent.putExtra("dayStr",dayStr);
                startActivity(intent);
            }
        });
    }

    class DayFinish{
        int day,all,finish;
        public DayFinish(int day,int finish,int all){
            this.day = day;
            this.all = all;
            this.finish = finish;
        }
    }

}
