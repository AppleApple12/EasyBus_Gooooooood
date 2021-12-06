package com.example.easybus;
/*需求者定位查詢 好友*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Page901Activity extends AppCompatActivity {
    String email,getmail,img,img2,imgUrl,fimage;//
    String femail,dayStr;
    String phone;
    ImageView backBtn;
    RequestQueue requestQueue;
    RecyclerView mrecyclerView;
    HistoryAdapter historyAdapter;
    List<friend> friendList;
    cardAdapter cardAdapter;
    ViewPager viewPager;

    ArrayList<String> femailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page901);


        //隱藏title bar///
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        backBtn=findViewById(R.id.back);
        viewPager = findViewById(R.id.viewpager);
        readfriend();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        friendList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);


        //返回我的資料
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page901Activity.this,Page4Activity.class);
                //intent.putExtra("email",getmail);
                startActivity(intent);
                finish();
            }
        });

    }
    public void readfriend(){
        String URL =Urls.url1+"/LoginRegister/my_contact.php?email="+getmail;
        //Request.Method.GET,URL,null我自己加的
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        if (array.length()==0){
                           /* clickme.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(my_contact.this,qrcode_page.class);
                                    // intent.putExtra("email",getmail);
                                    startActivity(intent);
                                }
                            });*/
                        }else
                            for(int i =0;i<array.length();i++){
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    String name = object.getString("F_name").trim();
                                    fimage = object.getString("F_image").trim();
                                    femail = object.getString("F_email").trim();

                                    friend f =new friend();
                                    f.setF_name(name);
                                    //ImageList img = new ImageList();
                                    f.setFemail(femail);
                                    imgUrl = imgurlString(fimage);
                                    f.setImageUrl(imgUrl);
                                    //imageLists.add(img);
                                    femailList.add(femail);
                                    //fetchfimage();
                                    friendList.add(f);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        cardAdapter = new cardAdapter(Page901Activity.this,friendList);
                        viewPager.setAdapter(cardAdapter);
                        viewPager.setPadding(100,0,100,0);

                        cardAdapter.setCallBack(new cardAdapter.CallBack() {
                            @Override
                            public void OnClick(int position) {
                                String myfemail  = friendList.get(position).getFemail();
                                System.out.println("myfemail : " + myfemail);

                                Intent intent = new Intent(Page901Activity.this,Page9Activity.class);
                                intent.putExtra("email",myfemail);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Page901Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(Page901Activity.this);
        requestQueue2.add(request);
    }
    public String imgurlString(final String img2){
        return Urls.url1+"/LoginRegister/images/"+img2;
    }
} 