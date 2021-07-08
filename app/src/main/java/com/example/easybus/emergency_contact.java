package com.example.easybus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class emergency_contact extends AppCompatActivity {
    TextView back;
    String email, getmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        getmail = mail();
        back = findViewById(R.id.back);
        //隱藏title bar///
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(emergency_contact.this, Page8Activity.class);
                it1.putExtra("email",getmail);
                startActivity(it1);
            }
        });
    }

    public String mail() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
        }
        return email;
    }
}