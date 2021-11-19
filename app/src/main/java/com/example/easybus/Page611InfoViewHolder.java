package com.example.easybus;
/*搭車*/
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Page611InfoViewHolder extends RecyclerView.ViewHolder {
    TextView mHtmlText,mStep;
    public Page611InfoViewHolder(@NonNull View itemView) {
        super(itemView);
        mHtmlText=itemView.findViewById(R.id.htmlText);
        mStep=itemView.findViewById(R.id.step);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(mHtmlText,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(mHtmlText, 22,26, 1, TypedValue.COMPLEX_UNIT_SP);
    }
}
