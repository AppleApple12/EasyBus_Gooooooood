package com.example.easybus;
/*注意事項問題4*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Page70401Activity extends AppCompatActivity {
    Dialog corrdialog,crossdialog;
    ImageView imga,imgb,imgc,nextbtn,backbtn;
    TextView correct_content,nexttxt,cross_content;
    RequestQueue requestQueue;
    String getmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page70401);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        imga = (ImageView)findViewById(R.id.imga);
        imgb = (ImageView)findViewById(R.id.imgb);
        imgc = (ImageView)findViewById(R.id.imgc);

        corrdialog = new Dialog(Page70401Activity.this);
        crossdialog = new Dialog(Page70401Activity.this);

        corrdialog.setContentView(R.layout.correct_dialog);
        crossdialog.setContentView(R.layout.cross_dialog);

        corrdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nextbtn = corrdialog.findViewById(R.id.nextbtn);
        correct_content = corrdialog.findViewById(R.id.correct_content);
        nexttxt = corrdialog.findViewById(R.id.nexttxt);


        crossdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        backbtn = crossdialog.findViewById(R.id.backbtn);
        cross_content = crossdialog.findViewById(R.id.cross_content);

        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        requestQueue = Volley.newRequestQueue(this);
        //浮動按鈕撥打給緊急聯絡人
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
            }
        });


        ImageView mBack = (ImageView)findViewById(R.id.view);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Page70401Activity.this,Page7Activity.class);
                startActivity(back);
            }
        });

        imga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crossdialog.show();
                cross_content.setText("遭受到欺負\n要勇於保護自己");
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crossdialog.dismiss();
                    }
                });

            }
        });
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corrdialog.show();
                correct_content.setText("真棒!\n被欺負時\n就應該要好好保護自己");
                nexttxt.setText("恭喜你完成挑戰");
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next = new Intent(Page70401Activity.this,Page7Activity.class);
                        startActivity(next);
                    }
                });
            }
        });
        imgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corrdialog.show();
                correct_content.setText("真棒!\n答對了!");
                nexttxt.setText("恭喜你完成挑戰");
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next = new Intent(Page70401Activity.this,Page7Activity.class);
                        startActivity(next);
                    }
                });
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
            //Toast.makeText(Page5012Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page70401Activity.this,"請重撥！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Page70401Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page70401Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}