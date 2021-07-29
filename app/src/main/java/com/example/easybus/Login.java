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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mPassword, mEmail;
    TextView mRegistertext,mForgetext;
    ImageButton backBtn;
    Button mLoginBtn;
    ProgressBar mProgressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mPassword=findViewById(R.id.password);
        mEmail=findViewById(R.id.Email);
        mRegistertext=findViewById(R.id.registertext);
        mLoginBtn=findViewById(R.id.RegisterBtn);
        mForgetext=findViewById(R.id.Forgetext);
        backBtn=findViewById(R.id.back);
        fAuth=FirebaseAuth.getInstance();
        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        //跳到註冊
        mRegistertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        /*//跳頁到"忘記密碼"
        mForgetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
            }
        });*/

        //返回健(回需求者選單)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Page3Activity.class));
            }
        });

        //按登入
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tex_email = mEmail.getText().toString().trim();
                String tex_password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(  tex_email)){
                    mEmail.setError("請輸入電子信箱");
                    return;
                }
                if(TextUtils.isEmpty(tex_password)) {
                    mPassword.setError("請輸入密碼");
                    return;
                }
                if(tex_password.length()<6){
                    mPassword.setError("密碼至少要6位數");
                    return;
                }else{
                    mProgressBar.setVisibility(View.VISIBLE);
                    login(tex_email,tex_password);
                }
            }
        });
    }

    //判斷是否登入成功
    private void login(String tex_email, String tex_password) {
        mProgressBar.setVisibility(View.VISIBLE);
        fAuth.signInWithEmailAndPassword(tex_email,tex_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){//如果執行成功
                    Toast.makeText(Login.this,"登入成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Page8Activity.class));
                }else{
                    Toast.makeText(Login.this,"登入失敗"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }//
        });
    }
}
