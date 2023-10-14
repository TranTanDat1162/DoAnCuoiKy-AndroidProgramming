package edu.uef.doan;

public class RowItem {
    private String title;
    private String desc;
    private int bgcolors;
    public RowItem(String title, String desc,int bgcolors) {
        this.title = title;
        this.desc = desc;
        this.bgcolors = bgcolors;
    }
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
