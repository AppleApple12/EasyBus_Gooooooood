package com.example.easybus;
/*注意事項影片*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class Page7Activity extends AppCompatActivity {
    String getmail;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page7);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");

        requestQueue = Volley.newRequestQueue(this);
        //浮動按鈕撥打給緊急聯絡人
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });

        ImageView back = (ImageView)findViewById(R.id.backicon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page7Activity.this,Page3Activity.class);
                startActivity(back);
            }
        });
        //學習篇123
        ImageButton ib1 = (ImageButton)findViewById(R.id.button);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page701Activity.class);
                startActivity(it1);
            }
        });
        //禮儀篇
        ImageButton ib2 = (ImageButton)findViewById(R.id.button2);
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page702Activity.class);
                startActivity(it1);
            }
        });
        //安全篇
        ImageButton ib3 = (ImageButton)findViewById(R.id.button3);
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page703Activity.class);
                startActivity(it1);
            }
        });
        //求救篇
        ImageButton ib4 = (ImageButton)findViewById(R.id.button4);
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page7Activity.this,Page704Activity.class);
                startActivity(it1);
            }
        });
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
            //Toast.makeText(Page7Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page7Activity.this,
                    "請重撥!", Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(Page7Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Page7Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
