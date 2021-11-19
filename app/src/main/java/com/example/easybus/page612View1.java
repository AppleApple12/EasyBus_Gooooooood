package com.example.easybus;
/*搭車*/
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class page612View1 extends RecyclerView.ViewHolder {
    TextView mBusline,mBustime;
    public page612View1(View page612View) {
        super(page612View);
        mBusline=itemView.findViewById(R.id.busline);
        mBustime=itemView.findViewById(R.id.bustime);
    }
}
