

package com.aptech.springintro.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.aptech.springintro.model.Student;
import com.aptech.springintro.repository.StudentRepository;

import java.util.List;

@Controller
public class StudentController {

    @Autowired  
    private StudentRepository studentRepo;

    // ── 1. READ ALL (With Search) ───────────────────────────
    @GetMapping("/students")
    public String list(@RequestParam(required = false) String search, Model model) {
        
        List<Student> studentsFromDb;
        
        if (search != null && !search.isBlank()) {
            // Let MySQL do the searching!
            studentsFromDb = studentRepo.findByNameContainingIgnoreCase(search);
        } else {
            // Get all students
            studentsFromDb = studentRepo.findAll();
        }

        model.addAttribute("students", studentsFromDb);
        model.addAttribute("totalCount", studentsFromDb.size());
        return "students";
    }

    // ── 2. READ ONE ─────────────────────────────────────────
    @GetMapping("/students/{id}")
    public String viewDetail(@PathVariable int id, Model model) {
        // findById returns an Optional. We use orElse(null) to handle missing records.
        Student found = studentRepo.findById(id).orElse(null);
        
        if (found == null) {
            model.addAttribute("error", "Student ID " + id + " not found.");
            return "error-page";
        }
        
        model.addAttribute("student", found);
        return "student-detail";
    }

    // ── 3. CREATE (Show Form) ───────────────────────────────
    @GetMapping("/students/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    // ── 4. CREATE (Save to DB) ──────────────────────────────
    @PostMapping("/students/add")
    public String processAdd(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result) {
            
        if (result.hasErrors()) return "add-student";
        
        // SAVE TO MYSQL! (Hibernate runs: INSERT INTO students...)
        studentRepo.save(student);
        
        return "redirect:/students";
    }


    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Student found = studentRepo.findById(id).orElse(null);
        if (found == null) return "redirect:/students";

        model.addAttribute("student", found);
        return "edit-student";
    }


    // ── 5. UPDATE (Process Edit) ────────────────────────────
    @PostMapping("/students/edit/{id}")
    public String processEdit(
            @PathVariable int id,
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            Model model) {
            
        if (result.hasErrors()) {
            model.addAttribute("editId", id);
            return "edit-student";
        }
        
        // VERY IMPORTANT: Set the ID of the object before saving!
        // If 'id' is NOT set, save() will create a NEW row (INSERT).
        // If 'id' IS set and exists, save() will overwrite the existing row (UPDATE).
        student.setId(id);
        studentRepo.save(student);
        
        return "redirect:/students";
    }

    // ── 6. DELETE ───────────────────────────────────────────
    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable int id) {
        // Run DELETE FROM students WHERE id=?
        studentRepo.deleteById(id);
        return "redirect:/students";
    }
}
