package com.example.easybus;
/*搭車*/
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Page612ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Page6article> page612R;

    public Page612ArticleAdapter() {
        page612R=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            View Page612View2=View.inflate(parent.getContext(),R.layout.page612recyclerview2,null);
            return new page612View2(Page612View2);
        }else if(viewType==2){
            View Page612View=View.inflate(parent.getContext(),R.layout.page612recyclerview,null);
            return new page612View1(Page612View);
        }
        else if(viewType==3){
            View Page612View3=View.inflate(parent.getContext(),R.layout.page612recyclerview3,null);
            return new page612View3(Page612View3);
        }else if(viewType==4){
            View Page612View4=View.inflate(parent.getContext(),R.layout.page612recyclerview4,null);
            return new page612View4(Page612View4);
        }
        return null;
    }

    public void setData(ArrayList<Page6article> page612R){
        this.page612R=page612R;
    }

    @Override
    public int getItemViewType(int position) {
        if (page612R.size()!=1 && position==0)
            return 1;
        else if(page612R.size()!=1 && position==page612R.size()-1 )
            return 3;
        else if(page612R.size()==1)
            return 4;
        else
            return 2;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof page612View2){
            page612View2 holderview2= (page612View2)holder;
            Page6article page612r=page612R.get(position);
            holderview2.mBusline.setText(page612r.line);
            try{
                if(Integer.parseInt(page612r.time)<=3) {
                    holderview2.mBustime.setText("即將進站");
                    holderview2.mBustime.setTextColor(Color.parseColor("#ffffff"));
                    holderview2.mBustime.setBackgroundResource(R.drawable.bus_in);
                }else{
                    holderview2.mBustime.setText(page612r.time+" 分");
                    holderview2.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview2.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }catch(Exception e){
                if(page612r.time.equals("末班駛離")) {
                    holderview2.mBustime.setText(page612r.time);
                    holderview2.mBustime.setTextColor(Color.parseColor("#f0f0f0"));
                    holderview2.mBustime.setBackgroundResource(R.drawable.bus_final);
                }else{
                    holderview2.mBustime.setText(page612r.time);
                    holderview2.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview2.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }
        }else if(holder instanceof page612View1){
            page612View1 holderview1= (page612View1)holder;
            Page6article page612r=page612R.get(position);
            holderview1.mBusline.setText(page612r.line);
            try{
                if(Integer.parseInt(page612r.time)<=3) {
                    holderview1.mBustime.setText("即將進站");
                    holderview1.mBustime.setTextColor(Color.parseColor("#ffffff"));
                    holderview1.mBustime.setBackgroundResource(R.drawable.bus_in);
                }else{
                    holderview1.mBustime.setText(page612r.time+" 分");
                    holderview1.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview1.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }catch(Exception e){
                if(page612r.time.equals("末班駛離")) {
                    holderview1.mBustime.setText(page612r.time);
                    holderview1.mBustime.setTextColor(Color.parseColor("#f0f0f0"));
                    holderview1.mBustime.setBackgroundResource(R.drawable.bus_final);
                }else{
                    holderview1.mBustime.setText(page612r.time);
                    holderview1.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview1.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }
        }else if(holder instanceof page612View4){
            page612View4 holderview4= (page612View4)holder;
            Page6article page612r=page612R.get(position);
            holderview4.mBusline.setText(page612r.line);
            try{
                if(Integer.parseInt(page612r.time)<=3) {
                    holderview4.mBustime.setText("即將進站");
                    holderview4.mBustime.setTextColor(Color.parseColor("#ffffff"));
                    holderview4.mBustime.setBackgroundResource(R.drawable.bus_in);
                }else{
                    holderview4.mBustime.setText(page612r.time+" 分");
                    holderview4.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview4.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }catch(Exception e){
                if(page612r.time.equals("末班駛離")) {
                    holderview4.mBustime.setText(page612r.time);
                    holderview4.mBustime.setTextColor(Color.parseColor("#f0f0f0"));
                    holderview4.mBustime.setBackgroundResource(R.drawable.bus_final);
                }else{
                    holderview4.mBustime.setText(page612r.time);
                    holderview4.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview4.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }
        }else{
            page612View3 holderview3= (page612View3)holder;
            Page6article page612r=page612R.get(position);
            holderview3.mBusline.setText(page612r.line);
            try{
                if(Integer.parseInt(page612r.time)<=3) {
                    holderview3.mBustime.setText("即將進站");
                    holderview3.mBustime.setTextColor(Color.parseColor("#ffffff"));
                    holderview3.mBustime.setBackgroundResource(R.drawable.bus_in);
                }else{
                    holderview3.mBustime.setText(page612r.time+" 分");
                    holderview3.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview3.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }catch(Exception e){
                if(page612r.time.equals("末班駛離")) {
                    holderview3.mBustime.setText(page612r.time);
                    holderview3.mBustime.setTextColor(Color.parseColor("#f0f0f0"));
                    holderview3.mBustime.setBackgroundResource(R.drawable.bus_final);
                }else{
                    holderview3.mBustime.setText(page612r.time);
                    holderview3.mBustime.setTextColor(Color.parseColor("#000000"));
                    holderview3.mBustime.setBackgroundResource(R.drawable.page601_btn);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return page612R.size();
    }
}
