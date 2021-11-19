package com.example.easybus;
/*需求者歷史足跡 好友*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Page1101Activity extends AppCompatActivity {
    String email,getmail,img,img2,imgUrl,fimage;//
    String femail,dayStr;
    String phone;
    ImageView backBtn;
    RequestQueue requestQueue;
    RecyclerView mrecyclerView;
    HistoryAdapter historyAdapter;
    List<friend> friendList;

    ArrayList<String> femailList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1101);


        //隱藏title bar///
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //抓email
        SharedPreferences email = getSharedPreferences("email",MODE_PRIVATE);
        getmail=email.getString("Email","");
        backBtn=findViewById(R.id.back);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mrecyclerView=findViewById(R.id.recyclerview);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(linearLayoutManager);

        friendList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        //從PAGE10傳過來的 年月日
        dayStr = intent.getStringExtra("dayStr");
        System.out.println("dayStr : "+dayStr);
        readfriend();

        //返回我的資料
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page1101Activity.this,Page4Activity.class);
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
                        historyAdapter = new HistoryAdapter(Page1101Activity.this,friendList);
                        mrecyclerView.setAdapter(historyAdapter);
                        /*imageListAdapter = new ImageListAdapter(my_contact.this, imageLists);
                        mrecyclerView.setAdapter(imageListAdapter);
                        //imgrecyclerView.setAdapter(imageListAdapter);*/

                        historyAdapter.setOnItemClick(new HistoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String myfemail  = friendList.get(position).getFemail();
                                System.out.println("myfemail : " + myfemail);

                                Intent intent = new Intent(Page1101Activity.this,Page11Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("email",myfemail);
                                bundle.putString("dayStr",dayStr);
//                                intent.putExtra("email",myfemail);
//                                intent.putExtra("dayStr",dayStr);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Page1101Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(Page1101Activity.this);
        requestQueue2.add(request);
    }
    public String imgurlString(final String img2){
        return Urls.url1+"/LoginRegister/images/"+img2;
    }
}