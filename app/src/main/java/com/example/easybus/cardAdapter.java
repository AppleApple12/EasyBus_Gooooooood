package com.example.easybus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class cardAdapter extends PagerAdapter {

    private Context context;
    private List<friend> friendList;

    public cardAdapter(Context context, List<friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }


    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item,container,false);

        ImageView img = view.findViewById(R.id.img_people);
        TextView txv = view.findViewById(R.id.txv_name);

        friend friend1 = friendList.get(position);
        String imgurl = friend1.getImageUrl();
        String textview = friend1.getF_name();

        Picasso.with(context).load(imgurl).placeholder(R.drawable.profile).into(img);
        txv.setText(textview);

        container.addView(view,position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.OnClick(position);
                }
            }
        });

        return view;
    }

    public interface CallBack{
        void OnClick(int position);
    }

    private CallBack callBack;
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }
}