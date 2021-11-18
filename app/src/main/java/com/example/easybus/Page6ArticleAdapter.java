package com.example.easybus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    ArrayList<Page6article> articlesFilter;

    OnItemClickListener listener;
    public Page6ArticleAdapter() {
        articles1 = new ArrayList<>();
        articlesFilter = new ArrayList<>();
        articlesFilter.addAll(articles1);
    }
    public void setData(ArrayList<Page6article> articles1){
        this.articles1 = articles1;
    }

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
        holder.txv_line.setText(article.line);
        holder.txv_linename.setText(article.line_name);
        if(article.line.length()>5)
            holder.txv_line.setTextSize(15);
        if(article.line_name.length()>22)
            holder.txv_linename.setTextSize(14);

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

    public Filter getFilter(){
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Page6article> filteredList = new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filteredList.addAll(articlesFilter);
            }else{
                for(Page6article item:articles1){
                    if(item.getLine().toLowerCase().contains(constraint.toString().toLowerCase())){
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
}
