package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    Context context;
    List<friend> friendList;
    OnItemClickListener listener;

    public HistoryAdapter(Context context, List<friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View Historylayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_listview,parent,false);
        return new HistoryAdapter.HistoryHolder(Historylayout);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, final int position) {
        friend f = friendList.get(position);
        holder.mf_name.setText(f.getF_name());
        Picasso.with(context).load(f.getImageUrl()).into(holder.img);
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
        return friendList.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView mf_name;
        ImageView img;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            mf_name=itemView.findViewById(R.id.fname);
            img = itemView.findViewById(R.id.imageView6);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(HistoryAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
