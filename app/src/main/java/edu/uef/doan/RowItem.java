package edu.uef.doan;

public class RowItem {
    private String title;
    private String desc;
    private int bgcolors;
    private String date;
    private String type;
    public RowItem(){

    };
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
