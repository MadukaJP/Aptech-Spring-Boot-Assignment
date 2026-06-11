package com.aptech.webflux.model;

public class Student {

    private int id;
    private String name;
    private String course;
    private String grade;   // ← NEW: current grade (A/B/C/D/F)

    // Constructor updated to include all 5 fields
    public Student(int id, String name, String course,
                    String grade) {
        this.id     = id;
        this.name   = name;
        this.course = course;
        this.grade  = grade;
    }

    // Getters for all 5 fields
    public int getId()       { return id; }
    public String getName()  { return name; }
    public String getCourse(){ return course; }
    public String getGrade() { return grade; }  
}
