package com.example.easybus;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class page62_testViewHolder extends RecyclerView.ViewHolder {
    TextView txv_platenum,txv_direction,txv_stopname,txv_estimate;
    public page62_testViewHolder(@NonNull View itemView) {
        super(itemView);
        txv_platenum = itemView.findViewById(R.id.txv_platenumb);
        txv_stopname = itemView.findViewById(R.id.txv_stopname);
        txv_direction = itemView.findViewById(R.id.txv_direction);
        txv_estimate = itemView.findViewById(R.id.txv_estimate);
    }
}
