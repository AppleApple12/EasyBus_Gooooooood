package com.example.easybus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class qrcode_page extends AppCompatActivity {
    String email,getmail;
    ImageView qrcode,backBtn,qrscan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_page);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        qrscan = findViewById(R.id.qrscan_btn);
        qrcode=findViewById(R.id.qrimage);
        backBtn=findViewById(R.id.back);
        getmail = mail();
        //前往掃描
        qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qrcode_page.this,qrscnnner.class);
                intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });
        //返回健(基本資料)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qrcode_page.this,Page8Activity.class);
                intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });
        MultiFormatWriter writer = new MultiFormatWriter();
        try{
            BitMatrix martix = writer.encode(getmail, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(martix);
            qrcode.setImageBitmap(bitmap);
        }catch (WriterException e) {
            e.printStackTrace();
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