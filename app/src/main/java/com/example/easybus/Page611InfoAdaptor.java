package com.example.easybus;
/*搭車*/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Page611InfoAdaptor extends RecyclerView.Adapter<Page611InfoViewHolder> {
    ArrayList<Page611Info> page611infos;
    OnItemClickListener listener;

    public Page611InfoAdaptor() {
        page611infos=new ArrayList<>();
    }
    public void setData(ArrayList<Page611Info> page611infos){
        this.page611infos=page611infos;
    }

    @NonNull
    @Override
    public Page611InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View Page611InfoView=layoutInflater.inflate(R.layout.page611recyclerview,parent,false);
        return new Page611InfoViewHolder(Page611InfoView);
    }

    @Override
    public void onBindViewHolder(@NonNull Page611InfoViewHolder holder, final int position) {
        Page611Info page611info=page611infos.get(position);
        holder.mStep.setText("步驟"+(position+1));
        holder.mHtmlText.setText(page611info.htmlinstructions);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return page611infos.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(OnItemClickListener listener){
        this.listener = listener;
    }
}
