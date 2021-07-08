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

public class my_contact extends AppCompatActivity {
    String email,getmail;
    TextView mEnteredName;
    ImageView backBtn;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        backBtn=findViewById(R.id.backicon);
        mEnteredName = findViewById(R.id.EnteredName);
        //隱藏title bar///
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        requestQueue = Volley.newRequestQueue(this);
        getmail=mail();
        readUser();
        //返回我的資料
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(my_contact.this,Page8Activity.class);
                intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });
    }
    //抓取使用者基本資料
    private void readUser(){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String fullname;
                        try {
                            fullname = response.getString("fullname");
                            mEnteredName.setText(fullname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(my_contact.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(my_contact.this, error.toString(), Toast.LENGTH_SHORT).show();
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