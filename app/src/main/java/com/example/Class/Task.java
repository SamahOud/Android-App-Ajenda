package com.example.Class;

public class Task {
    private int taskId;
    private String title;
    private String details;
    private String date;
    private String ends;
    private String starts;
    private String category;
    private String priority;
    private int priorityNum;
    private String location;
    private String created_at;

    public Task() {

    }
    /* without id */
    public Task(String title, String details, String date,
                String ends, String starts,String category,
                String priority, int priorityNum, String location) {
        this.title = title;
        this.details = details;
        this.date = date;
        this.starts = starts;
        this.ends = ends;
        this.category = category;
        this.priority = priority;
        this.priorityNum = priorityNum;
        this.location = location;
    }
    /* with id */
    public Task(int taskId, String title, String details,
                String date, String starts, String ends,
                String category, String priority,
                int priorityNum, String location) {
        this.taskId = taskId;
        this.title = title;
        this.details = details;
        this.date = date;
        this.starts = starts;
        this.ends = ends;
        this.category = category;
        this.priority = priority;
        this.priorityNum = priorityNum;
        this.location = location;
    }

    public int get_TaskId() { return taskId; }
    public void set_TaskId(int taskId) { this.taskId = taskId; }

    public String get_Title() { return title; }
    public void set_Title(String title) { this.title = title; }

    public String get_Details() { return details; }
    public void set_Details(String details) { this.details = details; }

    public String get_S_Date() { return date; }
    public void set_S_Date(String startDate) { this.date = startDate; }

    public String get_Starts() { return starts; }
    public void set_Starts(String starts) { this.starts = starts; }

    public String get_Ends() { return ends; }
    public void set_Ends(String ends) { this.ends = ends; }

    public String get_Category() { return category; }
    public void set_Category(String category) { this.category = category; }

    public String get_Priority() { return priority; }
    public void set_Priority(String priority) { this.priority = priority; }

    public int get_PriorityNum() { return priorityNum; }
    public void set_PriorityNum(int priorityNum) { this.priorityNum = priorityNum; }

    public String get_Location() { return location; }
    public void set_Location(String location) { this.location = location; }

    public void setCreatedAt(String created_at) { this.created_at = created_at; }
}
