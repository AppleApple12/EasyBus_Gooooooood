package com.example.easybus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class page62_testAdapter extends RecyclerView.Adapter<page62_testViewHolder> {

    ArrayList<page62_testName> nameArrayList;
    public page62_testAdapter(){
        nameArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public page62_testViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View nameView = layoutInflater.inflate(R.layout.page62_testrecyclerview,parent,false);
        return new page62_testViewHolder(nameView);
    }

    @Override
    public void onBindViewHolder(@NonNull page62_testViewHolder holder, int position) {
        final page62_testName testName = nameArrayList.get(position);
        holder.txv_platenum.setText("PlateNumb:"+testName.PlateNumb);
        holder.txv_stopname.setText("StopName:"+testName.StopName);
        holder.txv_direction.setText("Direction:"+testName.Direction);
        holder.txv_estimate.setText("Estimate:"+testName.Estimate);
    }

    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }

    public void setData(ArrayList<page62_testName> nameArrayList){
        this.nameArrayList = nameArrayList;

    }

}
