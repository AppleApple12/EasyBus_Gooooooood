package com.example.easybus;
/*公車資訊*/
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BusInfoViewHolder extends RecyclerView.ViewHolder {

    ImageView mImg;
    TextView mText;
    public BusInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        mImg=itemView.findViewById(R.id.img);
        mImg.setImageResource(R.drawable.page5012modepic);
        mText=itemView.findViewById(R.id.Text);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(mText,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(mText, 18,
                26, 1, TypedValue.COMPLEX_UNIT_SP);
    }
}
