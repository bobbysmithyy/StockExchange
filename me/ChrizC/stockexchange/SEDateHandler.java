package me.ChrizC.stockexchange;

import java.util.Calendar;

public class SEDateHandler {
    
    private StockExchange plugin;
    
    static String mon;
    static String min;
    static String sec;
    static String hour;
    static String day;
    
    public SEDateHandler(StockExchange instance) {
        plugin = instance;
    }
    
    public String getDate() {
        Calendar c1 = Calendar.getInstance();
        
        int month = c1.get(Calendar.MONTH) + 1;
        if (month < 10) {
            mon = "0" + month;
        } else {
            mon = Integer.toString(month);
        }
        
        int minute = c1.get(Calendar.MINUTE);
        if (minute < 10) {
            min = "0" + minute;
        } else {
            min = Integer.toString(minute);
        }
        
        int second = c1.get(Calendar.SECOND);
        if (second < 10) {
            sec = "0" + second;
        } else {
            sec = Integer.toString(second);
        }
        
        int hourr = c1.get(Calendar.HOUR_OF_DAY);
        if (hourr < 10) {
            hour = "0" + hourr;
        } else {
            hour = Integer.toString(hourr);
        }
        
        int dayy = c1.get(Calendar.DATE);
        if (dayy < 10) {
            day = "0" + dayy;
        } else {
            day = Integer.toString(dayy);
        }
        
        return (c1.get(Calendar.YEAR) + "-" + mon + "-" + day + "_" + hour + "." + min + "." + sec);
    }
    
}
