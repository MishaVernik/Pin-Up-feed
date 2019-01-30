package com.example.yanec.onexbet.InternetClasses;

import java.util.Date;
import java.util.List;

import com.example.yanec.onexbet.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("A")
    @Expose
    public String a;
    @SerializedName("C")
    @Expose
    public String c;
    @SerializedName("D")
    @Expose
    public String d;
    @SerializedName("H")
    @Expose
    public String h;
    @SerializedName("NP")
    @Expose
    public String nP;
    @SerializedName("P")
    @Expose
    public Double p;
    @SerializedName("T")
    @Expose
    public Double t;
    @SerializedName("U")
    @Expose
    public String u;
    @SerializedName("V")
    @Expose
    public Double v;
    @SerializedName("B1")
    @Expose
    public Double b1;
    @SerializedName("B2")
    @Expose
    public Object b2;
    @SerializedName("EE")
    @Expose
    public List<EE> eE = null;
    @SerializedName("HL")
    @Expose
    public Boolean hL;
    @SerializedName("I")
    @Expose
    public Double i;
    @SerializedName("S")
    @Expose
    public String s;


    private String getDataStr(){
        String dateStr = "";
        for(int i = 6; i < d.length() - 2; i++){
            dateStr += d.charAt(i);
        }

        return dateStr;
    }

    public String getData(){
        Date date = new Date(Long.parseLong(getDataStr()));

        return Constants.simpleDateFormat.format(date);
    }

    public String getMatchInfo(){
        String result = "";
        boolean firstNull = false;

        if(a != null && !a.isEmpty()){
            result += a;
        }else{
            firstNull = true;
        }

        if(h != null && !h.isEmpty()){
            if(!firstNull) {
                result += " - " + h;
            }else{
                result = h;
            }
        }

        return result;
    }

}