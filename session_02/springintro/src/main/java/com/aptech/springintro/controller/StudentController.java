

package com.aptech.springintro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.ui.ModelAndView;
import org.springframework.web.bind.annotation.*;

import com.aptech.springintro.model.Student;

import java.util.ArrayList;
import java.util.List;



@Controller
public class StudentController {

    // Shared list — all methods in this controller can access it
    // (In Session 4, a real database replaces this list)
    private List<Student> students = new ArrayList<>(List.of(
        new Student(1, "Alice Johnson",  "Spring MVC",     "alice@school.com", "A"),
        new Student(2, "Bob Martins",    "Spring Boot",    "bob@school.com",   "B"),
        new Student(3, "Charlie Okafor", "Spring Security","charlie@school.com","A")
    ));

    // ── GET /students — show all students ───────────────────
    @GetMapping("/students")
    public String showStudents(Model model) {
        model.addAttribute("students", students);
        model.addAttribute("pageTitle", "Our Students");
        model.addAttribute("totalCount", students.size());
        return "students";
    }

    // ── GET /students/{id} — show ONE student ───────────────
    @GetMapping("/students/{id}")
    //                    ↑
    // {id} is a URL variable — it can be any number
    // /students/1 → id=1, /students/42 → id=42
    public String showStudentDetail(@PathVariable int id, Model model) {
        //                           ↑
        // @PathVariable: "take {id} from the URL and put it in this parameter"

        // Find the student with matching id
        Student found = students.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null); // null if not found

        if (found == null) {
            model.addAttribute("error", "No student found with ID: " + id);
            return "error-page";
        }

        model.addAttribute("student", found);
        return "student-detail";
    }

    // ── GET /students/add — show the empty add form ─────────
    @GetMapping("/students/add")
    public String showAddForm() {
        // No data needed — just render the empty form
        return "add-student";
    }

    // ── POST /students/add — process the submitted form ─────
    @PostMapping("/students/add")
    public String processAddStudent(
            @RequestParam String name,
            //             ↑ reads form field with name="name"
            @RequestParam String course,
            @RequestParam String email,
            @RequestParam String grade,
            Model model) {

        // Create a new Student with the next available ID
        int newId = students.size() + 1;
        Student newStudent = new Student(newId, name, course, email, grade);

        // Add to our list
        students.add(newStudent);

        // Show success message
        model.addAttribute("message",
            "✅ Student '" + name + "' enrolled successfully! ID assigned: " + newId);

        // POST → Redirect → GET pattern
        // Don't render a template after POST — redirect instead!
        return "redirect:/students";
        //      ↑ Tells browser: "Go make a fresh GET request to /students"
        //        This prevents the "Resubmit form?" warning on page refresh
    }
}

