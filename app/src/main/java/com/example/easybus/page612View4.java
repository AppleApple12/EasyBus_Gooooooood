package com.example.easybus;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class page612View4 extends RecyclerView.ViewHolder {
    TextView mBusline,mBustime;
    public page612View4(View page612View4) {
        super(page612View4);
        mBusline=itemView.findViewById(R.id.busline);
        mBustime=itemView.findViewById(R.id.bustime);
    }
}
