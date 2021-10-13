package com.example.easybus;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calendar2 extends View {
    private  String TAG = "CustomerCalendar";
    int nextlastLineNum=0;//
    private Date month,year;//當前月份
    private boolean isCurrentMonth;//展示的月份是否是當前月
    private int currentDay,selectDay,lastSelectDay;//當前日期.選中日期.上一次選中的日期

    private int dayOfMonth; //月分天數
    private int firstIndex;//當月第一天位置索引
    private int todayWeekIndex;//今天星期幾
    private int firstLineNum,lastLineNum;//第一行.最後一行能展示多少日期
    private int lineNum;    //日期行數
    private  String[] WEEK_STR = new String[]{"日","一","二","三","四","五","六"};


    private int mBgMonth,mBgWeek,mBgDay,mBgpre;//各部分背景
    //標題顏色.大小
    private int mTextColorMonth,mTextColorYear;
    private float mTextSizeMonth,mTextSizeYear;
    private int mMonthRowL,mMonthRowR;
    private float mMonthRowSpac;
    private float mMonthSpac,mYearSpac;
    //星期的顏色.大小
    private int mTextColorWeek,mSelectWeekTextColor;
    private float mTextSizeWeek;
    //日期文本的顏色.大小
    private int mTextColorDay;
    private float mTextSizeDay;
    //任務次數文本顏色.大小
    private int mTextColorPreFinish,mTextColorPreUnFinish,mTextColorPreNull;
    private float mTextSizePre;
    //選中文本的顏色
    private int mSelectTextColor;
    //選中背景
    private int mSelectBg,mCurrentBg;
    private float mSelectRadius,mCurrentBgStrokeWidth;
    private float[] mCurrentBgDahPath;

    //行間距
    private float mLineSpac;
    //字體上下間距
    private float mTextSpac;

    private Paint mPaint;
    private Paint bgPaint;

    private float titleHeight,weekHeight,dayHeight,preHeight,oneHeight,rectcolumnWidth;
    private int columnWidth;//每列寬度

    public String y,m;
    Date day;
    String[] Mtoday,Ytoday;
    String cDateStr,cYearStr,cDayStr; //默認當前 年、月 小高寫的年月日

    Calendar calendar2 = Calendar.getInstance();
    Date date = calendar2.getTime();
    public calendar2(Context context){
        this(context,null);
    }
    public calendar2(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public calendar2(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.calendar2,defStyleAttr,0);

        mBgMonth = a.getColor(R.styleable.calendar2_mBgMonth2, Color.TRANSPARENT);
        mBgWeek = a.getColor(R.styleable.calendar2_mBgWeek2,Color.TRANSPARENT);
        mBgDay = a.getColor(R.styleable.calendar2_mBgDay2,Color.TRANSPARENT);
        //mBgpre = a.getColor(R.styleable.calendar2_mBgPre,Color.TRANSPARENT);

        mMonthRowL = a.getResourceId(R.styleable.calendar2_mMonthRowL2,R.drawable.custom_calendar_row_left);
        mMonthRowR = a.getResourceId(R.styleable.calendar2_mMonthRowR2,R.drawable.custom_calendar_row_right);
        mMonthRowSpac = a.getDimension(R.styleable.calendar2_mMonthRowSpac2,50);
        mTextColorMonth = a.getColor(R.styleable.calendar2_mTextColorMonth2,Color.BLACK);
        mTextSizeMonth = a.getDimension(R.styleable.calendar2_mTextSizeMonth2,50);
        mMonthSpac = a.getDimension(R.styleable.calendar2_mMonthSpac2,20);

        mTextSizeYear = a.getDimension(R.styleable.calendar2_mTextSizeYear2,100);
        mTextColorYear = a.getColor(R.styleable.calendar2_mTextColorYear2,Color.BLACK);
        mYearSpac = a.getDimension(R.styleable.calendar2_mYearSpac2,20);

        mTextColorWeek = a.getColor(R.styleable.calendar2_mTextColorWeek2,Color.BLACK);
        mSelectWeekTextColor = a.getColor(R.styleable.calendar2_mSelectWeekTextColor2,Color.BLACK);

        mTextSizeWeek = a.getDimension(R.styleable.calendar2_mTextSizeWeek2,70);
        mTextColorDay = a.getColor(R.styleable.calendar2_mTextColorDay2,Color.GRAY);
        mTextSizeDay = a.getDimension(R.styleable.calendar2_mTextSizeDay2,70);
        //mTextColorPreFinish = a.getColor(R.styleable.calendar2_mTextColorPreFinish,Color.BLUE);
        //mTextColorPreUnFinish = a.getColor(R.styleable.calendar2_mTextColorPreUnFinish,Color.BLUE);
        //mTextColorPreNull = a.getColor(R.styleable.calendar2_mTextColorPreNull,Color.BLUE);
        //mTextSizePre = a.getDimension(R.styleable.calendar2_mTextSizePre,40);
        mSelectTextColor = a.getColor(R.styleable.calendar2_mSelectTextColor2,Color.YELLOW);
        mCurrentBg = a.getColor(R.styleable.calendar2_mCurrentBg2,Color.GRAY);

        try{
            int dashPathId = a.getResourceId(R.styleable.calendar_mCurrentBgDashPath,R.array.calendar_currentDay_bg_DashPath);
            int[] array = getResources().getIntArray(dashPathId);
            mCurrentBgDahPath = new float[array.length];
            for(int i  = 0;i<array.length;i++){
                mCurrentBgDahPath[i] = array[i];
            }
        }catch (Exception e){
            e.printStackTrace();
            mCurrentBgDahPath = new float[]{2,3,2,3};
        }

        mSelectBg = a.getColor(R.styleable.calendar2_mSelectBg2,Color.YELLOW);
        mSelectRadius = a.getDimension(R.styleable.calendar2_mSelectRadius2,20);
        mCurrentBgStrokeWidth = a.getDimension(R.styleable.calendar2_mCurrentBgStrokeWidth2,5);
        mLineSpac = a.getDimension(R.styleable.calendar2_mLineSpac2,20);
        mTextSpac = a.getDimension(R.styleable.calendar2_mTextSpac2,20);
        a.recycle();    //注意回收

        initcompute();
        currentDay = calendar2.get(calendar2.DAY_OF_MONTH);
        todayWeekIndex = calendar2.get(calendar2.DAY_OF_WEEK)-1;
    }
    //小高設置的年月日  小高超級棒
    /**day**/
    private String getDayStr(Date day){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(day);
    }
    private Date str2Day(String str){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void setDay(String Day){
        day = str2Day(Day);

        calendar2.setTime(day);
        String[] today = new String[3];
        today=Day.split("-");
        for(int i=0;i<today.length;i++){
            System.out.print(today[i]+"/");
        }
        System.out.println();
        System.out.println("day : "+day);//Date
        System.out.println("Day : "+Day);//String
    }
    //小高end
    /**設置年份**/
    private  void setYear(String Year){
        year = str2DateYear(Year);
        Ytoday=Year.split("-");
        //Calendar calendar2 = Calendar.getInstance();
        //calendar2.set(calendar2.YEAR,Integer.parseInt(Ytoday[0]));
        // calendar2.set(calendar2.MONTH,Integer.parseInt(Ytoday[1]));
        System.out.println("yyy : "+year.toString());

    }
    /**設置月份**/
    private  void setMonth(String Month){
        //設置的月份
        //Calendar calendar2 = Calendar.getInstance();
        month = str2Date(Month);
        Mtoday=Month.split("-");

        //mm=Integer.parseInt(today[1]);

        //calendar2.setTime(new Date());

        //currentDay = calendar2.get(calendar2.DAY_OF_MONTH);
        //todayWeekIndex = calendar2.get(calendar2.DAY_OF_WEEK)-1;

        Date cM = str2Date(getMonthStr(new Date()));

        //判斷是否為當月
        if(cM.getTime()== month.getTime()){
            isCurrentMonth = true;
            selectDay = currentDay;//當月默認選中當前日
        }else{
            isCurrentMonth = false;
            selectDay = 0;
        }
        System.out.println("selectDay : "+selectDay);
        Log.d(TAG,"設置月份:"+month+"   今天"+currentDay+"號,是否為當前月:"+isCurrentMonth);
        //calendar2.set(calendar2.YEAR,Integer.parseInt(Mtoday[0]));
        //calendar2.set(calendar2.MONTH,Integer.parseInt(Mtoday[1]));
        Mtoday[1]=Mtoday[1]+"月";
        calendar2.setTime(month);
        dayOfMonth = calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);//這個月總共有n天
        //第一行的1號是星期幾
        //日 一 二 三 四 五 六
        // 1 2 3  4  5  6  7
        // 0 1 2  3  4  5  6
        firstIndex = calendar2.get(Calendar.DAY_OF_WEEK)-1;
        lineNum = 1;
        //第一行能展示的天數
        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int shengyu = dayOfMonth-firstLineNum;
        while(shengyu>7){
            lineNum++;
            shengyu-=7;
        }
        if(shengyu>0){
            lineNum++;
            lastLineNum = shengyu;
        }
        Log.i(TAG,month+"一共有"+dayOfMonth+"天，第一天的索引是："+firstIndex+"   有"+lineNum+"行，第一行"+firstLineNum+"個，最後一行"+lastLineNum+"個");
    }
    /***計算相關常量，構造方法中適用*/
    private  void initcompute(){
        mPaint = new Paint();
        bgPaint = new Paint();
        mPaint.setAntiAlias(true);  //抗鋸齒
        bgPaint.setAntiAlias(true); //抗鋸齒

        map = new HashMap<>();
        //標題高度
        //mPaint.setTextSize(mTextSizeMonth);
        //mPaint.setTextSize(mTextSizeYear);
        //titleHeight = FontUtil.getFontHeight(mPaint)+2*mMonthRowSpac+2*mYearSpac;
        //星期高度
        mPaint.setTextSize(mTextSizeWeek);
        weekHeight = FontUtil.getFontHeight(mPaint);
        //日期高度
        mPaint.setTextSize(mTextSizeDay);
        dayHeight = FontUtil.getFontHeight(mPaint);
        /*次數字體高度
        mPaint.setTextSize(mTextSizePre);*/
        //每行高度 = 行間距 + 日期字體高度 + 字間距
        oneHeight = mLineSpac + dayHeight +mTextSpac;

        //默認當前月分
        cDateStr = getMonthStr(new Date());
        System.out.println("cDateStr : "+cDateStr);
        setMonth(cDateStr);
        //默認當前年分
        cYearStr = getYearStr(new Date());
        System.out.println("cYearStr : "+cYearStr);
        setYear(cYearStr);
        //小高寫的年月日
        cDayStr = getDayStr(new Date());
        System.out.println("cDayStr : "+cDayStr);
        setDay(cDayStr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        //寬度 = 填充父窗體
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  //獲取寬的尺寸
        columnWidth = widthSize/7;
       // System.out.println("columnWidth : "+columnWidth);
        //高度 = 標題高度+星期高度+日期高度*每行高度
        float height = titleHeight+weekHeight+(lineNum*oneHeight);
        //Log.v(TAG,"標題高度"+titleHeight+"星期高度："+weekHeight+" 每行高度："+oneHeight+"行數："+lineNum+" \n控建高度："+height);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumHeight(),widthMeasureSpec),(int)height);
    }
    @Override
    protected void onDraw(Canvas canvas){
        drawMonth(canvas);
        drawWeek(canvas);
        drawDayAndPre(canvas);
    }
    private  int rowLStart,rowRStart,rowWidth;
    /***繪製月份*/
    private void drawMonth(Canvas canvas){
        //背景
        bgPaint.setColor(Color.BLUE);
        RectF rect = new RectF(0,0,getWidth(),titleHeight);
        //canvas.drawRect(rect,bgPaint);
        canvas.drawRoundRect(rect,40,40,bgPaint);

        switch (Mtoday[1]){
            case "01月":
                m = "JAN";
                break;
            case "02月":
                m = "FEB";
                break;
            case "03月":
                m = "MAR";
                break;
            case "04月":
                m = "APR";
                break;
            case "05月":
                m = "MAY";
                break;
            case "06月":
                m = "JUN";
                break;
            case "07月":
                m = "JUL";
                break;
            case "08月":
                m = "AUG";
                break;
            case "09月":
                m = "SEP";
                break;
            case "10月":
                m = "OCT";
                break;
            case "11月":
                m = "NOV";
                break;
            case "12月":
                m = "DEC";
                break;
        }
        //canvas.drawText(m,getWidth()-FontUtil.getFontlength(mPaint,getMonthStr(month)),mMonthRowSpac+FontUtil.getFontLeading(mPaint),mPaint);
        canvas.drawText(m,getWidth()-FontUtil.getFontlength(mPaint,Mtoday[1]),275,mPaint);
        mPaint.setShadowLayer(5f,2,2,Color.GRAY);   //增加陰影*/


        //繪製左右箭頭
        float textLen = FontUtil.getFontlength(mPaint,Mtoday[1]);
        float textStart = (getWidth()-textLen)/2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),mMonthRowL);//獲取左箭頭圖片
        int h = bitmap.getHeight(); //箭頭的圖片高
        rowWidth = bitmap.getWidth();   //箭頭的圖片寬

        //float left,float top;
        rowLStart= (int)(textStart-2*mMonthRowSpac-rowWidth);
        canvas.drawBitmap(bitmap,0,titleHeight,new Paint());//設置左箭頭
        bitmap = BitmapFactory.decodeResource(getResources(), mMonthRowR);//獲取右箭頭圖片
        rowRStart = (int)(textStart+textLen)+200;
        canvas.drawBitmap(bitmap,rowRStart+mMonthSpac+70,titleHeight,new Paint());
    }
    /**繪製星期*/
    private  void drawWeek(Canvas canvas){
        //背景
        bgPaint.setColor(mBgWeek);
        //bgPaint.setColor(Color.RED);
        RectF rect = new RectF(40+rowWidth,titleHeight,rowRStart+mMonthSpac+40,titleHeight+weekHeight);

        //繪製星期：七天
        bgPaint.setTextSize(mTextSizeWeek);
        int total = 0;
        for(int i = 0;i<WEEK_STR.length;i++){
            total = total+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[i]);
        }
        float rectwidthSize = rect.width(); //獲取寬的尺寸
        rectcolumnWidth =(rectwidthSize-total)/7;
        canvas.drawRect(rect,bgPaint);
        //System.out.println("total/7 : "+(total/7));
        for(int i = 0;i<WEEK_STR.length;i++) {
            if (todayWeekIndex == i && isCurrentMonth) {
                //mPaint.setColor(mSelectWeekTextColor);
                bgPaint.setColor(mTextColorWeek);
            } else {
                bgPaint.setColor(mTextColorWeek);
            }
            int len = (int) FontUtil.getFontlength(bgPaint, WEEK_STR[i]);

            //int x = i*99;
            float x = i * rectcolumnWidth + (rectcolumnWidth - len) / 2;
        }
            /*System.out.println("len : "+len);
            System.out.println("x : "+x);
            System.out.println("70+rowWidth+x :"+(70+rowWidth+x));*/
        int lennnn=0;
        for(int i =0;i<WEEK_STR.length;i++) {
            if (i == 0)
                canvas.drawText(WEEK_STR[i], 80 + rowWidth, titleHeight + FontUtil.getFontLeading(bgPaint), bgPaint);
            else {
                lennnn = lennnn + (int) FontUtil.getFontlength(bgPaint, WEEK_STR[i - 1]);
                canvas.drawText(WEEK_STR[i], 80 + rowWidth + rectcolumnWidth*i + lennnn, titleHeight + FontUtil.getFontLeading(bgPaint), bgPaint);
            }
        }
            //canvas.drawText(WEEK_STR[i],70+rowWidth+x,titleHeight+FontUtil.getFontLeading(mPaint),mPaint);
           /* canvas.drawText(WEEK_STR[0],70+rowWidth,titleHeight+FontUtil.getFontLeading(bgPaint),bgPaint); //102 0
            canvas.drawText(WEEK_STR[1],70+rowWidth+rectcolumnWidth+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[0]),titleHeight+FontUtil.getFontLeading(bgPaint),bgPaint);//+len*(i-1)  102+
            canvas.drawText(WEEK_STR[2],70+rowWidth+rectcolumnWidth*2+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[1])+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[0]),titleHeight+FontUtil.getFontLeading(bgPaint),bgPaint);
            canvas.drawText(WEEK_STR[3],70+rowWidth+rectcolumnWidth*3+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[2])+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[1])+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[0]),titleHeight+FontUtil.getFontLeading(bgPaint),bgPaint);*/
            //canvas.drawText(WEEK_STR[3],70+rowWidth+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[2]),titleHeight+FontUtil.getFontLeading(mPaint),mPaint);
            //canvas.drawText(WEEK_STR[4],70+rowWidth+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[3]),titleHeight+FontUtil.getFontLeading(mPaint),mPaint);
            //canvas.drawText(WEEK_STR[5],70+rowWidth+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[4]),titleHeight+FontUtil.getFontLeading(mPaint),mPaint);
            //System.out.println("x() : "+x); //17,132,262,501,634,747,
           // System.out.println("y() : "+titleHeight+FontUtil.getFontLeading(mPaint)); //0.041.400146


    }
    /**繪製日期.次數*/
    private  void drawDayAndPre(Canvas canvas){
        //某行開始繪製的Y座標，第一行開始的座標為標題高度+星期部分高度
        float top = titleHeight+weekHeight;
        //行
        // Log.i(TAG,getMonthStr(month)+"一共有"+dayOfMonth+"天，第一天的索引是："+firstIndex+"   有"+lineNum+"行，第一行"+firstLineNum+"個，最後一行"+lastLineNum+"個");
        for(int line = 0;line<lineNum;line++){
            if(line==0){
                //第一行
                drawDayAndPre(canvas, top, firstLineNum,0,firstIndex);
            }else if(line==lineNum-1){
                top+=oneHeight;
                drawDayAndPre(canvas,top,lastLineNum,firstLineNum+(line-1)*7,0);
            }else{
                //滿行
                top+=oneHeight;
                drawDayAndPre(canvas,top,7,firstLineNum+(line-1)*7,0);
            }
        }

    }

    /**\
     * 繪製某一行的日期
     * @param canvas
     * @param top 頂點座標
     * @param count 此行需要繪製的日期數量(不一定都是7天)
     * @param overDay 已經繪製過的日期，從overDay+1開始繪製
     * @param startIndex 此行第一個日期的星期索引
     */
    private void drawDayAndPre(Canvas canvas,float top,int count,int overDay,int startIndex){
        //Log.e(TAG,"總共"+dayOfMonth+"天    有"+lineNum+"行"+"    已經畫了"+overDay+"天，下面繪製："+count+"天");
        //背景
        float topPre = top+mLineSpac+dayHeight;
        bgPaint.setColor(mBgDay);
        //bgPaint.setColor(Color.BLUE);
        RectF rect = new RectF(70 + rowWidth,top,rowRStart+mMonthSpac+70,topPre);
        canvas.drawRect(rect,bgPaint);

        int total = 0;
        for(int i = 0;i<WEEK_STR.length;i++){ total = total+(int)FontUtil.getFontlength(bgPaint,WEEK_STR[i]); }
        float rectwidthSize = rect.width(); //獲取寬的尺寸
        rectcolumnWidth =rectwidthSize/7;

        bgPaint.setColor(mBgpre);
        rect = new RectF(70 + rowWidth,topPre,rowRStart+mMonthSpac+70,topPre+mTextSpac+dayHeight);
        canvas.drawRect(rect,bgPaint);

        mPaint.setTextSize(mTextSizeDay);
        float dayTextLeading = FontUtil.getFontLeading(mPaint);
        mPaint.setTextSize(mTextSizePre);
        float preTextLeading = FontUtil.getFontLeading(mPaint);
        //Log.v(TAG,"當前日期："+currentDay+"  選擇日期："+selectDay+"  是否為當前月："+isCurrentMonth);
        for(int i = 0;i<count;i++){
            //int left = (startIndex+i)*columnWidth;
            int left = (startIndex+i)*(int)rectcolumnWidth;
            int day = (overDay+i+1);

            mPaint.setTextSize(mTextSizeDay);

            //如果是當前月，當天日期需要做處理
            if(isCurrentMonth && currentDay==day){
                mPaint.setColor(mTextColorDay);
                bgPaint.setColor(mCurrentBg);
                bgPaint.setStyle(Paint.Style.STROKE);   //空心
                PathEffect effect = new DashPathEffect(mCurrentBgDahPath,1);
                bgPaint.setPathEffect(effect);  //設置畫筆曲線間隔
                bgPaint.setStrokeWidth(mCurrentBgStrokeWidth);  //畫筆寬度
                //繪製空心圓背景
                //canvas.drawCircle(left+columnWidth/2,top+mLineSpac+dayHeight/2,mSelectRadius-mCurrentBgStrokeWidth,bgPaint);
                canvas.drawCircle(70+ rowWidth+left+(int)rectcolumnWidth/2,top+mLineSpac+dayHeight/2,mSelectRadius-mCurrentBgStrokeWidth,bgPaint);
            }
            //繪製完後將畫筆還原，避免髒筆
            bgPaint.setPathEffect(null);
            bgPaint.setStrokeWidth(0);
            bgPaint.setStyle(Paint.Style.FILL);
            //選中的日期，如果是本月，選中日期正好是當天日期，下面的背景會覆蓋上面繪製的虛線背景
            if(selectDay == day){
                //選中的日期字體白色，紅色背景
                mPaint.setColor(mSelectTextColor);
                bgPaint.setColor(mSelectBg);
                //繪製橙色圓背景，參數一是中心點的x軸，參數二是中心點的y軸，參數三是半徑，參數四是Paint對象;
                //canvas.drawCircle(left+columnWidth/2,top+mLineSpac+dayHeight/2,mSelectRadius,bgPaint);
                canvas.drawCircle(70+rowWidth+left+(int)rectcolumnWidth/2,top+mLineSpac+dayHeight/2,mSelectRadius,bgPaint);
                System.out.println("紅x : "+70+rowWidth+left+(int)rectcolumnWidth/2);
                System.out.println("紅y : "+top+mLineSpac+dayHeight/2);
            }else{
                mPaint.setColor(mTextColorDay);
            }

            int len = (int)FontUtil.getFontlength(mPaint,day+"");
            //int x = left+(columnWidth-len)/2;
            int x = left+((int)rectcolumnWidth-len)/2;
            //int x =left-37;
            canvas.drawText(day+"",70+ rowWidth+x,top+mLineSpac+dayTextLeading,mPaint);

        }
    }

    /**月份標題**/
    public String getMonthStr(Date month){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return  df.format(month);
    }
    //將str 轉換成Date
    private Date str2Date(String str){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**年份標題**/
    private String getYearStr(Date year){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(year);
    }
    private Date str2DateYear(String str){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**********事件處理**********/
    //焦點座標
    private PointF focusPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction() & MotionEvent.ACTION_MASK;   //ACTION_MASK處理多點觸控
        switch (action){
            case MotionEvent.ACTION_DOWN | MotionEvent.ACTION_MOVE:
                focusPoint.set(event.getX(),event.getY());
                touchFocusMove(focusPoint,false);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                focusPoint.set(event.getX(),event.getY());
                touchFocusMove(focusPoint,true);
                break;
        }
        return true;
    }
    /**焦點滑動**/
    public void touchFocusMove(final PointF point,boolean eventEnd){
        Log.e(TAG,"點擊座標：("+point.x+" ，"+point.y+")，事件是否結束："+eventEnd);
        /**星期只有在事件結束後才響應**/
        if (point.y<=weekHeight){
            //事件在星期的部分
            if (eventEnd && listener!=null){
                if(point.x>=0 && point.x<(40+rowWidth)){
                    Log.w(TAG,"點擊左箭頭");
                    listener.onLeftRowClick();
                }else if (point.x>rowRStart+mMonthSpac+70 && point.x<(rowRStart+mMonthRowSpac+rowWidth)){
                    Log.w(TAG,"點擊右箭頭");
                    listener.onRightRowClick();
                }
            }
        }else{
            /**日期部分按下和滑動時重繪**/
            touchDay(point,eventEnd);
        }
    }
    //控制事件是否響應
    private boolean responseWhenEnd = false;
    /**事件點在日期區域範圍內**/
    private void touchDay(final PointF point,boolean eventEnd){
        //根據y座標找到焦點行
        boolean availabitlity = false;  //事件是否有效
        //日期部分
        float top = titleHeight+weekHeight+oneHeight;
        int focusLine = 1;
        while (focusLine<=lineNum){
            if(top>=point.y){
                availabitlity = true;
                break;
            }
            top+=oneHeight;
            focusLine++;
        }
        if(availabitlity){
            //根據x座標找到具體的焦點日期(int)rectcolumnWidth
            /*int xIndex = (int)point.x/(int)rectcolumnWidth;
            if((point.x/(int)rectcolumnWidth-xIndex)>0)*/
            int avg =(columnWidth+(int)rectcolumnWidth);
            int xIndex = (int)point.x/columnWidth;
            if((point.x/columnWidth-xIndex)>0)
            {
                xIndex+=1;
            }
            //Log.e(TAG,"列寬："+columnWidth+"   x座標餘數："+(point.x/columnWidth));
            if (xIndex<=0){
                xIndex = 1;
            }
            if (xIndex>7){
                xIndex = 7;
            }
            //Log.e(TAG,"事件在日期部分，第"+focusLine+"/"+lineNum+"行，"+xIndex+"列");
            if (focusLine==1){
                //第一行
                if (xIndex<=firstIndex){
                    Log.e(TAG,"點到開始空位了");
                    setSelectedDay(selectDay,true);
                }else{
                    setSelectedDay(xIndex-firstIndex,eventEnd);
                }
            }else if (focusLine==lineNum){
                //最後一行
                if(xIndex>lastLineNum){
                    Log.e(TAG,"點到結尾空位");
                    setSelectedDay(selectDay,true);
                }else {
                    setSelectedDay(firstLineNum+(focusLine-2)*7+xIndex,eventEnd);
                }
            }else{
                setSelectedDay(firstLineNum+(focusLine-2)*7+xIndex,eventEnd);
            }
        }else{
            //超出日期區域後，視為事件結束，響應最後一個選擇日期的回調
            setSelectedDay(selectDay,true);
        }
    }
    /**設置選中的日期**/
    private void setSelectedDay(int day,boolean eventEnd){
        Log.w(TAG,"選中："+day+"   事件是否結束"+eventEnd);
        selectDay = day;
        invalidate();
        if(listener!=null && eventEnd && responseWhenEnd && lastSelectDay!=selectDay){
            lastSelectDay = selectDay;
            listener.onDayClick(selectDay,Mtoday[1]+getMonthStr(month)+selectDay,map.get(selectDay));
        }
        responseWhenEnd = !eventEnd;

    }

    @Override
    public void invalidate(){
        requestLayout();
        super.invalidate();
    }


    /************接口API**********/
    private Map<Integer,page1001Activity.DayFinish2> map;
    public void setRenwu(String year,String month, List<page1001Activity.DayFinish2> list){
        setYear(year);
        setMonth(month);
        if (list!=null && list.size()>0){
            map.clear();
            for(page1001Activity.DayFinish2 finish:list)
                map.put(finish.day,finish);
        }
        invalidate();
    }

    public void setRenwu(List<page1001Activity.DayFinish2> list){
        if(list!=null && list.size()>0){
            map.clear();    //重置map
            for (page1001Activity.DayFinish2 finish:list)
                map.put(finish.day,finish);
        }
        invalidate();
    }

    /**月份增減**/
    public void monthChange(int change){
        // Calendar calendar2 = Calendar.getInstance();
        //calendar2.setTime(month);
        calendar2.add(Calendar.MONTH,change);
        //setDay(getDayStr(calendar2.getTime()));
        setMonth(getMonthStr(calendar2.getTime()));
        map.clear();
        invalidate();
    }
    /**年份增減**/
    public void yearChange(int change){
        //Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(year);
        calendar2.add(Calendar.YEAR,change);
        //setDay(getDayStr(calendar2.getTime()));
        setYear(getYearStr(calendar2.getTime()));
        System.out.println("getYearStr(calendar2.getTime()) : "+getYearStr(calendar2.getTime()));
        map.clear();
        invalidate();
    }

    private onClickListener listener;
    public void setOnClickListen(onClickListener listener){
        this.listener = listener;
    }
    interface onClickListener{
        public abstract void onLeftRowClick();
        public abstract void onRightRowClick();
        public abstract void onTitleClick(String monStr,Date month);
        public abstract void onWeekClick(int weekIndex,String weekStr);
        public abstract void onDayClick(int day,String dayStr,page1001Activity.DayFinish2 finish);
    }
}
