  package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.net.InetAddress;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login2 extends AppCompatActivity {

    InetAddress ip;
    EditText mPassword, mEmail;
    TextView mRegistertext,mForgetext;
    ImageButton backBtn;
    Button mLoginBtn;
    ProgressBar mProgressBar;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //隱藏title bar///
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mPassword=findViewById(R.id.password2);
        mEmail=findViewById(R.id.Email);
        mRegistertext=findViewById(R.id.registertext);
        mLoginBtn=findViewById(R.id.login);
        mForgetext=findViewById(R.id.Forgetext);
        backBtn=findViewById(R.id.back);

        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        //跳到註冊
        mRegistertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp2.class);
                startActivity(intent);
                finish();
            }
        });
        //跳到忘記密碼
        mForgetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this,Forgotpassword.class);
                startActivity(intent);
                Toast.makeText(Login2.this,"跳頁成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email,password;

                email = String.valueOf(mEmail.getText());
                password = String.valueOf(mPassword.getText());

                if(!email.equals("") && !password.equals("")) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "email";
                            field[1] = "password";

                            String[] data = new String[2];
                            data[0] = email;
                            data[1] = password;

                            PutData putData = new PutData("http://192.168.0.132/LoginRegister/login.php", "POST", field, data);//小高電腦的IP
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    mProgressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        //System.out.println("幹"+result);
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Page8Activity.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",password);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        //System.out.println(result);   ///哈囉
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"All filed required",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
