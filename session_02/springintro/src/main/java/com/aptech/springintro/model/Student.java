package com.aptech.springintro.model;


public class Student {

    private int id;
    private String name;
    private String course;
    private String email;   // ← NEW: student's email address
    private String grade;   // ← NEW: current grade (A/B/C/D/F)

    // Constructor updated to include all 5 fields
    public Student(int id, String name, String course,
                   String email, String grade) {
        this.id     = id;
        this.name   = name;
        this.course = course;
        this.email  = email;
        this.grade  = grade;
    }

    // Getters for all 5 fields
    public int getId()       { return id; }
    public String getName()  { return name; }
    public String getCourse(){ return course; }
    public String getEmail() { return email; } 
    public String getGrade() { return grade; }  
}

