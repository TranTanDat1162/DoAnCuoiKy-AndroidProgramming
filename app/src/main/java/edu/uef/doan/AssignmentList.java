package edu.uef.doan;

public class AssignmentList {
    public AssignmentList(String id, Assignment assignment) {
        this.id = id;
        this.assignment = assignment;
    }

    public AssignmentList() {    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    private Assignment assignment;
}
