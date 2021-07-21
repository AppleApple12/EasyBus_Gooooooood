package com.example.easybus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Page6ArticleAdapter extends RecyclerView.Adapter<Page6AritcleViewHolder> implements Filterable{
    ArrayList<Page6article> articles1;
    ArrayList<Page6article> articles1Filter;
    OnItemClickListener listener;
    public Page6ArticleAdapter() {
        articles1 = new ArrayList<>();
        articles1Filter = new ArrayList<>();
        articles1Filter.addAll(articles1);
    }
    public void setData(ArrayList<Page6article> articles1){
        this.articles1 = articles1;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Page6article> filteredList = new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filteredList.addAll(articles1Filter);
            }else{
                for(Page6article item:articles1Filter){
                    if(item.getLine().toLowerCase().contains(constraint.toString().toLowerCase().trim())){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            articles1.clear();
            articles1.addAll((Collection<? extends Page6article>)results.values);
            notifyDataSetChanged();
        }
    };


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public Page6AritcleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View articleView = layoutInflater.inflate(R.layout.recyclerview_item,parent,false);
        return new Page6AritcleViewHolder(articleView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Page6AritcleViewHolder holder, final int position) {
        final Page6article article = articles1.get(position);
        holder.txv_line.setText("路線:"+article.line);
        holder.txv_linename.setText("路線名稱:"+article.line_name);
        //holder.txv_stampnum.setText("站序:"+article.stamp_num);
        //holder.txv_chinesestamp.setText("中文站點名稱:"+article.chinese_stamp);
        //holder.txv_longitude.setText("經度:"+article.longitude);
        //holder.txv_latitude.setText("緯度:"+article.latitude);
        //holder.txv_comeback.setText("去回:"+article.come_back);
        //holder.txv_englishstamp.setText("英文站點名稱:"+article.english_stamp);

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
        return articles1.size();
    }


}
