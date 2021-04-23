package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusInfoAdaptor extends RecyclerView.Adapter<BusInfoViewHolder> {
    ArrayList<BusInfo> businfos;

    public BusInfoAdaptor() {
        businfos=new ArrayList<>();
    }
    public void setData(ArrayList<BusInfo> businfos){
        this.businfos=businfos;
    }

    @NonNull
    @Override
    public BusInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View BusInfoView=layoutInflater.inflate(R.layout.googlemap_businfo,parent,false);
        return new BusInfoViewHolder(BusInfoView);
    }

    @Override
    public void onBindViewHolder(@NonNull BusInfoViewHolder holder, int position) {
        BusInfo businfo=businfos.get(position);
        holder.mNum.setText(businfo.shortname);
        holder.mText.setText(businfo.htmlinstructions);
    }

    @Override
    public int getItemCount() {
        return businfos.size();
     }
}
