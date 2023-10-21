package edu.uef.doan;

public class Assignment {
    private String Title;
    private String Topic;
    private String StartDate;
    private String StartTime;
    private String EndDate;
    private String EndTime;
    private String Category;

    public String getTitle() {return Title;}

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topic) {
        this.Topic = Topic;
    }

    public String getStartDate() {
        return  StartDate;
    }

    public void setStartDate(String  StartDate) {
        this. StartDate =  StartDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) { this.StartTime = StartTime; }

    public String getEndDate() {
        return EndDate;
    }

    public void setEmail(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }
    public Assignment() {
    }

    public Assignment(String Title, String Topic) {
        this.Title = Title;
        this.Topic = Topic;
    }
}