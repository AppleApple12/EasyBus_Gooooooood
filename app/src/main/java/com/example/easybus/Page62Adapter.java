package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class Page62Adapter extends RecyclerView.Adapter<Page62Adapter.ViewHolder> {
    ArrayList<Page62bus> arrayList1;

    public Page62Adapter() {
        arrayList1 = new ArrayList<>();
    }
    public void setData(ArrayList<Page62bus> arrayList1){
        this.arrayList1 = arrayList1;
    }
    @NonNull
    @Override
    public Page62Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View articleView = layoutInflater.inflate(R.layout.recyclerview04,parent,false);
        return new ViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(@NonNull Page62Adapter.ViewHolder holder, int position) {
        Page62bus bus = arrayList1.get(position);
        holder.txv_RouteID.setText("路線名稱："+bus.RouteID);
        holder.txv_Direction.setText("Direction："+bus.Direction);
        holder.txv_PlateNumb.setText("車牌："+bus.PlateNumb);
        holder.txv_GPS_Time.setText("GPS_Time："+bus.GPS_Time);
        holder.txv_X.setText("X："+bus.X);
        holder.txv_Y.setText("Y："+bus.Y);
    }

    @Override
    public int getItemCount() {
        return arrayList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txv_RouteID,txv_Direction,txv_PlateNumb,txv_GPS_Time,txv_X,txv_Y;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txv_RouteID = itemView.findViewById(R.id.txv_RouteID);
            txv_Direction = itemView.findViewById(R.id.txv_Direction);
            txv_PlateNumb = itemView.findViewById(R.id.txv_PlateNumb);
            txv_GPS_Time = itemView.findViewById(R.id.txv_GPS_Time);
            txv_X = itemView.findViewById(R.id.txv_X);
            txv_Y = itemView.findViewById(R.id.txv_Y);
        }
    }

}
