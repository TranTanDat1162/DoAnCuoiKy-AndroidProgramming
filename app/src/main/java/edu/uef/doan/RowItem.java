package edu.uef.doan;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class RowItem {
    private String title;
    private String desc;
    private int bgcolors;
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
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}
