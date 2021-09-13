package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.imagelistHolder> {

    Context context;
    List<ImageList> imageLists;

    public ImageListAdapter (Context context,List<ImageList> imageLists){
        this.context = context;
        this.imageLists = imageLists;
    }
    @NonNull
    @Override
    public imagelistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View Imagelayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_list,parent,false);
        return new imagelistHolder(Imagelayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.imagelistHolder holder, int position) {
        ImageList i = imageLists.get(position);
        //Glide.with(context).load(i.getImageUrl()).into(holder.img);
        Picasso.with(context).load(i.getImageUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return imageLists.size();
    }
    public static class imagelistHolder extends RecyclerView.ViewHolder{
        ImageView img;
        public imagelistHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView6);
        }
    }
}
