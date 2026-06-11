
package com.aptech.springintro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
// import org.springframework.ui.ModelAndView;
import org.springframework.web.bind.annotation.*;

import com.aptech.springintro.model.Student;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;



@Controller
public class StudentController {

    private List<Student> students = new ArrayList<>(List.of(
        new Student(1, "Alice Johnson",  "Spring MVC",  "alice@school.com", "08011111111", "A", "APT-2024-0001"),
        new Student(2, "Bob Martins",    "Spring Boot", "bob@school.com",   "08022222222", "B", "APT-2024-0002")
    ));

    // ── READ: List all ──────────────────────────────────────
    @GetMapping("/students")
    public String list(Model model) {
        model.addAttribute("students",   students);
        model.addAttribute("totalCount", students.size());
        return "students";
    }

    // ── CREATE: Show form ───────────────────────────────────
    @GetMapping("/students/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    // ── CREATE: Process form ────────────────────────────────
    @PostMapping("/students/add")
    public String processAdd(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result) {

        if (result.hasErrors()) return "add-student";

        student.setId(students.size() + 1);
        students.add(student);
        return "redirect:/students";
    }

    
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


    // ── UPDATE: Show pre-filled edit form ───────────────────
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Student found = findById(id);
        if (found == null) return "redirect:/students";

        model.addAttribute("student", found);
        return "edit-student";
    }

    // ── UPDATE: Process edit ────────────────────────────────
    @PostMapping("/students/edit/{id}")
    public String processEdit(
            @PathVariable int id,
            @Valid @ModelAttribute("student") Student student,
            BindingResult result) {

        if (result.hasErrors()) return "edit-student";

        // Update the existing student in the list
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                student.setId(id); // keep the original ID
                students.set(i, student);
                break;
            }
        }
        return "redirect:/students";
    }

    // ── Helper: find by ID ──────────────────────────────────
    private Student findById(int id) {
        return students.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }
}
