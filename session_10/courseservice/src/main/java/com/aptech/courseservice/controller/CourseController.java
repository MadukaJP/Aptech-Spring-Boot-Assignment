package com.aptech.courseservice.controller;

import com.aptech.courseservice.model.Course;
import com.aptech.courseservice.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/course")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
