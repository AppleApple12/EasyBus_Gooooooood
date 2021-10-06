package com.example.easybus;

import java.util.Date;

public class history {
    String date;          //日期
    double latitude;    //景點店家緯度
    double longitude;   //景點店家經度


   /* public history(String date,double latitude,double longitude){
        date=this.date;
        latitude=this.latitude;
        longitude=this.longitude;
    }*/
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
