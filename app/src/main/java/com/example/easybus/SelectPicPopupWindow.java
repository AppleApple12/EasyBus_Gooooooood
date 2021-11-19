package com.example.easybus;
/*我的帳戶照片新增*/
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class SelectPicPopupWindow extends PopupWindow {
    private Button mTakePhotoBtn,mSelectPhotoBtn,mCancelBtn;
    private View mMenuWindow;

    public SelectPicPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        //動態頁面載入
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuWindow=inflater.inflate(R.layout.activity_select_pic_popup_window,null);

        mTakePhotoBtn=mMenuWindow.findViewById(R.id.takePhotoBtn);
        mSelectPhotoBtn=mMenuWindow.findViewById(R.id.SelectPhotoBtn);
        mCancelBtn=mMenuWindow.findViewById(R.id.cancelBtn);

        //取消按鈕
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //銷毀
                dismiss();
            }
        });

        //設置Button監聽
        mTakePhotoBtn.setOnClickListener(itemsOnClick);
        mSelectPhotoBtn.setOnClickListener(itemsOnClick);

        //設置SelectPicPopupWindow的View
        this.setContentView(mMenuWindow);
        //設置SelectPicPopupWindow彈出框的寬
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        //設置SelectPicPopupWindow彈出框的高
        this.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        //設置SelectPicPopupWindow彈出框可點擊
        this.setFocusable(true);
        //設置SelectPicPopupWindow彈出框的動畫效果
        this.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        //實例化一個ColorDrawable顏色為半透明
        ColorDrawable dw=new ColorDrawable(0xb0000000);
        //設置SelectPicPopupWindow彈出框的背景
        this.setBackgroundDrawable(dw);

        //mMenuView添加OnTouchListener監聽判斷獲取觸屏如果在選擇框外則銷毀彈出框
        mMenuWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuWindow.findViewById(R.id.pop_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
//蕙璟版本