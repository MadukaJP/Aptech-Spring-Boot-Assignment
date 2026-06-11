

package com.aptech.springintro.controller;


import com.aptech.springintro.model.Student;
import com.aptech.springintro.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;


    // ════════════════════════════════════════════════════════
    // METHOD SECURITY: @PreAuthorize
    // ════════════════════════════════════════════════════════
    // @PreAuthorize runs BEFORE the method body executes.
    // If the condition is false → Spring throws AccessDeniedException → 403
    // This is the SECOND layer of defence (SecurityFilterChain is the first).
    // Having BOTH means even if a URL rule is misconfigured, the method is safe.

    @GetMapping("/students")
    @PreAuthorize("isAuthenticated()")
    // isAuthenticated() = user must be logged in (any role)
    public String list(Model model) {
        List<Student> students = studentRepo.findByActiveTrue();
        model.addAttribute("students", students);
        model.addAttribute("totalCount", students.size());
        return "students";
    }

    @GetMapping("/students/add")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    // hasAnyRole() = user must have at least one of these roles
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @PostMapping("/students/add")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public String processAdd(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result) {
        if (result.hasErrors()) return "add-student";
        student.setActive(true);
        studentRepo.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public String showEditForm(@PathVariable int id, Model model) {
        Student found = studentRepo.findById(id).orElse(null);
        if (found == null) return "redirect:/students";
        model.addAttribute("student", found);
        model.addAttribute("editId", id);
        return "edit-student";
    }

    @PostMapping("/students/edit/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public String processEdit(
            @PathVariable int id,
            @Valid @ModelAttribute("student") Student student,
            BindingResult result, Model model) {
        if (result.hasErrors()) { model.addAttribute("editId", id); return "edit-student"; }
        student.setId(id);
        studentRepo.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // hasRole('ADMIN') = ONLY users with ROLE_ADMIN
    public String delete(@PathVariable int id) {
        studentRepo.deleteById(id);
        return "redirect:/students";
    }


    // ════════════════════════════════════════════════════════
    // GETTING THE CURRENT USER IN JAVA CODE
    // ════════════════════════════════════════════════════════

    // ── Method 1: Principal (Simplest — only in Controllers) ──
    @GetMapping("/my-profile")
    public String myProfile(Principal principal, Model model) {
        // Principal is automatically injected by Spring MVC
        // It represents the currently logged-in user
        String username = principal.getName(); // e.g., "alice"

        model.addAttribute("username", username);
        return "my-profile";
    }

    // ── Method 2: Authentication object (Richer — more info) ──
    @GetMapping("/my-details")
    public String myDetails(Authentication authentication, Model model) {
        String username = authentication.getName();            // "alice"
        Object principal = authentication.getPrincipal();      // UserDetails object
        var authorities = authentication.getAuthorities();     // [ROLE_STUDENT]

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("username", username);
        model.addAttribute("roles", authorities.toString());
        model.addAttribute("isAdmin", isAdmin);
        return "my-details";
    }

    // ── Method 3: SecurityContextHolder (Works ANYWHERE — in Services too) ──
    @GetMapping("/context-demo")
    public String contextDemo(Model model) {
        // SecurityContextHolder is a static global holder for the security context.
        // Use this when you are NOT in a controller (e.g., inside a @Service class)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("username", username);
        return "context-demo";
    }

    // ── Admin Dashboard ──────────────────────────────────────
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model, Principal principal) {
        model.addAttribute("currentUser",    principal.getName());
        model.addAttribute("totalStudents",  studentRepo.count());
        return "admin/dashboard";
    }

    // Login page (GET — shows the form)
    // The POST to /login is handled automatically by Spring Security
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}