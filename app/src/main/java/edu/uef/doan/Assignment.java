package edu.uef.doan;

public class Assignment {
    private String Title;
    private String Topic;
    private String StartDate;
    private String StartTime;

    public int getNumAnsAttachments() {
        return NumAnsAttachments;
    }

    public void setNumAnsAttachments(int numAnsAttachments) {
        NumAnsAttachments = numAnsAttachments;
    }

    private int NumAnsAttachments;

    public int getNumAttachments() {
        return NumAttachments;
    }

    public void setNumAttachments(int numAttachments) {
        NumAttachments = numAttachments;
    }

    private int NumAttachments;

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    private String Answer;

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    private String EndDate;
    private String EndTime;
    private String Category;
    private String CreateTime;
    private String SubmitTime;

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

    public String getEndDate() { return EndDate; }

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
    public String getCreateTime() { return CreateTime; }

    public String getSubmitTime() { return SubmitTime; }

    public void setSubmitTime(String SubmitTime) { this.SubmitTime = SubmitTime;}
    public Assignment() {
    }

    public Assignment(String Title, String Topic) {
        this.Title = Title;
        this.Topic = Topic;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
