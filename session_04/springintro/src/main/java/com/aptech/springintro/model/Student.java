// ============================================================
// SNIPPET 03: JPA Entity Class
// ============================================================
// We merge Session 3 (Validation) with Session 4 (Database).
// The class now has TWO types of annotations:
// 1. Validation (jakarta.validation.constraints) -> rules for data
// 2. JPA (jakarta.persistence) -> rules for the database table
// ============================================================

package com.aptech.springintro.model;

// 1. Database Annotations
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column; // optional, for detailed table config

// 2. Validation Annotations
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;


@Entity // ◄ Tells Hibernate: "Make a database table for this class"
@Table(name = "students") // ◄ Optional: Names the table 'students' instead of 'Student'
public class Student {

    @Id // ◄ Tells Hibernate: "This field is the Primary Key"
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ◄ Tells Hibernate: "MySQL will Auto-Increment this. Do not ask me for an ID."
    private int id;


    // You can mix Validation and JPA Column settings on the same field
    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 80, message = "Name must be 2-80 characters")
    @Column(name = "full_name", nullable = false, length = 80)
    // ◄ @Column is optional. It overrides the default column settings in MySQL.
    // Here, we rename the column to 'full_name' instead of 'name'.
    private String name;


    @NotBlank(message = "Course is required")
    private String course;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true) // ◄ Tells MySQL: "Do not allow duplicate emails!"
    private String email;


    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^0[0-9]{10}$", message = "Phone must be 11 digits starting with 0")
    private String phone;


    @NotBlank(message = "Grade is required")
    @Pattern(regexp = "^[ABCDF]$", message = "Grade must be A, B, C, D, or F")
    private String grade;


    @NotBlank(message = "Registration number is required")
    @Pattern(regexp = "^APT-[0-9]{4}-[0-9]{4}$", message = "Format must be APT-YYYY-NNNN")
    @Column(unique = true) // ◄ Reg numbers must be unique
    private String regNumber;


    // ── NO-ARG CONSTRUCTOR ──────────────────────────────────
    // REQUIRED BY BOTH THYMELEAF AND JPA!
    // Hibernate uses this to create empty objects when reading from the database.
    public Student() {}

    // ── FULL CONSTRUCTOR ────────────────────────────────────
    public Student(String name, String course, String email,
                   String phone, String grade, String regNumber) {
        // Notice we REMOVED 'id' from the constructor!
        // We do not set the ID manually anymore. MySQL does it.
        this.name      = name;
        this.course    = course;
        this.email     = email;
        this.phone     = phone;
        this.grade     = grade;
        this.regNumber = regNumber;
    }

    // ── GETTERS AND SETTERS ─────────────────────────────────
    public int    getId()        { return id; }
    public String getName()      { return name; }
    public String getCourse()    { return course; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public String getGrade()     { return grade; }
    public String getRegNumber() { return regNumber; }

    public void setId(int id)              { this.id = id; }
    public void setName(String name)       { this.name = name; }
    public void setCourse(String course)   { this.course = course; }
    public void setEmail(String email)     { this.email = email; }
    public void setPhone(String phone)     { this.phone = phone; }
    public void setGrade(String grade)     { this.grade = grade; }
    public void setRegNumber(String r)     { this.regNumber = r; }
}