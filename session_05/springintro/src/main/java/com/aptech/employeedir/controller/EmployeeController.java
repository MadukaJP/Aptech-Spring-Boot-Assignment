package com.aptech.employeedir.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.aptech.employeedir.model.Employee;
import com.aptech.employeedir.repository.EmployeeRepository;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/employees")
    public String list(Model model) {
        List<Employee> employees = employeeRepo.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("totalCount", employees.size());
        return "employees";
    }

    @GetMapping("/employees/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add-employee";
    }

    @PostMapping("/employees/add")
    public String processAdd(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {
        if (result.hasErrors()) return "add-employee";
        employeeRepo.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Employee found = employeeRepo.findById(id).orElse(null);
        if (found == null) return "redirect:/employees";
        model.addAttribute("employee", found);
        return "edit-employee";
    }

    @PostMapping("/employees/edit/{id}")
    public String processEdit(
            @PathVariable int id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {
        if (result.hasErrors()) return "edit-employee";
        employee.setId(id);
        employeeRepo.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/delete/{id}")
    public String delete(@PathVariable int id) {
        employeeRepo.deleteById(id);
        return "redirect:/employees";
    }
}
