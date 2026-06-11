package com.aptech.webflux.model;

public class Course {

    private int id;
    private String title;
    private String instructor;
    private int duration;
    private String level;

    public Course(int id, String title, String instructor, int duration, String level) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
        this.duration = duration;
        this.level = level;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getInstructor() { return instructor; }
    public int getDuration() { return duration; }
    public String getLevel() { return level; }
}
