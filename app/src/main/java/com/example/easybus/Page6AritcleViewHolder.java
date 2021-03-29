package com.example.easybus;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Page6AritcleViewHolder extends RecyclerView.ViewHolder {
    TextView txv_line,txv_linename,txv_stampnum,txv_chinesestamp,txv_longitude,txv_latitude,txv_comeback;//txv_englishstamp;
    public Page6AritcleViewHolder(@NonNull View itemView) {
        super(itemView);
        txv_line = itemView.findViewById(R.id.txvline);
        txv_linename = itemView.findViewById(R.id.txvlinename);
        //txv_stampnum = itemView.findViewById(R.id.txvstampnum);
        txv_chinesestamp = itemView.findViewById(R.id.txvchinese);
        //txv_longitude = itemView.findViewById(R.id.txvlongitude);
        //txv_latitude = itemView.findViewById(R.id.txvlatitude);
        txv_comeback = itemView.findViewById(R.id.txvcomeback);
        //txv_englishstamp = itemView.findViewById(R.id.txvenglish);

    }
}
