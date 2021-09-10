package com.example.easybus;

import android.graphics.Paint;

public class FontUtil {
    public static float getFontlength(Paint paint,String str){
        return paint.measureText(str);
        //回傳指定筆和指定字串的長度
    }
    public static float getFontHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent-fm.ascent;
        //回傳指定筆的文字高度
    }
    public static float getFontLeading(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading-fm.ascent;
    }
    //念婷
}
