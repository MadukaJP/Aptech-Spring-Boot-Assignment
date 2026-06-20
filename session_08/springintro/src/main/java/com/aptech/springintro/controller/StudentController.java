package com.aptech.springintro.controller;

import com.aptech.springintro.client.CourseClient;
import com.aptech.springintro.dto.CourseDTO;
import com.aptech.springintro.model.Student;
import com.aptech.springintro.repository.StudentRepository;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private CourseClient courseClient;

    @GetMapping("/students")
    @PreAuthorize("isAuthenticated()")
    public String list(Model model) {
        List<Student> students = studentRepo.findByActiveTrue();
        model.addAttribute("students", students);
        model.addAttribute("totalCount", students.size());
        return "students";
    }

    @GetMapping("/students/add")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
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
    public String delete(@PathVariable int id) {
        studentRepo.deleteById(id);
        return "redirect:/students";
    }

    @GetMapping("/my-profile")
    public String myProfile(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "my-profile";
    }

    @GetMapping("/my-details")
    public String myDetails(Authentication authentication, Model model) {
        String username = authentication.getName();
        Object principal = authentication.getPrincipal();
        var authorities = authentication.getAuthorities();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("username", username);
        model.addAttribute("roles", authorities.toString());
        model.addAttribute("isAdmin", isAdmin);
        return "my-details";
    }

    @GetMapping("/context-demo")
    public String contextDemo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        return "context-demo";
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model, Principal principal) {
        model.addAttribute("currentUser",    principal.getName());
        model.addAttribute("totalStudents",  studentRepo.count());
        return "admin/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/courses")
    @PreAuthorize("isAuthenticated()")
    public String listCourses(Model model, Principal principal) {
        try {
            List<CourseDTO> courses = courseClient.getAllCourses();
            model.addAttribute("courses", courses);
        } catch (FeignException e) {
            model.addAttribute("courses", Collections.emptyList());
            model.addAttribute("courseError", "Courses are currently unavailable.");
        }

        try {
            List<Integer> enrolledIds = courseClient.getEnrolledCourses(principal.getName());
            model.addAttribute("enrolledCourseIds", enrolledIds);
        } catch (FeignException e) {
            model.addAttribute("enrolledCourseIds", Collections.emptyList());
        }

        return "courses";
    }

    @PostMapping("/courses/enroll/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public String enroll(@PathVariable int courseId, Principal principal, RedirectAttributes ra) {
        try {
            courseClient.enrollStudent(principal.getName(), courseId);
            ra.addFlashAttribute("enrollSuccess", true);
        } catch (FeignException e) {
            ra.addFlashAttribute("enrollError", true);
        }
        return "redirect:/courses";
    }

    @PostMapping("/courses/unenroll/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public String unenroll(@PathVariable int courseId, Principal principal, RedirectAttributes ra) {
        try {
            courseClient.unenrollStudent(principal.getName(), courseId);
            ra.addFlashAttribute("unenrollSuccess", true);
        } catch (FeignException e) {
            ra.addFlashAttribute("unenrollError", true);
        }
        return "redirect:/courses";
    }

    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("isAuthenticated()")
    public String viewCourseStudents(@PathVariable int courseId, Model model) {
        try {
            Map<String, Object> response = courseClient.getCourseStudents(courseId);
            model.addAttribute("studentNames", response.getOrDefault("students", Collections.emptyList()));
            model.addAttribute("studentCount", response.getOrDefault("count", 0));
        } catch (FeignException e) {
            model.addAttribute("studentNames", Collections.emptyList());
            model.addAttribute("studentCount", 0);
            model.addAttribute("courseError", "Course data is currently unavailable.");
        }

        try {
            List<CourseDTO> courses = courseClient.getAllCourses();
            CourseDTO course = courses.stream()
                    .filter(c -> c.getId() == courseId)
                    .findFirst().orElse(null);
            model.addAttribute("course", course);
        } catch (FeignException e) {
            if (!model.containsAttribute("courseError")) {
                model.addAttribute("courseError", "Course data is currently unavailable.");
            }
        }

        return "course-students";
    }
}
