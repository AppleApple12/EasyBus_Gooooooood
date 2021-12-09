package com.example.easybus;
/*照顧者主頁*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Page4Activity extends AppCompatActivity {
    String getmail,fullname,image;
    TextView Name;
    Dialog dialog;
    Button clickme;
    RequestQueue requestQueue;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        requestQueue = Volley.newRequestQueue(this);
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");

        img = findViewById(R.id.imageView14);
        Name = findViewById(R.id.textView4);

        dialog = new Dialog(Page4Activity.this);
        dialog.setContentView(R.layout.nofriend_dialog);
        //刪除dialog方方的背景
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        clickme=dialog.findViewById(R.id.button10);
        new Page4Activity.fetchDatapage4().execute();
        //跳頁到定位查詢
        ImageView btn1 = (ImageView) findViewById(R.id.img_bg2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page4Activity.this,Page901Activity.class);
                //it1.putExtra("email",getmail);
                startActivity(it1);


            }
        });
        //跳頁到歷史足跡
        ImageView btn2 = (ImageView) findViewById(R.id.img_bg3);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL =Urls.url1+"/LoginRegister/my_contact.php?email="+getmail;
                //Request.Method.GET,URL,null我自己加的
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,URL,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray array) {
                                if (array.length()==0){
                                    dialog.show();
                                    clickme.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Page4Activity.this,qrcode_page.class);
                                            // intent.putExtra("email",getmail);
                                            startActivity(intent);
                                        }
                                    });
                                }else{
                                    Intent it2 = new Intent(Page4Activity.this,Page10Activity.class);
                                    //it2.putExtra("email",getmail);
                                    startActivity(it2);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page4Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue2 = Volley.newRequestQueue(Page4Activity.this);
                requestQueue2.add(request);
            }
        });
        //跳頁到我的帳戶
        ImageView btn3 = (ImageView) findViewById(R.id.img_bg4);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getmail != "") {
                    Intent it4 = new Intent(Page4Activity.this, Page8Activity_caregiver.class);
                    startActivity(it4);
                }else{
                    Intent it = new Intent(Page4Activity.this,Login3.class);
                    startActivity(it);
                }
            }
        });

    }
    public class fetchDatapage4 extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected void onPostExecute(Void aVoid) {
            super.onPreExecute();
            readUser();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    public void readUser(){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            fullname = response.getString("fullname");
                            Name.setText(fullname);
                            image= response.getString("image");
                            ImageRetriveWithPicasso(image);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(Page3Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Page3Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }
    public void ImageRetriveWithPicasso(String image) {
        String imgurl = Urls.url1+"/LoginRegister/images/"+image;
        Picasso.with(this)

                .load(imgurl)
                .placeholder(R.drawable.profile)
                .fit()
                // .error(R.drawable.ic_error_black_24dp)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        // 圖片讀取完成
                        //Toast.makeText(Page4Activity.this, "圖片讀取成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        // 圖片讀取失敗
                        //Toast.makeText(Page4Activity.this, "圖片讀取失敗", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}