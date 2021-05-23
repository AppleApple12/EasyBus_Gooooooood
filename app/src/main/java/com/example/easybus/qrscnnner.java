package com.example.easybus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class qrscnnner extends AppCompatActivity {
    String getmail,name,tmp;
    RequestQueue requestQueue;
    ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscnnner);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        backbtn = findViewById(R.id.back);
        Toast.makeText(qrscnnner.this, tmp, Toast.LENGTH_SHORT).show();
        Toast.makeText(qrscnnner.this, getmail, Toast.LENGTH_SHORT).show();
        //返回健(回基本資料)
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qrscnnner.this,Page8Activity.class);
                intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        //Initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(qrscnnner.this);
        //Set prompt text
        intentIntegrator.setPrompt("請將QRcode對準");
        //Set beep
        intentIntegrator.setBeepEnabled(true);
        //Locked orientation
        intentIntegrator.setOrientationLocked(true);
        //Set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //Initiate scan
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
        if(intentResult.getContents()!=null){
            //when reuslt content not null
            //Initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    qrscnnner.this
            );

            //builder.setTitle();
            getmail = intentResult.getContents();
            readUser();
            builder.setMessage("是否加"+tmp+"為好友");
            builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }else{
            Toast.makeText(getApplicationContext(), "掃描失敗", Toast.LENGTH_SHORT).show();
        }
    }

    //抓取使用者基本資料
    private void readUser(){
        String URL ="http://192.168.0.132/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String fullname,phone;
                        try {
                            fullname = response.getString("fullname");
                            name = fullname;
                            tmp=name;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(qrscnnner.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(qrscnnner.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}