package com.example.easybus;
/*照顧者主頁*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Page4Activity extends AppCompatActivity {
    String email,getmail;
    Dialog dialog;
    Button clickme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");

        dialog = new Dialog(Page4Activity.this);
        dialog.setContentView(R.layout.nofriend_dialog);
        //刪除dialog方方的背景
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        clickme=dialog.findViewById(R.id.button10);

        //跳頁到定位查詢
        Button btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page4Activity.this,Page901Activity.class);
                //it1.putExtra("email",getmail);
                startActivity(it1);


            }
        });
        //跳頁到歷史足跡
        Button btn2 = (Button)findViewById(R.id.btn2);
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
        Button btn3 = (Button)findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getmail != "") {
                    Intent it4 = new Intent(Page4Activity.this, Page8Activity_caregiver.class);
                    //it4.putExtra("email", email2);
                    startActivity(it4);
                }else{
                    Intent it = new Intent(Page4Activity.this,Login3.class);
                    startActivity(it);
                }
            }
        });

    }
    /*public String mail(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            email=extras.getString("email");
        }
        return email;
    }*/
}