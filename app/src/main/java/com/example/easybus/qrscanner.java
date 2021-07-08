package com.example.easybus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class qrscanner extends AppCompatActivity {
    String friendmail,email,getmail;
    RequestQueue requestQueue;
    Dialog dialog;
    Button btnok,btncancle;
    TextView maddfriend,friendname;
    //private PopupWindow popupWindow = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getmail=mail();
        requestQueue = Volley.newRequestQueue(this);
        //Initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(qrscanner.this);
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
        dialog = new Dialog(qrscanner.this);

        dialog.setContentView(R.layout.activity_scan_dialog);
        //刪除dialog方方的背景
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnok = dialog.findViewById(R.id.okok);
        btncancle = dialog.findViewById(R.id.canclecancle);
        maddfriend = dialog.findViewById(R.id.addfriend);

    }
    public void readUser(String getmail){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    String fullname;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fullname = response.getString("fullname");
                            //dialog內的TextView
                            maddfriend.setText("是否加入\n"+fullname+"\n為聯絡人");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(qrscanner.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(qrscanner.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
        if(intentResult.getContents()!=null){
            //when reuslt content not null
            friendmail = intentResult.getContents();
            readUser(friendmail);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(qrscanner.this,Page8Activity.class);
                    intent.putExtra("email",getmail);
                    startActivity(intent);
                }
            });
            btncancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(qrscanner.this,Page8Activity.class);
                    intent.putExtra("email",getmail);
                    startActivity(intent);
                }
            });
            dialog.show();
        }else{
            Toast.makeText(getApplicationContext(), "掃描失敗", Toast.LENGTH_SHORT).show();
        }
    }

    public String mail(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            email=extras.getString("email");
        }
        return email;
    }

}