package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class friendAdapter extends RecyclerView.Adapter<friendAdapter.frinedHolder>{
    Context context;
    List<friend> friendList;
    List<ImageList> imageLists;
    AdapterView.OnItemClickListener listener;
    public friendAdapter(Context context, List<friend> friendList,List<ImageList> imageLists) {
        this.context = context;
        this.friendList = friendList;
        this.imageLists = imageLists;
    }

    @NonNull
    //@org.jetbrains.annotations.NotNull
    @Override
    public frinedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View frinedlayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_listview,parent,false);
        return new frinedHolder(frinedlayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final friendAdapter.frinedHolder holder, final int position) {
        friend f = friendList.get(position);
        holder.mf_name.setText(f.getF_name());
        holder.mf_phone.setText(f.getF_phone());
        Picasso.with(context).load(f.getImageUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        System.out.println("friendList :"+friendList.size());
        return friendList.size();
    }

    public class frinedHolder extends RecyclerView.ViewHolder {
        TextView mf_name,mf_phone;
        ImageView img;
        public frinedHolder(@NonNull View itemView) {
            super(itemView);
            mf_name=itemView.findViewById(R.id.fname);
            mf_phone=itemView.findViewById(R.id.fphone);
            img = itemView.findViewById(R.id.imageView6);
        }
    }
}