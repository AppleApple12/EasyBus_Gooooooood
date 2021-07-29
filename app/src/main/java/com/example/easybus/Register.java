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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText mFullname, mPassword, mEmail;
    TextView mRegistertext;
    Button mReg;
    ProgressBar mProgressBar;

    FirebaseAuth fAuth;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fAuth=FirebaseAuth.getInstance();

        mFullname=findViewById(R.id.fullname);
        mPassword=findViewById(R.id.password2);
        mEmail=findViewById(R.id.Email);
        mRegistertext=findViewById(R.id.Registertext);
        mReg=findViewById(R.id.RegisterBtn);

        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String fullName=mFullname.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("請輸入電子信箱");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("請輸入密碼");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("密碼至少要6位數");
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    mFullname.setError("請輸入姓名");
                    return;
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    RegisterUser(email, password, fullName); //多加fullName
                }
            }

            private void RegisterUser(final String email, final String password , final String fullName) {

                //使用者註冊
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user= fAuth.getCurrentUser();
                            assert user != null;
                            String uid = user.getUid();
                            mRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                            HashMap<String,String> hashMap = new HashMap<>();  //看不懂HashMap
                            hashMap.put("fullName",fullName);
                            hashMap.put("email",email);
                            hashMap.put("password",password);
                            hashMap.put("imageURL","default");
                            mRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this,"註冊成功", Toast.LENGTH_SHORT).show(); //和影片不一樣
                                        Intent intent = new Intent(Register.this,Login.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // 加得莫名其妙
                                        startActivity(intent);

                                    }else{

                                        Toast.makeText(Register.this,"註冊失敗"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                            //updateUI(user);
                        }else{
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(Register.this,"註冊失敗"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        //跳到註冊
        mRegistertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));

            }
        });
    }
}
