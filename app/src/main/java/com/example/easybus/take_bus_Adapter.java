package com.example.easybus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class take_bus_Adapter extends RecyclerView.Adapter<take_bus_Adapter.take_bus_Holder>{
    Context context;
    List<take_bus_businfo> takeBusBusinfoList;
    OnItemClickListener listener;

    public take_bus_Adapter (Context context,List<take_bus_businfo> takeBusBusinfoList){
        this.context = context;
        this.takeBusBusinfoList = takeBusBusinfoList;
    }
    @NonNull
    @Override
    public take_bus_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View takebusLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.take_bus_listview,parent,false);
        return new take_bus_Holder(takebusLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final take_bus_Adapter.take_bus_Holder holder, final int position) {
        take_bus_businfo b = takeBusBusinfoList.get(position);
        holder.routename.setText(b.getRoutename());
        holder.s =b.getImage();
        System.out.println(holder.s);
        System.out.println(b.getRoutename());
        if(holder.s.equals("work")) {
            Glide.with(context).load(R.drawable.working).into(holder.imageView);
            System.out.println(Glide.with(context).load(R.drawable.working).into(holder.imageView));
        }else if (holder.s.equals("shopping")){
            Glide.with(context).load(R.drawable.shopping).into(holder.imageView);
            System.out.println(Glide.with(context).load(R.drawable.shopping).into(holder.imageView));
        }else if (holder.s.equals("home")){
            Glide.with(context).load(R.drawable.home).into(holder.imageView);
            System.out.println(Glide.with(context).load(R.drawable.home).into(holder.imageView));
        }else if (holder.s.equals("play")){
            Glide.with(context).load(R.drawable.play).into(holder.imageView);
            System.out.println(Glide.with(context).load(R.drawable.play).into(holder.imageView));
        }else if (holder.s.equals("busdetails")){
            Glide.with(context).load(R.drawable.busdetails).into(holder.imageView);
        }else{
            Glide.with(context).load(R.drawable.business).into(holder.imageView);
        }//
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
        return takeBusBusinfoList.size();
    }

    public class take_bus_Holder extends RecyclerView.ViewHolder {
        TextView routename;
        ImageView imageView;
        String s;
        public take_bus_Holder(View itemView) {
            super(itemView);
            routename=itemView.findViewById(R.id.routename);
            imageView=itemView.findViewById(R.id.busimg);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(take_bus_Adapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
