package com.example.easybus;
/*搭車*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class Page61Holder extends RecyclerView.Adapter<Page61Holder.ViewHolder>{
    OnItemClickListener listener;
    String img;
    @NonNull
    @Override
    public Page61Holder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview03,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Page61Holder.ViewHolder holder, final int position) {
        holder.imageView.setImageResource(Page61.arrayList2.get(position).get("busphoto"));
        holder.txv.setText(Page61.arrayList1.get(position).get("busname"));
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
        return Page61.arrayList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txv = itemView.findViewById(R.id.txv);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(Page61Holder.OnItemClickListener listener){
        this.listener = listener;
    }


}
