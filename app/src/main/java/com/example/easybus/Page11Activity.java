package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * An activity that displays a Google map with polylines to represent paths or routes,
 * and polygons to represent areas.
 */
public class Page11Activity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener{
    private static final String TAG = "Page11Activity";
    //GoogleMap.OnPolygonClickListener{
    private GoogleMap mMap;
    ArrayList<String> dateArrayList;
    ArrayList<history> historyArrayList = new ArrayList<>();
   // historyAdapter historyAdapter;
    String getmail;
    String dayStr,date,la,lo;
    Double latitude,longitude;
    Double mlatitude,mlongitude;
    RequestQueue requestQueue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page11);
        Page11Activity page11Activity =new Page11Activity();
        dateArrayList =  new ArrayList<>();



        Intent intent = getIntent();
        //從PAGE1101 傳過來的 年月日
        dayStr = intent.getStringExtra("dayStr");
        System.out.println("dayStr : "+dayStr);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //從PAGE1101 傳過來的 朋友email
        getmail = intent.getStringExtra("email");
        System.out.println("email : "+getmail);


        // page11Activity.fetch_H();
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.r_map);
        mapFragment.getMapAsync(this);
        requestQueue2 = Volley.newRequestQueue(Page11Activity.this);
       // requestQueue2.add(jsonArrayRequest);

        //跳頁回家長主頁
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(Page11Activity.this,Page4Activity.class);
                startActivity(it1);
            }
        });
        fetch_history();

    }

    public void fetch_history(){
        String URL =Urls.url1+"/LoginRegister/fetch_perhistory.php?email="+getmail;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        for(int i =0;i<array.length();i++){
                            try{
                                JSONObject object = array.getJSONObject(i);
                                date = object.getString("date").trim();
                                la = object.getString("latitude").trim();
                                lo = object.getString("longitude").trim();
                                latitude=Double.parseDouble(la);
                                longitude=Double.parseDouble(lo);
                                //mlatitude=GetLa(latitude);
                                System.out.println("Get()");
                                System.out.println("date :"+date);
                                System.out.println("latitude :"+latitude);
                                System.out.println("longitude :"+longitude);

                                history h =new history();
                                h.setDate(date);
                                h.setLatitude(latitude);
                                h.setLongitude(longitude);
                                historyArrayList.add(h);


                            } catch (JSONException e) {
                                System.out.println("JSONException e :"+e.toString());
                            }
                        }
                       // historyAdapter =  new historyAdapter(Page11Activity.this,historyArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(Page11Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(Page11Activity.this);
        requestQueue2.add(jsonArrayRequest);
    }

    /**  for(history mh :historyArrayList){
     System.out.println("getDate :"+mh.getDate());
     System.out.println("getLatitude :"+mh.getLatitude());
     System.out.println("getLongitude :"+mh.getLongitude());
     }
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        
        // Add polylines to the map.
        // Polylines are useful to show a route or some other connection between points.
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        // 把抓到的資料 丟到這邊
                        new LatLng(-35.016, 143.321),//緯度 經度
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.setTag("A");
        // Style the polyline.
        stylePolyline(polyline1);

        /*Polyline polyline2 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-29.501, 119.700),
                        new LatLng(-27.456, 119.672),
                        new LatLng(-25.971, 124.187),
                        new LatLng(-28.081, 126.555),
                        new LatLng(-28.848, 124.229),
                        new LatLng(-28.215, 123.938)));
        polyline2.setTag("B");
        stylePolyline(polyline2);*/

        /*// Add polygons to indicate areas on the map.
        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-27.457, 153.040),
                        new LatLng(-33.852, 151.211),
                        new LatLng(-37.813, 144.962),
                        new LatLng(-34.928, 138.599)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("alpha");
        // Style the polygon.
        stylePolygon(polygon1);

        Polygon polygon2 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-31.673, 128.892),
                        new LatLng(-31.952, 115.857),
                        new LatLng(-17.785, 122.258),
                        new LatLng(-12.4258, 130.7932)));
        polygon2.setTag("beta");
        stylePolygon(polygon2);*/

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        //googleMap.setOnPolygonClickListener(this);
    }

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    /**
     * Styles the polyline, based on type.
     * @param polyline2 The polyline object that needs styling.
     */
    private void stylePolyline(Polyline polyline2) {
        // Use a round cap at the start of the line.
        polyline2.setStartCap(new RoundCap());


        polyline2.setEndCap(new RoundCap());
        polyline2.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline2.setColor(COLOR_BLACK_ARGB);
        polyline2.setJointType(JointType.ROUND);
    }
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    /**
     * Listens for clicks on a polyline.
     * @param polyline The polyline object that the user has clicked.
     */

    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern. 從實心筆劃翻轉為虛線筆劃圖案
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke. 默認圖案是實心筆劃。
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Listens for clicks on a polygon.
     * @param polygon The polygon object that the user has clicked.
     */

   /* @Override
    public void onPolygonClick(Polygon polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor() ^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor() ^ 0x00ffffff;
        polygon.setFillColor(color);

        Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();

    }*/
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);



    /**
     * Styles the polygon, based on type.
     * @param polygon2 The polygon object that needs styling.
     */
    /*private void stylePolygon(Polygon polygon2) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon2.getTag() != null) {
            type = polygon2.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon2.setStrokePattern(pattern);
        polygon2.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon2.setStrokeColor(strokeColor);
        polygon2.setFillColor(fillColor);
    }*/





}