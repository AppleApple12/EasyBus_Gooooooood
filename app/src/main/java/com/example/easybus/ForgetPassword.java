package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    Button sendEmailBtn;
    EditText userEmail;
    ImageButton backBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sendEmailBtn=findViewById(R.id.okBtn);
        userEmail=findViewById(R.id.Email);
        backBtn=findViewById(R.id.back);

        fAuth=FirebaseAuth.getInstance();

        sendEmailBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String mEmail=userEmail.getText().toString();

                if(TextUtils.isEmpty(mEmail))
                    Toast.makeText(ForgetPassword.this, "輸入您的電子信箱", Toast.LENGTH_SHORT).show();
                else{
                    fAuth.sendPasswordResetEmail(mEmail).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetPassword.this,"查看您的電子信箱",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPassword.this,Login.class));
                            }
                            else
                                Toast.makeText(ForgetPassword.this,"錯誤:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //返回健(回Login頁面)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}