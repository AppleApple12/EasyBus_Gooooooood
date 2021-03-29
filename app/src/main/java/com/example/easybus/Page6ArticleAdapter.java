package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Page6ArticleAdapter extends RecyclerView.Adapter<Page6AritcleViewHolder> {
    ArrayList<Page6article> articles;
    public Page6ArticleAdapter() {
        articles = new ArrayList<>();
    }
    public void setData(ArrayList<Page6article> articles){
        this.articles = articles;
    }
    public  void filterList(ArrayList<Page6article> filterList){
        articles = filterList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Page6AritcleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View articleView = layoutInflater.inflate(R.layout.recyclerview_item,parent,false);
        return new Page6AritcleViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(@NonNull Page6AritcleViewHolder holder, int position) {
        Page6article article = articles.get(position);
        holder.txv_line.setText("路線:"+article.line);
        holder.txv_linename.setText("路線名稱:"+article.line_name);
        //holder.txv_stampnum.setText("站序:"+article.stamp_num);
        holder.txv_chinesestamp.setText("中文站點名稱:"+article.chinese_stamp);
        //holder.txv_longitude.setText("經度:"+article.longitude);
        //holder.txv_latitude.setText("緯度:"+article.latitude);
        holder.txv_comeback.setText("去回:"+article.come_back);
        //holder.txv_englishstamp.setText("英文站點名稱:"+article.english_stamp);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


}
