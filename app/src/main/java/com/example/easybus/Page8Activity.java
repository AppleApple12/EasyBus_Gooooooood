package com.example.easybus;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page8Activity extends AppCompatActivity {
    SelectPicPopupWindow menuWindow; //自訂義的彈出框類別(SelectPicPopupWindow)

    ImageView backBtn,editpassword,qrcode,emergency,mycontact;
    CircleImageView mPforfilepic;
    
    TextView mEnteredName,myphone;

    public static final int SELECT_PHOTO=1;
    public static final int TAKE_PHOTO = 3;
    private Uri imageUri;
    private Context mContext;
    public String email,getmail,password,getpass,name;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page8);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        backBtn=findViewById(R.id.backicon);
        mEnteredName = findViewById(R.id.EnteredName);
        mPforfilepic = findViewById(R.id.profilepic);
        myphone = findViewById(R.id.txt1);
        editpassword=findViewById(R.id.frame2);
        qrcode = findViewById(R.id.frame3);
        emergency = findViewById(R.id.frame4);
        mycontact = findViewById(R.id.frame5);
        mContext = Page8Activity.this;

        requestQueue = Volley.newRequestQueue(this);

        getmail=mail();
        getpass=pass();
        System.out.println(mPforfilepic);
        readUser();


        //QRcode
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page8Activity.this,qrcode_page.class);
                intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });

        //修改密碼
        editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page8Activity.this,edit_password.class);
                intent.putExtra("email",getmail);
                intent.putExtra("password",getpass);
                startActivity(intent);
                finish();

            }
        });
        //返回健(回需求者選單)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Page3Activity.class));
            }
        });

        mPforfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //實例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(Page8Activity.this, itemsOnClick);
                //設計彈出框
                menuWindow.showAtLocation(Page8Activity.this.findViewById(R.id.profilepic), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
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
                            phone = response.getString("userphone");
                            name=fullname;
                            myphone.setText(phone);
                            mEnteredName.setText(fullname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Page8Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page8Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
    public String pass(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            password=extras.getString("password");
        }
        return password;
    }
    private View.OnClickListener itemsOnClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()){
                //拍照
                case R.id.takePhotoBtn:
                    takePhoto();
                    break;
                //相簿選擇相片
                case R.id.SelectPhotoBtn:
                    openAlbum();
                    break;
                case R.id.cancelBtn:
                    break;
                default:
                    break;
            }
        }
    };

    private void openAlbum() {
        Intent in=new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in,SELECT_PHOTO);
    }

    public void takePhoto() {
        //時間命名圖片的名稱
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);

        //儲存至DCIM資料夾
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path,filename+".jpg");

        //照片更換
        try {
            //如果上次的照片存在,就刪除
            if (outputImage.exists()) {
                outputImage.delete();
            }
            //創一個新文件
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //如果Android版本大於7.0
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(Page8Activity.this, "com.example.EasyBus.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }

        //申請動態權限
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)mContext,new String[]{Manifest.permission.CAMERA},100);
        }else{
            //啟動相機程序
            startCamera();
        }
    }

    private void startCamera(){
        //指定圖片輸出地址為imageUri
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE"); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri); //指定圖片地址
        startActivityForResult(intent,TAKE_PHOTO); //啟動相機
        //拍完照startActivityForResult() 结果返回onActivityResult()函数
    }

    // 使用startActivityForResult()方法開啟Intent的回調
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                try{
                    //將圖片解析成bitmap對象
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    //將圖片顯示出來
                    mPforfilepic.setImageBitmap(bitmap);
                    System.out.println("turi"+imageUri);
                    System.out.println("tbitmap"+bitmap.toString());
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                break;

            case SELECT_PHOTO:
                Uri uri=data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    //獲取圖片
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    mPforfilepic.setImageBitmap(bitmap);
                    System.out.println("suri"+uri);
                    System.out.println("sbitmap"+bitmap.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                super.onActivityResult(requestCode, resultCode, data);
                break;

            default:
                break;
        }
    }
}
