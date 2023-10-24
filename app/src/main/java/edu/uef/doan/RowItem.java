package edu.uef.doan;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class RowItem {
    private String title;
    private String desc;
    private int bgcolors;
    private String date;
    private String type;
    public RowItem(){

    };
    private Date DayNotification;

    public RowItem(String title, String desc,int bgcolors) {
        this.title = title;
        this.desc = desc;
        this.bgcolors = bgcolors;
    }

    public RowItem(String title, int bgcolors, Date dayNotification) {
        this.title = title;
        this.bgcolors = bgcolors;
        DayNotification = dayNotification;

    }

    public RowItem(String topic, int i, String startTime) {
        this.title = topic;
        this.bgcolors = i;
        this.date = startTime;
    }


    public Date getDayNotification() {
        return DayNotification;
    }

    public void setDayNotification(Date dayNotification) {
        DayNotification = dayNotification;
    }

//    public LocalTime getTimeNotification() {
//        return TimeNotification;
//    }
//
//    public void setTimeNotification(LocalTime timeNotification) {
//        TimeNotification = timeNotification;
//    }

    public int getBgcolors() { return bgcolors; }
    public void setBgcolors(int bgcolors) { this.bgcolors = bgcolors; }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
}
