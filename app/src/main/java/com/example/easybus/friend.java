package com.example.easybus;
/*我的聯絡人*/
public class friend {
    String F_name,F_phone,F_image;
    private String imageUrl;
    private String femail;

    public String getF_phone() {
        return F_phone;
    }

    public void setF_phone(String f_phone) {
        F_phone = f_phone;
    }

    public String getF_name() {
        return F_name;
    }

    public void setF_name(String f_name) {
        F_name = f_name;
    }

    public String getF_image() {
        return F_image;
    }

    public void setF_image(String f_image) {
        F_image = f_image;
    }
    public String getFemail(){return femail;}
    public void setFemail(String femail){this.femail=femail;}

    public String getImageUrl(){
        return imageUrl;
    }
    public  void  setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

}