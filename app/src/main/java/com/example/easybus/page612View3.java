package com.example.easybus;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class page612View3 extends RecyclerView.ViewHolder {
    TextView mBusline,mBustime;
    public page612View3(View page612View3) {
        super(page612View3);
        mBusline=itemView.findViewById(R.id.busline);
        mBustime=itemView.findViewById(R.id.bustime);
    }
}
