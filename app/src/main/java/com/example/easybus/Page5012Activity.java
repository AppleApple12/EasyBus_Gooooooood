package com.example.easybus;
/*新增路線 加常用*/
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Page5012Activity extends AppCompatActivity {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAR3ZSrF2IrlUPdjjAIlXNRaMEJU-wN3CI";
    String url,urlOrigin,urlDestination,title2,getmail;
    ImageView mBack,mAdd;
    RecyclerView recyclerView;
    BusInfoAdaptor adaptor;
    ArrayList<BusInfo> businfos;
    RequestQueue requestQueue,requestQueue1,requestQueue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5012);
        mBack=findViewById(R.id.backicon);
        mAdd=findViewById(R.id.addIcon);
        recyclerView=findViewById(R.id.page611Info);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //取值(origin,destination)
        Bundle bundle = getIntent().getExtras();
        urlOrigin= bundle.getString("origin2");
        urlDestination= bundle.getString("destination2");

        url=DIRECTION_URL_API+"origin="+urlOrigin+"&destination="+urlDestination+"&mode=transit&transit_mode=bus&language=zh-TW&key="+GOOGLE_API_KEY;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor=new BusInfoAdaptor();
        recyclerView.setAdapter(adaptor);
        businfos=new ArrayList<>();

        //getData();
        new getData().execute();
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //實例化mPopupWindow
                popupWindow popupWin=new popupWindow(Page5012Activity.this);
                View view= LayoutInflater.from(Page5012Activity.this).inflate(R.layout.page5012popupwindow,null);
                //設計彈出框
                popupWin.showAtLocation(view,Gravity.CENTER,0,0);

            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestQueue2 = Volley.newRequestQueue(this);
        //浮動按鈕撥打給緊急聯絡人
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });
    }

    public class getData extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page5012Activity.this);
            pd.setCancelable(false);
            pd.setMessage("Downloading...Please wait!");
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            pd.dismiss();

            final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONArray ArrayRoutes = response.getJSONArray("routes");
                        JSONArray ArrayLegs =ArrayRoutes.getJSONObject(0).getJSONArray("legs");
                        JSONArray ArraySteps =ArrayLegs.getJSONObject(0).getJSONArray("steps");

                        for(int i=0;i<ArraySteps.length();i++){
                            JSONObject ObjSteps=ArraySteps.getJSONObject(i);
                            String travelMode=ObjSteps.getString("travel_mode");
                            BusInfo businfo=new BusInfo();

                            if (travelMode.equals("WALKING")) {
                                String Htmlinstructions=ObjSteps.getString("html_instructions");
                                businfo.setHtmlinstructions(Htmlinstructions.substring(3));
                                businfo.setShortname(null);
                                businfo.setPic(1);
                            }else{
                                businfo.setHtmlinstructions (ObjSteps.getJSONObject("transit_details").getJSONObject("arrival_stop").getString("name"));
                                businfo.setShortname(ObjSteps.getJSONObject("transit_details").getJSONObject("line").getString("short_name"));
                                businfo.setPic(0);
                            }
                            businfos.add(businfo);
                        }
                    }catch (JSONException e){
                        //Toast.makeText(Page5012Activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    adaptor.setData(businfos);
                    adaptor.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(Page5012Activity.this,"發生錯誤1111!",Toast.LENGTH_LONG).show();
                }
            });
            //RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    /*private void getData() { //抓公車JSON
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("載入中...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray ArrayRoutes = response.getJSONArray("routes");
                    JSONArray ArrayLegs =ArrayRoutes.getJSONObject(0).getJSONArray("legs");
                    JSONArray ArraySteps =ArrayLegs.getJSONObject(0).getJSONArray("steps");

                    for(int i=0;i<ArraySteps.length();i++){
                        JSONObject ObjSteps=ArraySteps.getJSONObject(i);
                        String travelMode=ObjSteps.getString("travel_mode");
                        BusInfo businfo=new BusInfo();

                        if (travelMode.equals("WALKING")) {
                            String Htmlinstructions=ObjSteps.getString("html_instructions");
                            businfo.setHtmlinstructions(Htmlinstructions.substring(3));
                            businfo.setShortname(null);
                            businfo.setPic(1);
                        }else{
                            businfo.setHtmlinstructions (ObjSteps.getJSONObject("transit_details").getJSONObject("arrival_stop").getString("name"));
                            businfo.setShortname(ObjSteps.getJSONObject("transit_details").getJSONObject("line").getString("short_name"));
                            businfo.setPic(0);
                        }
                        businfos.add(businfo);
                    }
                }catch (JSONException e){
                    //Toast.makeText(Page5012Activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                adaptor.setData(businfos);
                adaptor.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(Page5012Activity.this,"發生錯誤1111!",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    } */

    //標題popupwindow
    public class popupWindow extends PopupWindow implements View.OnClickListener {
        View view;
        EditText mTitle;
        TextView mNext;

        public popupWindow(Context mContext) {
            //動態頁面載入
            view = LayoutInflater.from(mContext).inflate(R.layout.page5012popupwindow, null);
            mTitle = view.findViewById(R.id.title);
            mNext = view.findViewById(R.id.nexttxt);
            //下一步前檢查是否有輸入標題
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TitleRequest(); //檢查標題是否為空
                }
            });
            //設置PopupWindow的View
            this.setContentView(view);
            //設置PopupWindow彈出框的高
            this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            //設置PopupWindow彈出框的寬
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            //設置mPopupWindow彈出框可點擊
            this.setFocusable(true);
            //實例化一個ColorDrawable顏色為半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //設置PopupWindow彈出框的背景
            this.setBackgroundDrawable(dw);
            //設置PopupWindow點框外銷毀
            this.setOutsideTouchable(true);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    int top=view.findViewById(R.id.img).getTop();
                    int bottom=view.findViewById(R.id.img).getBottom();
                    int left=view.findViewById(R.id.img).getLeft();
                    int right=view.findViewById(R.id.img).getRight();
                    int y=(int)motionEvent.getY();
                    int x=(int)motionEvent.getX();
                    if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                        if (y<top || y>bottom || x<left || x>right)
                            dismiss();
                    }
                    return true;
                }
            });
        }
        @Override
        public void onClick(View V) {
        }

        //檢查標題是否為空(如不是:選擇圖片)
        public void TitleRequest(){
            title2=mTitle.getText().toString().trim();
            if (title2.isEmpty()){
                Toast.makeText(Page5012Activity.this, "請輸入標題", Toast.LENGTH_LONG).show();
                return;
            }else{  //選擇圖片popupwindow
                //銷毀第一個popupwindow

                dismiss();
                //實例化PopupPhoto
                popupSelPhoto popupPhoto=new popupSelPhoto(Page5012Activity.this);
                View v=LayoutInflater.from(Page5012Activity.this).inflate(R.layout.page5012popupphoto,null);
                //設計彈出框
                popupPhoto.showAtLocation(v,Gravity.CENTER,0,0);
                //title2=popupPhoto.title;
                popupPhoto.title=title2;
            }
        }
    }

    //標題popupSelPhoto
    public class popupSelPhoto extends PopupWindow implements View.OnClickListener{
        View v;
        ImageView mWork,mHome,mPlay,mShopping,mSaveImg;
        int choose=0;
        String title,img = "";
        public popupSelPhoto(Context mContext2) {
            //動態頁面載入
            v = LayoutInflater.from(mContext2).inflate(R.layout.page5012popupphoto, null);

            mWork=v.findViewById(R.id.work);
            mHome=v.findViewById(R.id.home);
            mPlay=v.findViewById(R.id.play);
            mShopping=v.findViewById(R.id.shopping);
            mSaveImg=v.findViewById(R.id.saveImg);

            //設置PopupWindow的View
            this.setContentView(v);
            //設置PopupWindow彈出框的高
            this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            //設置PopupWindow彈出框的寬
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            //設置mPopupWindow彈出框可點擊
            this.setFocusable(true);
            //實例化一個ColorDrawable顏色為半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //設置PopupWindow彈出框的背景
            this.setBackgroundDrawable(dw);
            //設置PopupWindow點框外銷毀
            this.setOutsideTouchable(true);
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int height=v.findViewById(R.id.selphotobg).getHeight();
                    int y=(int)motionEvent.getY();
                    if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                        if (y<height){
                            dismiss();
                        }
                    }
                    return true;
                }
            });

            mWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose=1;
                    if (choose==1){
                        mWork.setImageResource(R.drawable.working1);
                        mHome.setImageResource(R.drawable.home);
                        mPlay.setImageResource(R.drawable.play);
                        mShopping.setImageResource(R.drawable.shopping);
                        img = "work";
                    }
                }
            });

            mHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose=2;
                    if (choose==2){
                        mHome.setImageResource(R.drawable.home1);
                        mWork.setImageResource(R.drawable.working);
                        mPlay.setImageResource(R.drawable.play);
                        mShopping.setImageResource(R.drawable.shopping);
                        img = "home";
                    }
                }
            });

            mPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose=3;
                    if (choose==3){
                        mPlay.setImageResource(R.drawable.play1);
                        mWork.setImageResource(R.drawable.working);
                        mHome.setImageResource(R.drawable.home);
                        mShopping.setImageResource(R.drawable.shopping);
                        img = "play";
                    }
                }
            });

            mShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose=4;
                    if (choose==4){
                        mShopping.setImageResource(R.drawable.shopping1);
                        mWork.setImageResource(R.drawable.working);
                        mHome.setImageResource(R.drawable.home);
                        mPlay.setImageResource(R.drawable.play);
                        img = "shopping";
                    }
                }
            });

            mSaveImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose+=1;
                    if (choose>=2 && choose<=5){ //有選擇照片
                        System.out.println(urlDestination);
                        System.out.println("title : "+title);
                        System.out.println(urlOrigin);
                        dismiss();
                        //將資訊存入資料庫
                        savebusinfo(getmail,title,urlOrigin,urlDestination,img);
                        System.out.println(img);
                        Toast.makeText(Page5012Activity.this,"路線儲存成功！",Toast.LENGTH_LONG).show();
                    }else{ //沒有選照片
                        Toast.makeText(Page5012Activity.this,"請選擇圖片！",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
        }
        public void savebusinfo(final String email,final String routename,final String origin,final String destination,final String img) {
            String URL = Urls.url1+"/LoginRegister/save_businfo.php";
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("Success")){
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Page5012Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("email",email);
                    parms.put("routename",routename);
                    parms.put("origin",origin);
                    parms.put("destination",destination);
                    parms.put("image",img);
                    return parms;
                }
            };
            requestQueue1.add(stringRequest);
        }
    }

    @SuppressLint("LongLogTag")
    protected void makeCall(final String phone) {
        //Snackbar.make(v,"打電話給緊急連絡人",Snackbar.LENGTH_LONG).setAction("Action",null).show();
        Intent call = new Intent(Intent.ACTION_DIAL);
        Uri u = Uri.parse("tel:"+phone);
        call.setData(u);

        try {
            startActivity(call);
            finish();
            Log.i("Finished making a call...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(Page5012Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page5012Activity.this,"請重撥！", Toast.LENGTH_SHORT).show();
        }
        //startActivity(call        );
    }
    public void readUser(){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    String emergency_phone;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            emergency_phone = response.getString("emergency_contact");
                            makeCall(emergency_phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Page5012Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page5012Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue2.add(jsonObjectRequest);
    }
}