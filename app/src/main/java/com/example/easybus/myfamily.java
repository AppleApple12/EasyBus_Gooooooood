package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class myfamily extends AppCompatActivity {
    ImageView personalbtn,backBtn;
    TextView mEnteredName;
    RequestQueue requestQueue;
    String email,getmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfamily);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        personalbtn=findViewById(R.id.personal);
        mEnteredName = findViewById(R.id.EnteredName);
        backBtn=findViewById(R.id.backicon);

        getmail=mail();

        requestQueue = Volley.newRequestQueue(this);
        readUser();
        //返回健(回需求者選單)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Page3Activity.class));
            }
        });
        //基本資料
        personalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myfamily.this,Page8Activity.class);
                intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });
    }

    private void readUser(){
        String URL ="https://0065a21d3bfa.ngrok.io/LoginRegister/fetch.php?email="+getmail;
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
                            mEnteredName.setText(fullname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(myfamily.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myfamily.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public String mail(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            email=extras.getString("email");
        }
        return email;
    }
}