package com.example.easybus;
/*需求者 - 我的帳戶*/

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page8Activity extends AppCompatActivity {
    SelectPicPopupWindow menuWindow; //自訂義的彈出框類別(SelectPicPopupWindow)
    View editpassword,qrcode,emergency,mycontact,logout;
    ImageView backBtn;
    CircleImageView mPforfilepic;
    TextView mEnteredName,myphone;
    String identity;
    public static final int SELECT_PHOTO=1;
    public static final int TAKE_PHOTO = 3;
    private Uri imageUri;
    private Context mContext;
    public String email,getmail,password,getpass,fullname,pic,phone,encodeimage,img;
    RequestQueue requestQueue,requestQueue1;
    String url =Urls.url1+"/LoginRegister/savepic.php?fullname="+fullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page8);

        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        backBtn=findViewById(R.id.backicon);
        mEnteredName = findViewById(R.id.EnteredName);
        mPforfilepic = findViewById(R.id.profilepic);
        myphone = findViewById(R.id.Enteredphone);
        editpassword=findViewById(R.id.view2);
        qrcode = findViewById(R.id.view3);
        emergency = findViewById(R.id.view4);
        mycontact = findViewById(R.id.view5);
        logout = findViewById(R.id.view6);
        mContext = Page8Activity.this;

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);

        //浮動按鈕撥打給緊急聯絡人
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser1();
            }
        });

        new Page8Activity.fetchDatapage8().execute();
        //我的聯絡人
        mycontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page8Activity.this,my_contact.class);
                //intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });
        //新增緊急聯絡人
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page8Activity.this,emergency_contact.class);
                //intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });
        //登出
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
                email.edit().clear().commit();
                Intent it = new Intent(Page8Activity.this,Login3.class);
                startActivity(it);
            }
        });

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
                //intent.putExtra("email",getmail);
                intent.putExtra("password",getpass);
                startActivity(intent);
                finish();

            }
        });
        //返回健(回選單)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("requester".equalsIgnoreCase(identity)) {
                    Intent it4 = new Intent(Page8Activity.this, Page3Activity.class);
                    //it4.putExtra("email", getmail);
                    startActivity(it4);
                }else if("caregiver".equalsIgnoreCase(identity)){
                    Intent it = new Intent(Page8Activity.this,Page4Activity.class);
                   // it.putExtra("email",getmail);
                    startActivity(it);
                }

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
    public class fetchDatapage8 extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected void onPostExecute(Void aVoid) {
            super.onPreExecute();
            readUser();
            fetchimage();
            ImageRetriveWithPicasso();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    //抓取使用者基本資料
    public void readUser(){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fullname = response.getString("fullname");
                            phone = response.getString("userphone");
                            identity = response.getString("identity");
                            turnpage(identity);
                            myphone.setText(phone);
                            mEnteredName.setText(fullname);
                            img = response.getString("image");
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(Page8Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(Page8Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    //儲存頭貼
    private void savepic(){//
        String URL =Urls.url1+"/LoginRegister/upload.php?email="+getmail;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(Page8Activity.this,response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Page8Activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("image",encodeimage);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Page8Activity.this);
        requestQueue.add(request);
    }
    public  void fetchimage(){
        String URL =Urls.url1+"/LoginRegister/fetchimage.php?email="+getmail;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    img  = jsonObject.getString("image");

                    ImageRetriveWithPicasso();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(Page8Activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Page8Activity.this);
        requestQueue.add(request);
    }
    public void ImageRetriveWithPicasso() {
        String imgurl = Urls.url1+"/LoginRegister/images/"+img;

        Picasso.with(this)

                .load(String.valueOf(imgurl))
                .placeholder(R.drawable.profile)
                .fit()
               // .error(R.drawable.ic_error_black_24dp)
                .into(mPforfilepic, new Callback() {
                    @Override
                    public void onSuccess() {
                        // 圖片讀取完成
                        //Toast.makeText(Page8Activity.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        // 圖片讀取失敗
                      //  Toast.makeText(Page8Activity.this, "失敗拉幹", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println(imgurl);
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
                    int degree = readPictureDegree((imageUri).toString());
                    Bitmap bitmap1 = rotateBitmap(bitmap, degree);
                    //將圖片顯示出來
                    mPforfilepic.setImageBitmap(bitmap);
                    if(data!=null){
                        System.out.println("turi"+imageUri);
                        System.out.println("tbitmap"+bitmap.toString());
                        pic=imageUri.toString();
                        imageStore(bitmap1);
                        savepic();
                    }else{

                        Intent it4 = new Intent(Page8Activity.this, Page8Activity.class);
                        it4.putExtra("email",getmail);
                        readUser();
                        fetchimage();
                        ImageRetriveWithPicasso();
                        startActivity(it4);
                    }

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                break;

            case SELECT_PHOTO:
                try {
                    //獲取圖片
                    if(data != null){
                        Uri uri=data.getData();
                    if(uri != null){
                        ContentResolver cr = this.getContentResolver();
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        int degree = readPictureDegree((uri).toString());
                        Bitmap bitmap2 = rotateBitmap(bitmap, degree);
                        mPforfilepic.setImageBitmap(bitmap);
                        imageStore(bitmap2);
                        System.out.println("suri"+uri);
                        System.out.println("sbitmap"+bitmap.toString());
                        pic=uri.toString();
                        savepic();
                    }
                    }else{
                        Intent it4 = new Intent(Page8Activity.this, Page8Activity.class);
                        //it4.putExtra("email",getmail);
                        readUser();
                        fetchimage();
                        ImageRetriveWithPicasso();
                        startActivity(it4);
                    }

                } catch (FileNotFoundException e) {

                    e.printStackTrace();

                }
                super.onActivityResult(requestCode, resultCode, data);
                break;

            default:
                break;

        }
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imageBytes = stream.toByteArray();
        encodeimage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    private void turnpage(String identity) {

        /*if("requester".equalsIgnoreCase(identity)) {
            Intent it4 = new Intent(Page8Activity.this,Page8Activity.class);
            //it4.putExtra("email", email);
            startActivity(it4);
        }else */if("caregiver".equalsIgnoreCase(identity)){
            Intent it = new Intent(Page8Activity.this,Page8Activity_caregiver.class);
            //it.putExtra("email", email);
            startActivity(it);

        }
    }

    /**
     * 获取图片旋转角度
     * @param srcPath
     * @return
     */
    private static int readPictureDegree(String srcPath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(srcPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //处理图片旋转
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    @SuppressLint("LongLogTag")
    protected void makeCall(final String phone) {
        //Snackbar.make(v,"打電話給緊急連絡人",Snackbar.LENGTH_LONG).setAction("Action",null).show();
        Intent call = new Intent(Intent.ACTION_DIAL);
        Uri u = Uri.parse("tel:"+phone);
        call.setData(u);

        try {
            startActivity(call);
            finish();
            Log.i("Finished making a call...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(Page5012Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Page8Activity.this,"請重撥！", Toast.LENGTH_SHORT).show();
        }
        //startActivity(call        );
    }
    public void readUser1(){
        String URL =Urls.url1+"/LoginRegister/fetch.php?email="+getmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    String emergency_phone;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            emergency_phone = response.getString("emergency_contact");
                            makeCall(emergency_phone);
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
        requestQueue1.add(jsonObjectRequest);
    }
}