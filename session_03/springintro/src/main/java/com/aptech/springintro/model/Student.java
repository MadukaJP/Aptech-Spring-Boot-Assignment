package com.aptech.springintro.model;
import jakarta.validation.constraints.*;


public class Student {

    private int id;

    // ── Name ────────────────────────────────────────────────
    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 80,
          message = "Name must be between {min} and {max} characters")
    //                                    ↑       ↑
    //              {min} and {max} are placeholder tokens.
    //              Bean Validation replaces them with the actual values.
    //              Useful when the values might change.
    private String name;

    // ── Course ───────────────────────────────────────────────
    @NotBlank(message = "Please select or enter a course")
    private String course;

    // ── Email ────────────────────────────────────────────────
    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format. Example: alice@school.com")
    private String email;

    // ── Phone ────────────────────────────────────────────────
    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp  = "^0[0-9]{10}$",
        //         ↑ ^      = start of string
        //           0      = must start with 0
        //           [0-9]  = any digit 0-9
        //           {10}   = exactly 10 more digits (11 total including leading 0)
        //           $      = end of string
        message = "Phone must be 11 digits starting with 0 (e.g. 08012345678)"
    )
    private String phone;

    // ── Grade ────────────────────────────────────────────────
    @NotBlank(message = "Please select a grade")
    @Pattern(
        regexp  = "^[ABCDF]$",
        //         ^ start, [ABCDF] one of these 5 chars, $ end
        //         This means ONLY a single letter A, B, C, D, or F is valid
        message = "Grade must be one of: A, B, C, D, or F"
    )
    private String grade;

    // ── Registration Number ──────────────────────────────────
    @NotBlank(message = "Registration number is required")
    @Pattern(
        regexp  = "^APT-[0-9]{4}-[0-9]{4}$",
        //         APT-   = literal prefix
        //         [0-9]{4} = exactly 4 digits (year)
        //         -        = literal hyphen
        //         [0-9]{4} = exactly 4 digits (sequence)
        //         Valid: APT-2024-0001, APT-2023-9999
        //         Invalid: APT-24-001, APTECH-2024-001
        message = "Format must be APT-YYYY-NNNN (e.g. APT-2024-0042)"
    )
    private String regNumber;

    // ── No-arg constructor (required) ────────────────────────
    public Student() {}

    // ── Full constructor ─────────────────────────────────────
    public Student(int id, String name, String course, String email,
                   String phone, String grade, String regNumber) {
        this.id        = id;
        this.name      = name;
        this.course    = course;
        this.email     = email;
        this.phone     = phone;
        this.grade     = grade;
        this.regNumber = regNumber;
    }

    // ── Getters ───────────────────────────────────────────────
    public int    getId()          { return id; }
    public String getName()        { return name; }
    public String getCourse()      { return course; }
    public String getEmail()       { return email; }
    public String getPhone()       { return phone; }
    public String getGrade()       { return grade; }
    public String getRegNumber()   { return regNumber; }

    // ── Setters ───────────────────────────────────────────────
    public void setId(int id)               { this.id = id; }
    public void setName(String name)        { this.name = name; }
    public void setCourse(String course)    { this.course = course; }
    public void setEmail(String email)      { this.email = email; }
    public void setPhone(String phone)      { this.phone = phone; }
    public void setGrade(String grade)      { this.grade = grade; }
    public void setRegNumber(String r)      { this.regNumber = r; }
}

