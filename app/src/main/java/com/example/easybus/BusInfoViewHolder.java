package com.example.easybus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusInfoViewHolder extends RecyclerView.ViewHolder {

    //ImageView mImg;
    TextView mNum,mText;
    public BusInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        //mImg=itemView.findViewById(R.id.img);
        mNum=itemView.findViewById(R.id.Num);
        mText=itemView.findViewById(R.id.Text);
    }
}
