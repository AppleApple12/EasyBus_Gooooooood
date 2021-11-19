package com.example.easybus;
/*公車資訊*/
public class BusInfo {
    String htmlinstructions;
    String shortname;
    int pic;

    public String getHtmlinstructions() {
        return htmlinstructions;
    }

    public void setHtmlinstructions(String htmlinstructions) {
        this.htmlinstructions = htmlinstructions;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public int getPic() { return pic;}

    public void setPic(int pic) { this.pic = pic; }

    public BusInfo(){}
}
