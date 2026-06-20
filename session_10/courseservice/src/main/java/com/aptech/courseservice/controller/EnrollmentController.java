package com.aptech.courseservice.controller;

import com.aptech.courseservice.client.StudentClient;
import com.aptech.courseservice.dto.StudentDTO;

import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnrollmentController {

    @Autowired
    private StudentClient studentClient;

    @GetMapping("/course/verify-student/{studentId}")
    public String verifyStudent(@PathVariable int studentId) {
        try {
            StudentDTO student = studentClient.getStudentById(studentId);

            return "Success! Found student: " + student.name() +
                    " who is enrolled in " + student.course();
        } catch (FeignException.Unauthorized e) {
            return "Error: Invalid API key provided.";
        } catch (FeignException.NotFound e) {
            return "Error: No student exists with ID " + studentId + ".";
        } catch (FeignException e) {
            return "Error: Student Service is currently offline. Please try again later.";
        }
    }
}
